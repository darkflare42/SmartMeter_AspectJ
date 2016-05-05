import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Or Keren on 05/05/2016.
 */
public class LoginScreen extends JFrame{
    private JPasswordField passwordField1;
    private JPanel rootPanel;
    private JButton loginButton;
    private JTextField textField1;

    public LoginScreen(){
        super("LoginScreen");
        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        //setVisible(true);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        setSize(width/4, height/4);

        // here's the part where i center the jframe on screen
        setLocationRelativeTo(null);

        setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
                MainMenu m = new MainMenu();
            }
        });
    }
}
