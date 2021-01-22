import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatApiHandler {

    interface Callback {
        void addNewMessage(String text);
    }

    static class Error extends Exception {
        Error(Throwable cause) {
            super(cause);
        }
    }

    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    Socket socket;

    ChatApiHandler() {
        try {
            socket = new Socket("localhost", 8083);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized boolean auth(String login, String password, Callback callback) throws Error {
        if (!login.equals("") && !password.equals("")) {
            try {
                dataOutputStream.writeUTF("/auth " + login + " " + password);
                String response = dataInputStream.readUTF();
                if (response.equals("ok")) {
                    startListenServer(callback);
                    return true;
                } else {
                    throw new Error(new Exception(response));
                }
            } catch (Exception e) {
                throw new Error(e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Поля не могут быть пустыми!");
            return false;
        }
    }

    synchronized void sendMessage(String text) throws Error {
        if (!text.equals("")) {
            try {
                dataOutputStream.writeUTF(text);
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error(e);
            }
        }
    }

    synchronized void startListenServer(Callback callback) {
        new Thread(() -> {
            try {
                while (socket.isBound()) {
                    String newMessage = dataInputStream.readUTF();
                    System.out.println(newMessage);
                    callback.addNewMessage(newMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}


