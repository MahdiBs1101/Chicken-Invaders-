import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private GameMain gameMain;
    private Image backgroundImage;
    private Image airplaneImage;

    private int airplaneX = 375;
    private int airplaneY = 500;

    private boolean up, down, left, right;

    public GamePanel(GameMain gameMain) {
        this.gameMain = gameMain;
        try {
            backgroundImage = ImageIO.read(new File("assets/background/background.jpg"));
            airplaneImage = ImageIO.read(new File("assets/airplan/1.png"));
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                requestFocusInWindow();
            }
        });

        this.setFocusable(true);
        this.addKeyListener(this);

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        if (airplaneImage != null) {
            g.drawImage(airplaneImage, airplaneX, airplaneY, 50, 50, this);
        }

        g.setColor(Color.WHITE);
        g.drawString("Chicken Invaders Engine is Running!", 300, 300);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (left && airplaneX > 0) airplaneX -= 7;
        if (right && airplaneX < getWidth() - 50) airplaneX += 7;
        if (up && airplaneY > 0) airplaneY -= 7;
        if (down && airplaneY < getHeight() - 50) airplaneY += 7;

        repaint();
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) left = true;
        if (key == KeyEvent.VK_D) right = true;
        if (key == KeyEvent.VK_W) up = true;
        if (key == KeyEvent.VK_S) down = true;
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) left = false;
        if (key == KeyEvent.VK_D) right = false;
        if (key == KeyEvent.VK_W) up = false;
        if (key == KeyEvent.VK_S) down = false;
    }
    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {}
}