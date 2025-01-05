import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Timer;
import java.util.TimerTask;

// class AnotherClass {
//     public void printConfig() {
//         System.out.println("Rows: " + GameConfig.ROWS);
//         System.out.println("Columns: " + GameConfig.COLUMNS);
//         System.out.println("Tile Size: " + GameConfig.TILE_SIZE);
//     }
// }


public class Rabbit {
    private BufferedImage image;
    private Point pos;
    private boolean isAlive;
    private final int TILE_SIZE = GameConfig.TILE_SIZE;
    private Timer timer;
    private int score;
    
    public Rabbit() {
        loadImage();
        setRandomPosition();
        isAlive = true;
        startMovementTimer();
        score = 0;
    }
    
    private void loadImage() {
        try {
            image = ImageIO.read(new File("images/rabbit.png"));
            if (image == null) {
                System.out.println("Warning: Rabbit image failed to load");
            }
        } catch (IOException e) {
            System.out.println("Error loading rabbit image: " + e.getMessage());
            image = createDefaultRabbitImage();
        }
    }
    
    private BufferedImage createDefaultRabbitImage() {
        BufferedImage defaultImage = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = defaultImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillOval(0, 0, TILE_SIZE-1, TILE_SIZE-1);
        g.setColor(Color.BLACK);
        g.drawOval(0, 0, TILE_SIZE-1, TILE_SIZE-1);
        g.dispose();
        return defaultImage;
    }
    
    private void startMovementTimer() {
        timer = new Timer(true); // Make it a daemon timer
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Toggle visibility and move to new position
                if (isAlive) {
                    // If visible, make invisible
                    isAlive = false;
                } else {
                    // If invisible, make visible at new position
                    setRandomPosition();
                    isAlive = true;
                }
            }
        }, 2500, 2500); // Initial delay 2.5s, repeat every 2.5s
    }
    
    public void setRandomPosition() {
        int maxX = GameConfig.COLUMNS - 1;
        int maxY = GameConfig.ROWS - 1;
        pos = new Point(
            (int) (Math.random() * maxX),
            (int) (Math.random() * maxY)
        );
    }
    
    public void draw(Graphics g, Component observer) {
        if (!isAlive) return;
        
        int x = pos.x * TILE_SIZE;
        int y = pos.y * TILE_SIZE;
        g.drawImage(image, x, y, TILE_SIZE, TILE_SIZE, observer);
    }
    
    public Point getPos() {
        return pos;
    }
    
    public boolean isAlive() {
        return isAlive;
    }
    
    public void eliminate() {
        isAlive = false;
        if (timer != null) {
            timer.cancel(); // Stop the timer when rabbit is eliminated by dog
        }
    }
    
    public boolean checkCollision(Point otherPos) {
        return isAlive && pos.equals(otherPos);
    }
    
    // Add this method to clean up when the game ends
    public void cleanup() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    public void addScore(int amount) {
        score += amount;
    }

    public String getScore() {
        return String.valueOf(score);
    }
}