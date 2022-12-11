package org.cis1200.minesweeper;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Paths;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Minesweeper ms; // model for the game
    private JLabel status; // current status text

    // Game constants
    private static int boardWidth = 25;
    private static int boardHeight = 25;
    private static int numMines;

    public static final int CELL_WIDTH = 20;

    public static final int CELL_HEIGHT = 20;

    /**
     * Initializes the game board.
     */
    public GameBoard(int width, int height, int numMines, JLabel statusInit) {
        if (width <= 0 || height <= 0 || numMines < 0) {
            throw new IllegalArgumentException();
        }
        boardHeight = height;
        boardWidth = width;
        this.numMines = numMines;
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        ms = new Minesweeper(width, height, numMines); // initializes model for the game
        status = statusInit; // initializes the status JLabel
        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                if (SwingUtilities.isRightMouseButton(e)) {
                    ms.playFlag(p.x / CELL_WIDTH, p.y / CELL_HEIGHT);
                } else {
                    ms.playTurn(p.x / CELL_WIDTH, p.y / CELL_HEIGHT);
                }

                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        ms.setNumMines(numMines);
        ms.reset();
        status.setText("Start playing!");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void save() {
        File file = Paths.get("files/savedGame.txt").toFile();
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            String[][] board = ms.getBoard();
            String[][] hiddenBoard = ms.getHiddenBoard();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    bw.write(board[i][j] + ",");
                }
                bw.write("\n");
            }
            bw.newLine();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    bw.write(hiddenBoard[i][j] + ",");
                }
                bw.write("\n");
            }
            bw.write(String.valueOf(ms.getNumMines()));
            bw.newLine();
            bw.write(String.valueOf(ms.getNumFlags()));
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void resume() {
        try {
            System.out.println("RESUME SECTION");
            BufferedReader br = FileLineIterator.fileToReader("files/savedGame.txt");
            FileLineIterator li = new FileLineIterator(br);
            String[][] board = new String[25][25];
            String[][] hiddenBoard = new String[25][25];
            for (int i = 0; i < board.length; i++) {
                board[i] = li.next().split(",");
                for (String s : board[i]) {
                    System.out.print(s + " ");
                }
                System.out.println();
            }
            li.next();
            System.out.println("HIDDENBOARD");
            for (int i = 0; i < board.length; i++) {
                hiddenBoard[i] = li.next().split(",");
                for (String s : hiddenBoard[i]) {
                    System.out.print(s + " ");
                }
                System.out.println();
            }
            ms.setNumMines(Integer.parseInt(li.next()));
            ms.setNumFlags(Integer.parseInt(li.next()));
            br.close();
            ms.setHiddenBoard(hiddenBoard);
            ms.setBoard(board);
            repaint();
            updateStatus();
        } catch (IOException e) {
            throw new RuntimeException();
        }
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (ms.getGameOver()) {
            status.setText("Game Over!");
        } else if (ms.checkWinner()) {
            status.setText("You Won!");
        } else {
            status.setText("Number of Mines Left: " + (numMines - ms.getNumFlags()));
        }
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draws board grid
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                g.setColor(Color.BLACK);
                int x = i * CELL_WIDTH;
                int y = j * CELL_HEIGHT;
                g.drawLine(x, 0, x, boardHeight * CELL_HEIGHT);
                g.drawLine(0, y, boardWidth * CELL_WIDTH, y);
                String cell = ms.getCell(i, j);
                if (!cell.equals(" ")) {
                    g.setColor(Color.decode("#a9a9a9"));
                    g.fillPolygon(
                            new int[] { x, (x + CELL_WIDTH), (x + CELL_WIDTH), x },
                            new int[] { y, y, (y + CELL_HEIGHT), (y + CELL_HEIGHT) }, 4
                    );
                }
                if (cell.equals("M") || cell.equals("F")) {
                    g.setColor(Color.RED);
                    g.fillPolygon(
                            new int[] { x + 10, x + 5, x + 15 },
                            new int[] { y + 6, y + 14, y + 14 }, 3
                    );
                } else if (cell.equals("*")) {
                    g.setColor(Color.BLACK);
                    g.fillOval(x + (CELL_WIDTH / 4) + 1, y + (CELL_HEIGHT / 4), 10, 10);
                } else if ((ms.getHiddenCell(i, j).equals("+") && ms.getGameOver() ||
                        ms.getHiddenCell(i, j).equals("+") && ms.checkWinner())) {
                    g.setColor(Color.BLACK);
                    g.fillOval(x + (CELL_WIDTH / 4), y + (CELL_HEIGHT / 4), 10, 10);
                } else {
                    switch (cell) {
                        case "1" -> g.setColor(Color.BLUE);
                        case "2" -> g.setColor(Color.decode("#008000"));
                        case "3" -> g.setColor(Color.RED);
                        case "4" -> g.setColor(Color.decode("#000080"));
                        case "5" -> g.setColor(Color.decode("#800000"));
                        case "6" -> g.setColor(Color.decode("#30D5C8"));
                        case "7" -> g.setColor(Color.BLACK);
                        default -> g.setColor(Color.GRAY);
                    }
                    g.drawString(cell, (i * CELL_WIDTH) + 6, (j * CELL_HEIGHT) + 14);
                }
            }
        }
    }

    public void setHiddenCell(int c, int r, String val) {
        try {
            ms.setHiddenCell(r, c, val);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public void playTurn(int c, int r) {
        ms.playTurn(r, c);
    }

    public void playFlag(int c, int r) {
        ms.playFlag(r, c);
    }

    public String getCell(int c, int r) {
        try {
            return ms.getCell(r, c);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public int numberOfAdjacentMines(int r, int c) {
        return ms.numberOfAdjacentMines(r, c);
    }

    public boolean getGameOver() {
        return ms.getGameOver();
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(boardWidth * CELL_WIDTH, boardHeight * CELL_HEIGHT + 94);
    }
}
