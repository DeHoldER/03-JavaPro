import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AuthWindow extends JFrame {

    private static final Dimension DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int WINDOW_WIDTH = 260;
    private static final int WINDOW_HEIGHT = 170;
    private static final int SCREEN_CENTER_X = (DIMENSION.width / 2) - WINDOW_WIDTH / 2;
    private static final int SCREEN_CENTER_Y = (DIMENSION.height / 2) - WINDOW_HEIGHT / 2;

    interface Callback {
        void onLoginClick(String login, String password);
    }

    public AuthWindow(Callback callback) {
        setTitle("Авторизация");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(SCREEN_CENTER_X, SCREEN_CENTER_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        setLayout(new GridLayout(5, 1));
        setResizable(false);

        // ELEMENTS
        JLabel labelLogin = new JLabel("Введите логин:");
        JLabel labelPass = new JLabel("Введите пароль:");
        JTextField loginField = new JTextField();
        JTextField passwordField = new JTextField();

        JButton enterButton = new JButton("Войти в чат");

        add(labelLogin);
        add(loginField);
        add(labelPass);
        add(passwordField);
        add(enterButton);

        loginField.setText("pavel1");
        passwordField.setText("123");

        setVisible(true);

        enterButton.addActionListener((actionEvent) -> {
            callback.onLoginClick(loginField.getText(), passwordField.getText());
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    callback.onLoginClick(loginField.getText(), passwordField.getText());
                    e.consume();
                }
            }
        });

    }

//    public void onAuthClick(DataOutputStream out) {
//        if (!loginField.getText().equals("") && !passField.getText().equals("")) {
//            try {
//                out.writeUTF("/auth " + loginField.getText() + " " + passField.getText());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            JOptionPane.showMessageDialog(null,"Поля не могут быть пустыми!");
//        }
//    }

    void showError(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

}