import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class ServerApp {

    List<Message> messages = new ArrayList<>();
    List<ClientHandler> clients = new ArrayList<>();
    LogService logService = new LogService(ServerApp.class.getName());

    ServerApp() {
        int PORT = 8083;
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            AuthService authService = new AuthService();
            System.out.println("Сервер запущен на порту '" + PORT + "'. Ожидает подключений...");
            logService.logInfo("Сервер запущен на порту '" + PORT + "'. Ожидает подключений...");
            // Обработчик клиентов
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(authService, this, socket, executorService);
            }

        } catch (IOException e) {
            logService.logger.log(Level.SEVERE, "Сервер завершил работу с ошибкой!");
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    synchronized void onNewMessage(Client sender, String text) {
        Message message = new Message(getDateAndTime(), sender, text);
        messages.add(message);
//        System.out.println("log: " + sender.name + ": " + text);
        logService.logInfo("Сообщение от пользователя " + sender.login);
        // Рассылаем сообщения всем
        for (int i = 0; i < clients.size(); i++) {
            ClientHandler recipient = clients.get(i);
            recipient.sendMessage(message.time, sender, text);
        }
    }

    synchronized void sendPrivateMessage(Client sender, String toClient, String text, DataOutputStream dataOutputStream) {
        boolean isRecipientExist = false;
        for (int i = 0; i < clients.size(); i++) {
            ClientHandler recipient = clients.get(i);
            if (recipient.client.name.equalsIgnoreCase(toClient)) {
                isRecipientExist = true;
                Message message = new Message(getDateAndTime(), sender, text);
                recipient.sendMessage(message.time + " Личное сообщение от", sender, text);
                logService.logInfo(sender.login + " отправил личное сообщение для (" + toClient + ")");
                break;
            }
        }
        if (!isRecipientExist) {
            try {
                dataOutputStream.writeUTF("Данный пользователь не в сети или не зарегистрирован");
                logService.logInfo(sender.login + " попытался отправить сообщение несуществующему/не зарегистрированному пользователю (" + toClient + ")");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized void onNewClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
        // Последние сообщения с сервера пришлось пока отключить, дабы они не дублировались с локальной историей при переподключении клиента.
        // Пока идей нет, как это реализовать совместно (буду рад предложениям :)

//        for (int i = 0; i < messages.size(); i++) {
//            Message message = messages.get(i);
//            clientHandler.sendMessage(message.time, message.client, message.text);
//        }
        onNewMessage(clientHandler.client, "вошёл в чат");
        logService.logInfo(clientHandler.client.login + " вошёл в чат");
    }

    synchronized void onClientDisconnected(ClientHandler clientHandler, Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        try {
            onNewMessage(clientHandler.client, "покиул чат");
            logService.logInfo(clientHandler.client.login + " покиул чат");
            clients.remove(clientHandler);
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ServerApp();
    }

    public String getDateAndTime() {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("[dd.MM.yyyy '@' HH:mm:ss]");
        return formatForDateNow.format(dateNow);
    }

    public void onClientChangeName(Client client, String newName) {
        DBQuery dbQuery = new DBQuery();
        try {
            dbQuery.changeName(client, newName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for (ClientHandler clientHandler : clients) {
            if (clientHandler.client.equals(client)) {
                clientHandler.client.name = newName;
            }
        }

    }
}
