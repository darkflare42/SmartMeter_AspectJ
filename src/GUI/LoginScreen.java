package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Or Keren on 05/05/2016.
 */
public class LoginScreen extends JFrame{
    private JPasswordField password;
    private JPanel rootPanel;
    private JButton loginButton;
    private JTextField userName;

    public LoginScreen(){
        super("LoginScreen");
        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();


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
                if(DBComm.login(userName.getText(), new String(password.getPassword()))){
                    MainTest.currentUser = DBComm.getUser(userName.getText());
                    MainTest.currentUser.openMainMenu(); //This opens up the main menu that is appropriate for the current user type
                    setVisible(false);
                    dispose();
                }
            }
        });
    }
}
