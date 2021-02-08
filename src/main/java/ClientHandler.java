import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

public class ClientHandler {
    LogService logService = new LogService(ClientHandler.class.getName());
    AuthService authService;
    ServerApp server;
    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    Client client;

    ClientHandler(AuthService authService, ServerApp server, Socket socket, ExecutorService executorService) {
        executorService.submit(() -> {
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
            e.printStackTrace();
            server.onClientDisconnected(this, socket, dataInputStream, dataOutputStream);
        }
    });
    }

    void sendMessage(String time, Client client, String text) {
        try {
            dataOutputStream.writeUTF(time + " " + client.name + ": " + text);
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
                logService.logInfo(client.name + " Пользователь был отключен от сервера по таймауту");
                JOptionPane.showMessageDialog(null, "Вы были отключены от сервера (превышено время ожидания). \nПерезапустите приложение для повторного входа");
                server.onClientDisconnected(clientHandler, socket, dataInputStream, dataOutputStream);
            }
        }, 15 * 1000);
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
                            timer.cancel();
                            dataOutputStream.writeUTF("ok");
                            dataOutputStream.writeUTF("/userName " + client.name);
                            logService.logInfo(client.login + " залогинился");
                            break;
                        } else {
                            logService.logInfo("Кто-то ввёл неверные логин/пароль (" + login + " / " + password);
                            dataOutputStream.writeUTF("Неверные логин или пароль!");
                        }
                    }
                } else {
                    dataOutputStream.writeUTF("Ошибка авторизации!");
                    logService.logger.log(Level.SEVERE, newMessage + " получил ошибку авторизации!");
                }
                if (tryCount == maxTryCount) {
                    dataOutputStream.writeUTF("Первышен лимит попыток авторизции!");
                    logService.logger.log(Level.WARNING, newMessage + " превышен лимит попыток авторизции!");
                    server.onClientDisconnected(this, socket, dataInputStream, dataOutputStream);
                    return false;
                }
            }
            return true;
        }
    }

    private void messageListener(DataInputStream dataInputStream) throws IOException {
        while (socket.isBound()) {
            String newMessage = dataInputStream.readUTF();
            if (newMessage.equals("/exit")) {
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
                    logService.logConfig(client.login + " сменил имя (" + nameText[1] + ")");
                }
            } else {
                server.onNewMessage(client, newMessage);
            }

        }
    }
}
