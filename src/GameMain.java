import javax.swing.*;
import java.awt.*;

public class GameMain {
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public GameMain() {
        frame = new JFrame("Chicken Invaders");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        frame.add(cardPanel);
        frame.setVisible(true);
    }

    public void switchPanel(String panelName) {
        cardLayout.show(cardPanel, panelName);
    }

}