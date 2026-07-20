import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private GameMain gameMain;

    public static boolean musicOn = true;
    public static boolean shotOn = true;
    public static boolean crashOn = true;
    public static boolean gameOverOn = true;

    public SettingsPanel(GameMain gameMain) {
        this.gameMain = gameMain;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        add(Box.createVerticalStrut(60));

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createVerticalStrut(40));

        JCheckBox musicBox = createCheckBox("Background Music", musicOn);
        JCheckBox shotBox = createCheckBox("Shot Sound", shotOn);
        JCheckBox crashBox = createCheckBox("Crash / Explosion Sound", crashOn);
        JCheckBox gameOverBox = createCheckBox("Game Over / Win Sound", gameOverOn);

        musicBox.addActionListener(e -> musicOn = musicBox.isSelected());
        shotBox.addActionListener(e -> shotOn = shotBox.isSelected());
        crashBox.addActionListener(e -> crashOn = crashBox.isSelected());
        gameOverBox.addActionListener(e -> gameOverOn = gameOverBox.isSelected());

        add(musicBox);
        add(Box.createVerticalStrut(15));
        add(shotBox);
        add(Box.createVerticalStrut(15));
        add(crashBox);
        add(Box.createVerticalStrut(15));
        add(gameOverBox);

        add(Box.createVerticalStrut(40));

        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(150, 35));
        backButton.addActionListener(e -> gameMain.switchPanel("MainMenu"));
        add(backButton);
    }

    private JCheckBox createCheckBox(String label, boolean initial) {
        JCheckBox box = new JCheckBox(label, initial);
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.setForeground(Color.WHITE);
        box.setOpaque(false);
        box.setFont(new Font("Arial", Font.PLAIN, 18));
        return box;
    }
}