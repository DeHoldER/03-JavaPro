

public class ClientApp implements AuthWindow.Callback, ChatWindow.Callback {

    ChatApiHandler api;
    AuthWindow authWindow;
    ChatWindow chatWindow;
    HistoryService historyService = new HistoryService();

    ClientApp() {
        api = new ChatApiHandler();
        authWindow = new AuthWindow(this);
        chatWindow = new ChatWindow(this);
    }

    public static void main(String[] args) {
        new ClientApp();
    }

    @Override
    public synchronized void onLoginClick(String login, String password) {
            try {
                boolean isSuccess = api.auth(login, password, chatWindow);
                System.out.println("login:" + isSuccess);
                if (isSuccess) {
                    authWindow.dispose();
                    historyService.setLogin(login);
                    chatWindow.setHistoryService(historyService);
                    chatWindow.readHistory(historyService);
                    showChatWindow();
                } else {
                    if (!login.equals("") && !password.equals("")) {
                        authWindow.showError("Неверный логин или пароль");
                    }
                }
            } catch (ChatApiHandler.Error error) {
                error.printStackTrace();
                if (error.getCause().getMessage() != null) {
                    authWindow.showError("Ошибка!\n" + error.getCause().getMessage());
                } else {
                    authWindow.showError("Ошибка сервера!");
                }
            }
    }

    @Override
    public void sendMessage(String text) {
        try {
            api.sendMessage(text);
            chatWindow.msgSend(text);
        } catch (ChatApiHandler.Error error) {
            // Обработать ошибку
        }

    }

    private void showChatWindow() {
        chatWindow.setVisible(true);
        chatWindow.txtFieldSetFocus();
    }

    private void hideChatWindow() {
        chatWindow.setVisible(false);
    }
}
