import java.util.Scanner;
import javax.swing.*;

class App {
    private static void initWindow() {
        try (Scanner sc = new Scanner(System.in)) {
            while (true){
                System.out.println("Enter the number of rows: (12 Recommended) ");

                GameConfig.ROWS = sc.nextInt();
                if (GameConfig.ROWS < 12 ){
                    System.out.println("Minimum number of rows should be 12");
                    continue;
                }
                System.out.println("Enter the number of columns: (18 Recommended) ");
                GameConfig.COLUMNS = sc.nextInt();
                if (GameConfig.COLUMNS < 18 ){
                    System.out.println("Minimum number of columns should be 12");
                    continue;
                }
                
                System.out.println("Enter the number of rabbits in the board: (5-10 Recommended) ");
                GameConfig.numberOfRabbits = sc.nextInt();
                if (GameConfig.numberOfRabbits < 5){
                    System.out.println("Minimum number of rabbits should be 5");
                    continue;
                }
                break;
            }
        }

        // create a window frame and set the title in the toolbar
        JFrame window = new JFrame("avva java project: Pakkodo Pakkodo");
        // when we close the window, stop the app
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create the jpanel to draw on.
        // this also initializes the game loop
        Board board = new Board();
        // add the jpanel to the window
        window.add(board);
        // pass keyboard inputs to the jpanel
        window.addKeyListener(board);
        
        // don't allow the user to resize the window
        window.setResizable(false);
        // fit the window size around the components (just our jpanel).
        // pack() should be called after setResizable() to avoid issues on some platforms
        window.pack();
        // open window in the center of the screen
        window.setLocationRelativeTo(null);
        // display the window
        window.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initWindow();
            }
        });
    }
}