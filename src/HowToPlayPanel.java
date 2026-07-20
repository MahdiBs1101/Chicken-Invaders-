import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends JPanel {
    private GameMain gameMain;

    public HowToPlayPanel(GameMain gameMain) {
        this.gameMain = gameMain;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("How To Play", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JTextArea instructions = new JTextArea();
        instructions.setEditable(false);
        instructions.setOpaque(false);
        instructions.setForeground(Color.WHITE);
        instructions.setFont(new Font("Arial", Font.PLAIN, 18));
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        instructions.setText(
                "Controls:\n\n" +
                        "  Arrow Keys / WASD   -   Move the plane\n" +
                        "  Space               -   Shoot\n" +
                        "  Esc                 -   Return to Main Menu\n\n" +
                        "Goal:\n\n" +
                        "  Destroy the chickens before they overwhelm you.\n" +
                        "  Avoid falling eggs and enemy bullets.\n" +
                        "  Defeat the boss at the end of stages 4 and 8.\n\n" +
                        "Power-Ups:\n\n" +
                        "  Rapid Fire    - Increases fire rate temporarily\n" +
                        "  Freeze Bomb   - Freezes all enemies temporarily\n" +
                        "  Extra Life    - Adds one life (up to 5)\n" +
                        "  Shield        - Protects you from eggs temporarily\n" +
                        "  Add Fire      - Permanently adds an extra bullet\n\n" +
                        "Good luck, pilot!"
        );

        JScrollPane scrollPane = new JScrollPane(instructions);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 60));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.addActionListener(e -> gameMain.switchPanel("MainMenu"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}