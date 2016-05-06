import javax.swing.*;
import java.awt.*;

/**
 * Created by Or Keren on 06/05/2016.
 */
public class TechnicianMainMenu extends JFrame {
    private JButton addMeterButton;
    private JButton viewMetersButton;
    private JPanel rootPanel;

    public TechnicianMainMenu(){
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
