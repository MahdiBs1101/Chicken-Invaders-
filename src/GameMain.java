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

        LoginPanel loginPanel = new LoginPanel(this);
        cardPanel.add(loginPanel, "Login");

        MainMenu mainMenu = new MainMenu(this);
        cardPanel.add(mainMenu, "MainMenu");

        GamePanel gamePanel = new GamePanel(this);
        cardPanel.add(gamePanel, "GamePanel");

        frame.add(cardPanel);
        frame.setVisible(true);
    }

    public void switchPanel(String panelName) {
        cardLayout.show(cardPanel, panelName);
    }

}