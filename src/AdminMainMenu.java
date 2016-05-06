import javax.swing.*;
import java.awt.*;

/**
 * Created by Or Keren on 05/05/2016.
 */
public class AdminMainMenu extends JFrame{
    private JPanel rootPanel;
    private JButton viewMetersButton;
    private JButton viewCustomersButton;
    private JButton viewTechniciansButton;

    public AdminMainMenu(){
        super("AdminMainMenu");
        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
