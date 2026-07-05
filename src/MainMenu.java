import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel {
    private GameMain gameMain;
    private Image backgroundImage;

    public MainMenu(GameMain gameMain) {
        this.gameMain = gameMain;


        try {
            backgroundImage = ImageIO.read(new File("assets/background/background2.jpg"));
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalStrut(100));

        JLabel titleLabel = new JLabel("Main Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        add(Box.createVerticalStrut(100));

        JButton newGameButton = new JButton("New Game");
        JButton settingsButton = new JButton("Settings");
        JButton exitButton = new JButton("Exit");

        Dimension buttonSize = new Dimension(200, 40);
        JButton[] buttons = {newGameButton, settingsButton, exitButton};
        for (JButton btn : buttons) {
            btn.setMaximumSize(buttonSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFont(new Font("Arial", Font.BOLD, 30));
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setForeground(Color.WHITE);
        }

        add(newGameButton);
        add(Box.createVerticalStrut(20));
        add(settingsButton);
        add(Box.createVerticalStrut(20));
        add(exitButton);

        newGameButton.addActionListener(e -> {
            System.out.println("Start Game!");
        });

        settingsButton.addActionListener(e -> {
            System.out.println("Go to Settings");
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}