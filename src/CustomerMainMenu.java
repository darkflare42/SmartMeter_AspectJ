import javax.swing.*;
import java.awt.*;

/**
 * Created by Or Keren on 06/05/2016.
 */
public class CustomerMainMenu extends JFrame {
    private JButton viewMetersButton;
    private JButton viewMonthlyBillButton;
    private JPanel rootPanel;

    public CustomerMainMenu(){
        super("CustomerMainMenu");
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
