import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class OptionsWindow extends JFrame {


    private static final Dimension DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 150;
    private static final int SCREEN_CENTER_X = (DIMENSION.width / 2) - WINDOW_WIDTH / 2;
    private static final int SCREEN_CENTER_Y = (DIMENSION.height / 2) - WINDOW_HEIGHT / 2;

    public OptionsWindow(ChatWindow.Callback callback) {
        setResizable(false);
        setTitle("Options");
        setBounds(SCREEN_CENTER_X, SCREEN_CENTER_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBounds(5, 5, WINDOW_WIDTH, WINDOW_HEIGHT);
        optionsPanel.setLayout(null);

        JLabel labelUserName = new JLabel("Ваше имя: ");
        labelUserName.setBounds(5, 5, 70, 25);
        JTextField name = new JTextField(ChatWindow.getUserName());
        name.setBounds(labelUserName.getWidth(), labelUserName.getY(), WINDOW_WIDTH - labelUserName.getWidth() - 85, labelUserName.getHeight());
        JButton rename = new JButton("Ok");
        rename.setBounds(name.getX() + name.getWidth() + 5, name.getY(), 55, name.getHeight());

        rename.addActionListener(e -> {
            if (!name.getText().equals("")) {
                ChatWindow.setUserName(name.getText(), callback);
                dispose();
            }
        });
        name.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    if (!name.getText().equals("")) {
                        ChatWindow.setUserName(name.getText(), callback);
                        dispose();
                    }
                }
            }
        });

        optionsPanel.add(rename);
        optionsPanel.add(labelUserName);
        optionsPanel.add(name);

        add(optionsPanel);
        setVisible(true);
    }
}
