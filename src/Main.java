import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Main extends JFrame implements KeyListener {
    int speed = 5;
    int height = 625;
    boolean jumped = false;
    int vel = 0;
    boolean onPlat = true;

    private BufferedImage image;

    public Main(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        setTitle("Geometry Walk");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);  // Draw the BufferedImage onto the panel
            }
        };
        add(panel);
        setVisible(true);

        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    public void drawSquare(int x, int y, Color color) {
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(x, y, 25, 25);  // Draw square at (x, y)
        g2d.dispose();
        repaint();  // Refresh the JFrame
    }

    public void clearImage(Color backgroundColor) {
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());  // Clear the entire image
        g2d.dispose();
        repaint();  // Refresh the JFrame
    }

    public void updatePosition() {
        // Clear the previous frame
        clearImage(Color.WHITE);

        // Draw the square at the updated position
        drawSquare(250, height, Color.RED);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_SPACE) {
            jumped = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        Main frame = new Main(1000, 700);

        // Start the thread to update the square's position every millisecond
        new Thread(() -> {
            while (true) {
                try {
                    if (frame.jumped) {
                        frame.jumped = false;
                        if (frame.onPlat) {
                            frame.vel -= 20;
                        }
                    }

                    // move cube on base of velocity
                    if (frame.vel >= 5) {
                        frame.vel -= 5;
                        frame.height += 5;
                    } else if (frame.vel < 0) {
                        if (frame.vel <= -5) {
                            frame.vel += 5;
                            frame.height -= 5;
                        }
                    }

                    /*//move the environment
                    for (int i = 0; i < env.length; i++) {
                        int[] p = env[i];

                        p[0] += frame.dirVel;
                        p[1] += frame.dirVel;
                    }*/
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                frame.updatePosition();
            }
        }).start();
    }
}