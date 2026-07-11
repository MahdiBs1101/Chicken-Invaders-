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
    private Plane plane;
    private Grid grid;
    private boolean up, down, left, right, shooting;

    public GamePanel(GameMain gameMain) {
        this.gameMain = gameMain;
        try {
            backgroundImage = ImageIO.read(new File("assets/background/background.jpg"));
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        plane = new DefaultPlane(375, 500);
        grid = new Grid(50, 50, 1.0, 20, 2, 800);


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

        plane.draw(g);
        grid.draw(g);

        g.setColor(Color.WHITE);
        g.drawString("Lives: " + plane.getLives() + "   Bullets: " + plane.getBulletCount(), 20, 20);

        if (grid.isStageComplete()) {
            g.drawString("Complete!", 350, 300);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (left)  plane.moveLeft(getWidth());
        if (right) plane.moveRight(getWidth());
        if (up)    plane.moveUp(getHeight());
        if (down)  plane.moveDown(getHeight());
        if (shooting) plane.shoot();

        plane.update(getHeight());
        grid.update();

        checkBulletEnemyCollisions();

        repaint();
    }

    private void checkBulletEnemyCollisions() {
        for (Bullet b : plane.getBullets()) {
            if (!b.isActive()) continue;
            for (Enemy en : grid.getAllEnemies()) {
                if (b.getBounds().intersects(en.getBounds())) {
                    en.takeHit();
                    b.deactivate();
                    break;
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) left = true;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) right = true;
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) up = true;
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) down = true;
        if (key == KeyEvent.VK_SPACE) shooting = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) left = false;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) right = false;
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) up = false;
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) down = false;
        if (key == KeyEvent.VK_SPACE) shooting = false;
    }
    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {}
}