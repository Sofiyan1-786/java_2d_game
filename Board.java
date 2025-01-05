import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.math.*;

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
    private static final int NUM_RABBITS = 5;
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
        // we need to cast the Graphics to Graphics2D to draw nicer text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(
            RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        // set the text color and font
        g2d.setColor(new Color(251, 251, 251));
        g2d.setFont(new Font("Algerian", Font.BOLD, 25));
        // draw the score in the bottom center of the screen
        // https://stackoverflow.com/a/27740330/4655368
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        // the text will be contained within this rectangle.
        // here I've sized it to be the entire bottom row of board tiles
        Rectangle rect = new Rectangle(-330, TILE_SIZE * (ROWS - 1), TILE_SIZE * COLUMNS, TILE_SIZE);
        // determine the x coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // determine the y coordinate for the text
        // (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // draw the string
        g2d.drawString(text, x, y);
        g2d.drawString(text1, x, y - 45);
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