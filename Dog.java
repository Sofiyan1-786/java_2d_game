import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Dog {
    // Position and movement
    private Point pos;
    private Point vel;
    
    // Size constants
    private final int TILE_SIZE = Board.TILE_SIZE;  // Use the Board's tile size
    
    // Dog's movement speed
    private final int SPEED = 4;
    
    // Image for the dog
    private BufferedImage dogImage;
    private Color debugColor;  // Color used if image fails to load
    
    public Dog() {
        // Start the dog at position (3,3)
        pos = new Point(3, 3);
        vel = new Point(0, 0);
        debugColor = new Color(139, 69, 19); // Brown color for debug rectangle
        
        loadDogImage();
    }
    
    private void loadDogImage() {
        try {
            // Try multiple ways to load the image
            
            // Method 1: Try loading from project resources
            URL resourceUrl = getClass().getResource("/images/dog.jpg");
            if (resourceUrl != null) {
                dogImage = ImageIO.read(resourceUrl);
                System.out.println("Successfully loaded dog image from resources");
                return;
            }
            
            // If we get here, no image was found
            System.out.println("Could not find dog.png in any location");
            System.out.println("Looked in: ");
            System.out.println("- resources folder");
            System.out.println("- " + new File("images/dog.png").getAbsolutePath());
            System.out.println("- " + new File("images/dog.png").getAbsolutePath());
            
        } catch (IOException e) {
            System.out.println("Error loading dog image: " + e.getMessage());
            e.printStackTrace();
        }
        
        // If image loading failed, create a default colored rectangle
        dogImage = createDefaultDogImage();
        System.out.println("Created default dog image (colored rectangle)");
    }
    
    private BufferedImage createDefaultDogImage() {
        // Create a default colored rectangle if no image is available
        BufferedImage defaultImage = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = defaultImage.createGraphics();
        
        // Draw filled rectangle
        g.setColor(debugColor);
        g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        
        // Draw border
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, TILE_SIZE-1, TILE_SIZE-1);
        
        // Draw "DOG" text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        String text = "DOG";
        int textX = (TILE_SIZE - fm.stringWidth(text)) / 2;
        int textY = (TILE_SIZE + fm.getAscent()) / 2;
        g.drawString(text, textX, textY);
        
        g.dispose();
        return defaultImage;
    }
    
    public void tick() {
        // Update position based on velocity
        pos.translate(vel.x, vel.y);
        
        // Keep the dog on the board
        if (pos.x < 0) pos.x = 0;
        if (pos.x >= Board.COLUMNS) pos.x = Board.COLUMNS - 1;
        if (pos.y < 0) pos.y = 0;
        if (pos.y >= Board.ROWS) pos.y = Board.ROWS - 1;
    }
    
    public void draw(Graphics g, Component observer) {
        // Calculate pixel position
        int x = pos.x * TILE_SIZE;
        int y = pos.y * TILE_SIZE;
        
        // Draw the dog image
        g.drawImage(dogImage, x, y, TILE_SIZE, TILE_SIZE, observer);
    }
    
    public void keyPressed(KeyEvent e) {
        // WASD controls for the dog
        int key = e.getKeyCode();
        
        // Reset velocity
        vel.x = 0;
        vel.y = 0;
        
        // Update velocity based on key press
        if (key == KeyEvent.VK_UP) {
            pos.translate(0, -1);
        }
        if (key == KeyEvent.VK_RIGHT) {
            pos.translate(1, 0);
        }
        if (key == KeyEvent.VK_DOWN) {
            pos.translate(0, 1);
        }
        if (key == KeyEvent.VK_LEFT) {
            pos.translate(-1, 0);
        }
    }
    
    public Point getPos() {
        return pos;
    }
}