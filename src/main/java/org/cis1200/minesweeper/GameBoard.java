package org.cis1200.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Paths;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {
    private Minesweeper ms; // model for the game
    private JLabel status; // current status text
    private static int boardWidth;
    private static int boardHeight;
    private static int numMines;
    public static final int CELL_WIDTH = 20;
    public static final int CELL_HEIGHT = 20;

    /**
     * Initializes the game board.
     */
    public GameBoard(int height, int width, int numMines, JLabel statusInit) {
        if (width <= 0 || height <= 0 || numMines < 0) {
            throw new IllegalArgumentException();
        }
        boardHeight = height;
        boardWidth = width;
        GameBoard.numMines = numMines;
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

    /**
     * save allows the user to save the state of the game and runs when the
     * user clicks the 'Save Game' button. It writes to a text file the board
     * and hiddenBoard arrays as well as the number of flags and mines.
     */
    public void save() throws IOException {
        File file = Paths.get("files/savedGame.txt").toFile();
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            String[][] board = ms.getBoard();
            String[][] hiddenBoard = ms.getHiddenBoard();
            for (String[] strings : board) {
                for (int j = 0; j < board[0].length; j++) {
                    bw.write(strings[j] + ",");
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
            bw.newLine();
            bw.write(String.valueOf(ms.getGameOver()));
            bw.close();
        } catch (IOException e) {
            throw new IOException();
        }
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * resume allows the user to resume a past state of the game and runs when
     * the user clicks the 'Resume Game' button. It reads from a text file the
     * saved board and hiddenBoard arrays as well as the number of flags and
     * mines. It uses the data to set the board and hiddenBoard variables and
     * repaint the board.
     */
    public void resume() {
        try {
            BufferedReader br = FileLineIterator.fileToReader("files/savedGame.txt");
            FileLineIterator li = new FileLineIterator(br);
            String[][] board = new String[boardWidth][boardHeight];
            String[][] hiddenBoard = new String[boardWidth][boardHeight];
            for (int i = 0; i < board.length; i++) {
                board[i] = li.next().split(",");
            }
            li.next();
            for (int i = 0; i < board.length; i++) {
                hiddenBoard[i] = li.next().split(",");
            }
            ms.setNumMines(Integer.parseInt(li.next()));
            ms.setNumFlags(Integer.parseInt(li.next()));
            ms.setGameOver(Boolean.parseBoolean(li.next()));
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
        if (ms.checkWinner()) {
            status.setText("You Won!");
        } else if (ms.getGameOver()) {
            status.setText("Game Over!");
        } else {
            status.setText("Number of Mines Left: " + (numMines - ms.getNumFlags()));
        }
    }

    /**
     * Draws the game board.
     */
    @Override
    public void paintComponent(Graphics g) {
        ms.printGameState();
        super.paintComponent(g);
        // Draws board grid
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                g.setColor(Color.BLACK);
                int x = j * CELL_WIDTH;
                int y = i * CELL_HEIGHT;
                g.drawLine(x, 0, x, boardWidth * CELL_HEIGHT);
                g.drawLine(0, y, boardHeight * CELL_WIDTH, y);
                String cell = ms.getCell(j, i);
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
                } else if ((ms.getHiddenCell(j, i).equals("+") && ms.getGameOver() ||
                        ms.getHiddenCell(j, i).equals("+") && ms.checkWinner())) {
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
                    if (cell.equals("0")) {
                        g.drawString(" ", (j * CELL_WIDTH) + 6, (i * CELL_HEIGHT) + 14);
                    } else {
                        g.drawString(cell, (j * CELL_WIDTH) + 6, (i * CELL_HEIGHT) + 14);
                    }
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

    public boolean playFlag(int c, int r) {
        return ms.playFlag(r, c);
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

    public boolean checkWinner() {
        return ms.checkWinner();
    }

    public boolean getGameOver() {
        return ms.getGameOver();
    }

    public String[][] getBoard() {
        return ms.getBoard();
    }

    public String[][] getHiddenBoard() {
        return ms.getHiddenBoard();
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(boardHeight * CELL_HEIGHT, boardWidth * CELL_WIDTH + 94);
    }
}
