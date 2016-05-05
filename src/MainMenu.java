import javax.swing.*;
import java.awt.*;

/**
 * Created by Or Keren on 05/05/2016.
 */
public class MainMenu extends JFrame{
    private JPanel rootPanel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;

    public MainMenu(){
        super("MainMenu");
        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        setSize(width/4, height/2);

        // here's the part where i center the jframe on screen
        setLocationRelativeTo(null);

        setVisible(true);
    }

}
