
import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler {
    AuthService authService;
    ServerApp server;
    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    Client client;

    ClientHandler(AuthService authService, ServerApp server, Socket socket) {
        this.authService = authService;
        this.server = server;
        this.socket = socket;
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            if (!authorize(dataInputStream, dataOutputStream)) {
                // Удаляемся из сервера
                server.onClientDisconnected(this, socket, dataInputStream, dataOutputStream);
                return;
            }
            server.onNewClient(this);
            messageListener(dataInputStream);

        } catch (IOException e) {
            server.onClientDisconnected(this, socket, dataInputStream, dataOutputStream);
            e.printStackTrace();
        }
    }

    void sendMessage(String time, Client client, String text) {
        try {
            dataOutputStream.writeUTF(time + " " + client.name + ": " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String time, String privateMessage, Client client, String text) {
        try {
            dataOutputStream.writeUTF(time + " >>> " + privateMessage + client.name + ": " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean authorize(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        // Цикл ожидания авторизации клиентов

        ClientHandler clientHandler = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, "Вы были отключены от сервера (превышено время ожидания). \nПерезапустите приложение для повторного входа");
                server.onClientDisconnected(clientHandler, socket, dataInputStream, dataOutputStream);
            }
        }, 10 * 1000);

//        Runnable task = () -> {
//            int timeCounter = 0;
//            while (!Thread.interrupted()) {
//                try {
//                    Thread.sleep(1000);
//                    System.out.println(timeCounter);
//                    timeCounter++;
//                    if (timeCounter == 10) {
//                        try {
//                            dataOutputStream.writeUTF("/timeout");
//                            server.onClientDisconnected(this, socket, dataInputStream, dataOutputStream);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        Thread.currentThread().interrupt();
//                    }
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                    e.printStackTrace();
//                }
//                        System.out.println("Таймаут");
//            }
//        };
//        Thread timeOutCounter = new Thread(task);
//        timeOutCounter.start();

        {
            int tryCount = 0;
            int maxTryCount = 5;
            while (true) {
                // Читаем команду от клиента
                String newMessage = dataInputStream.readUTF();
                // Разбиваем сообщение на составляющие
                String[] messageData = newMessage.split("\\s");
                // Проверяем, команда ли это авторизации
                if (newMessage.startsWith("/auth")) {
                    if (messageData.length == 3 && messageData[0].equals("/auth")) {
                        tryCount++;
                        String login = messageData[1];
                        String password = messageData[2];
                        client = authService.authorize(login, password);
                        // Проверяем, зарегистрирован ли клиент
                        if (client != null) {
                            // Если получилось авторизоваться, то выходим из цикла
//                            timeOutCounter.interrupt();
                            timer.cancel();
                            dataOutputStream.writeUTF("ok");
                            dataOutputStream.writeUTF("/userName " + client.name);
                            break;
                        } else {
                            dataOutputStream.writeUTF("Неверные логин или пароль!");
                        }
                    }
                } else {
                    dataOutputStream.writeUTF("Ошибка авторизации!");
                }
                if (tryCount == maxTryCount) {
                    dataOutputStream.writeUTF("Первышен лимит попыток авторизции!");
                    server.onClientDisconnected(this, socket, dataInputStream, dataOutputStream);
                    return false;
                }
            }
            return true;
        }
    }

    private void messageListener(DataInputStream dataInputStream) throws IOException {
        while (true) {
            String newMessage = dataInputStream.readUTF();
            if (newMessage.equals("/exit")) {
//                dataOutputStream.writeUTF("ok");
                server.onClientDisconnected(this, socket, dataInputStream, dataOutputStream);
            } else if (newMessage.startsWith("/w")) {
                String[] privateMessageData = newMessage.split("\\s", 3);
                if (privateMessageData.length == 3) {
                    String recipient = privateMessageData[1];
                    server.sendPrivateMessage(client, recipient, privateMessageData[2], dataOutputStream);
                }
            } else if (newMessage.startsWith("/nn")) {
                String[] nameText = newMessage.split("\\s", 2);
                if (nameText.length == 2) {
                    String newName = nameText[1];
                    server.onClientChangeName(client, nameText[1]);
                    dataOutputStream.writeUTF("/userName " + nameText[1]);
                }
            } else {
                server.onNewMessage(client, newMessage);
            }

        }
    }
}
