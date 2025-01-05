import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Board extends JPanel implements ActionListener, KeyListener  {

    //delay ke liye 
    private final int DELAY = 25;

    // controls the size of the board
    
    // // controls how many coins appear on the board
    // public static final int NUM_COINS = 5;

    // suppress serialization warning
    private static final long serialVersionUID = 490905409104883233L;
    
    // keep a reference to the timer object that triggers actionPerformed() in
    // case we need access to it in another method
    private Timer timer;

    // objects that appear on the game board
    private Player player;
    private Dog dog;
    private ArrayList<Carrot> coins;
    static Integer myInf = Integer.MAX_VALUE;
    private static final int NUM_RABBITS = GameConfig.numberOfRabbits;
    private ArrayList<Rabbit> rabbits;
    private int rabbitsEliminated = 0;
    private int rabbitScore = 0;
    
    public Board() {
        // set the game board size
        int ROWS = GameConfig.ROWS;
        int COLUMNS = GameConfig.COLUMNS;
        int TILE_SIZE = GameConfig.TILE_SIZE;
        setPreferredSize(new Dimension(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS));
        // set the game board background color
        setBackground(new Color(21,144, 70)); //yeh main background hai

        // initialize the game state
        player = new Player();
        dog = new Dog();
        coins = populateCoins();

        //initialize the rabbits
        rabbits = new ArrayList<>();
        for (int i = 0; i < NUM_RABBITS; i++) {
            rabbits.add(new Rabbit());
        }

        // this timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this method is called by the timer every DELAY ms.
        // use this space to update the state of your game or animation
        // before the graphics are redrawn.

        // prevent the player from disappearing off the board
        player.tick();
        dog.tick();

        // give the player points for collecting coins
        collectCoins(); //yeh rabbit ko diyo taki CARROTS DISSAPPEAR!
        rabbitsCollectCoins();

        // calling repaint() will trigger paintComponent() to run again,
        // which will refresh/redraw the graphics.
        checkDogRabbitCollisions();
        repaint();
    }

    //This is for collision detections
    private void checkDogRabbitCollisions() {
        Point dogPos = dog.getPos();
        
        for (Rabbit rabbit : rabbits) {
            if (rabbit.isAlive() && rabbit.checkCollision(dogPos)) {
                rabbit.eliminate();
                rabbitsEliminated++;
                // Optional: Add sound effect or visual effect here
                System.out.println("Rabbit eliminated! Total: " + rabbitsEliminated);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // when calling g.drawImage() we can use "this" for the ImageObserver 
        // because Component implements the ImageObserver interface, and JPanel 
        // extends from Component. So "this" Board instance, as a Component, can 
        // react to imageUpdate() events triggered by g.drawImage()

        // draw our graphics.
        drawBackground(g);
        // drawScore(g);
        for (Carrot coin : coins) {
            coin.draw(g, this);
        }
        player.draw(g, this);
        dog.draw(g, this);

        for (Rabbit rabbit : rabbits) {
            rabbit.draw(g, this);
        }
        drawScore(g);

        // rabbit.draw(g, this);//making error here
        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // this is not used but must be defined as part of the KeyListener interface
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // react to key down events
        player.keyPressed(e);
        dog.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // react to key up events
    }




private void drawBackground(Graphics g) {
    // Draw a checkered background
    int ROWS = GameConfig.ROWS;
    int COLUMNS = GameConfig.COLUMNS;
    int TILE_SIZE = GameConfig.TILE_SIZE;
    g.setColor(new Color(5, 203, 101));
    for (int row = 0; row < ROWS; row++) {
        for (int col = 0; col < COLUMNS; col += 2) { // Increment by 2 for alternate columns
            // Draw a square tile at the current row/column position
            g.fillRect(
                col * TILE_SIZE,
                row * TILE_SIZE,
                TILE_SIZE,
                TILE_SIZE
            );
        }
    }
}

private void drawScore(Graphics g) {
    // set the text to be displayed
    int ROWS = GameConfig.ROWS;
    int COLUMNS = GameConfig.COLUMNS;

    int TILE_SIZE = GameConfig.TILE_SIZE;

    String text = "CARROTS TAKEN:" + player.getScore();
    String text1 = "CARROTS TAKEN by Rabbits:" + rabbitScore;

    String text2 = "";
    if (Integer.parseInt(player.getScore()) > rabbitScore) {
        text2 = "Player is winning";
        System.out.println("Player is winning");
    } else if (Integer.parseInt(player.getScore()) < rabbitScore) {
        text2 = "Rabbits are winning";
        System.out.println("Rabbits are winning");
    } else {
        text2 = "It's a tie!";
        System.out.println("It's a tie!");
    }

    // Cast the Graphics to Graphics2D
    Graphics2D g2d = (Graphics2D) g;
    Graphics2D g2d1 = (Graphics2D) g;

    // Rendering hints for smooth text
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

    g2d1.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2d1.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d1.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

    // Set colors and fonts for g2d and g2d1

    g2d.setColor(new Color(195, 14, 89)); // Red color for text1 and text
    g2d.setFont(new Font("Algerian", Font.BOLD, 25));

    g2d1.setColor(new Color(255, 250, 213)); // Green color for text2
    g2d1.setFont(new Font("Algerian", Font.ITALIC, 25));

    // Font metrics for alignment
    FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
    FontMetrics metrics1 = g2d1.getFontMetrics(g2d1.getFont());

    // Bottom-left positioning for text and text1
    int textX = 10; // A small padding from the left edge
    int textY = TILE_SIZE * ROWS - 10; // Near the bottom row, with a small margin

    g2d.drawString(text, textX, textY); // Draw "CARROTS TAKEN:"
    g2d.drawString(text1, textX, textY - 45); // Draw "CARROTS TAKEN by Rabbits:"

    // Calculate the center of the board for text2
    int centerX = (COLUMNS * TILE_SIZE) / 2;
    int centerY = (ROWS * TILE_SIZE) / 2;

    // Get the width of the text
    int textWidth = metrics1.stringWidth(text2);

    // Center align text2
    int text2X = centerX - (textWidth / 2);
    int text2Y = centerY + (metrics1.getAscent() / 2);

    // Draw text2 in the center
    g2d1.drawString(text2, text2X, text2Y);
}

        
        private ArrayList<Carrot> populateCoins() {
        ArrayList<Carrot> coinList = new ArrayList<>();

        // create coins on every square of the board
        int ROWS = GameConfig.ROWS;
        int COLUMNS = GameConfig.COLUMNS;
        int TILE_SIZE = GameConfig.TILE_SIZE;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
            coinList.add(new Carrot(col, row));
            }
        }
        return coinList;
        }

        private void collectCoins() {
        // allow player to pickup coins
        ArrayList<Carrot> collectedCoins = new ArrayList<>();
        for (Carrot coin : coins) {
            // if the player is on the same tile as a coin, collect it
            if (player.getPos().equals(coin.getPos())) {
                // give the player some points for picking this up
                player.addScore(1);
                collectedCoins.add(coin);
            }
        }
        // remove collected coins from the board
        coins.removeAll(collectedCoins);
        }
        private void rabbitsCollectCoins() {
            ArrayList<Carrot> collectedCoins = new ArrayList<>();
            for (Rabbit rabbit : rabbits) {
                if (rabbit.isAlive()) {
                    for (Carrot coin : coins) {
                        if (rabbit.getPos().equals(coin.getPos())) {
                            collectedCoins.add(coin);
                            rabbitScore++;  // Add the coin to our collection
                        }
                    }
                }
            }
            // Remove all collected coins after the loops
            coins.removeAll(collectedCoins);
        }

}