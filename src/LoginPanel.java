import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private GameMain gameMain;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel(GameMain gameMain) {
        this.gameMain = gameMain;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalStrut(120));

        JLabel titleLabel = new JLabel("Chicken Invaders");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(40));

        JPanel userPanel = new JPanel();
        userPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        userPanel.add(usernameField);
        add(userPanel);


        JPanel passPanel = new JPanel();
        passPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        passPanel.add(passwordField);
        add(passPanel);


        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        add(buttonPanel);


        DatabaseManager dbManager = DatabaseManager.db();

        loginButton.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dbManager.validateLogin(user, pass)) {
                JOptionPane.showMessageDialog(this,
                        "Login Successful! Welcome " + user, "Success", JOptionPane.INFORMATION_MESSAGE);
                gameMain.switchPanel("MainMenu");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dbManager.registerUser(user, pass)) {
                JOptionPane.showMessageDialog(this,
                        "Registration Successful! You can now login.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Username already exists or database error!",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}