package org.cis1200.minesweeper;

import java.util.Arrays;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

/**
 * This class is a model for TicTacToe.
 * 
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games. We
 * STRONGLY recommend you review these lecture slides, starting at
 * slide 8, for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 * 
 * Run this file to see the main method play a game of TicTacToe,
 * visualized with Strings printed to the console.
 */
public class Minesweeper {

    private String[][] board;

    private String[][] hiddenBoard;

    private static int boardWidth = 25;
    private static int boardHeight = 25;
    public static final int NUM_MINES_MAX = 99;
    private static int numMines;
    private static int numFlags = 0;
    private boolean gameOver;

    /**
     * Constructor sets up game state.
     */

    public Minesweeper(int width, int height, int numMines) {
        boardWidth = width;
        boardHeight = height;
        this.numMines = numMines;
        reset();
    }

    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed.
     *
     * @param col column to play in
     * @param row row to play in
     * @return whether the turn was successful
     */
    public boolean playTurn(int col, int row) {
        if (!gameOver) {
            if (hiddenBoard[row][col].equals("+")) {
                board[row][col] = "*";
                gameOver = true;
                return false;
            } else if (board[row][col].equals("M") || board[row][col].equals("F")) {
                return false;
            } else if (board[row][col].equals(" ")) {
                board[row][col] = hiddenBoard[row][col];
                if (hiddenBoard[row][col].equals("0")) {
                    for (int r = Math.max(row - 1, 0); r <= Math
                            .min(row + 1, hiddenBoard.length - 1); r++) {
                        for (int c = Math.max(col - 1, 0); c <= Math
                                .min(col + 1, hiddenBoard[0].length - 1); c++) {
                            if (!hiddenBoard[r][c].equals("+")) {
                                if (hiddenBoard[r][c].equals("0")) {
                                    playTurn(c, r);
                                }
                                if (!board[r][c].equals("M") && !board[r][c].equals("F")) {
                                    board[r][c] = hiddenBoard[r][c];
                                }
                            }
                        }
                    }
                }
            }
            printGameState();
            return true;
        }
        return false;
    }

    public boolean playFlag(int col, int row) {
        if (!gameOver) {
            if (board[row][col].equals(" ")) {
                if (hiddenBoard[row][col].equals("+")) {
                    board[row][col] = "M";
                    this.numMines--;
                } else {
                    board[row][col] = "F";
                }
                this.numFlags++;
            } else if (board[row][col].equals("M") || board[row][col].equals("F")) {
                board[row][col] = " ";
                this.numMines++;
                this.numFlags--;
            }
            return true;
        }
        return false;
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * checkWinner only looks for horizontal wins.
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2
     *         has won, 3 if the game hits stalemate
     */
    public boolean checkWinner() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].equals("F")) {
                    return false;
                }
                if (board[i][j].equals(" ") && !hiddenBoard[i][j].equals("+")) {
                    return false;
                }
            }
        }
        // Check horizontal win
        // for (int i = 0; i < board.length; i++) {
        // if (board[i][0] == board[i][1] &&
        // board[i][1] == board[i][2] &&
        // board[i][1] != 0) {
        // gameOver = true;
        // if (player1) {
        // return 1;
        // } else {
        // return 2;
        // }
        // }
        // }
        //
        // if (numTurns >= 9) {
        // gameOver = true;
        // return 3;
        // } else {
        // return 0;
        // }
        return true;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println("");
        }
        System.out.println("\n");
        for (int row = 0; row < hiddenBoard.length; row++) {
            for (int col = 0; col < hiddenBoard[0].length; col++) {
                System.out.print(hiddenBoard[row][col] + " ");
            }
            System.out.println("");
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new String[boardWidth][boardHeight]; // create new board
        hiddenBoard = new String[boardWidth][boardHeight]; // create new board
        for (int row = 0; row < boardWidth; row++) { // fill with covered cells
            for (int col = 0; col < boardHeight; col++) {
                board[row][col] = " ";
                hiddenBoard[row][col] = "o";
            }
        }

        // randomly generate positions for numMines
        NumberGenerator ng = new RandomNumberGenerator();
        int i = 0;
        while (i <= NUM_MINES_MAX) {
            int x = ng.next(boardWidth);
            int y = ng.next(boardHeight);
            if (!hiddenBoard[x][y].equals("+")) {
                hiddenBoard[x][y] = "+";
                i++;
            }
        }

        for (int row = 0; row < boardWidth; row++) { // fill with covered cells
            for (int col = 0; col < boardHeight; col++) {
                if (hiddenBoard[row][col].equals("o")) {
                    hiddenBoard[row][col] = Integer.toString(numberOfAdjacentMines(row, col));
                } else {
                    hiddenBoard[row][col] = "+";
                }
            }
        }
        setNumFlags(0);
        gameOver = false;
    }

    public int numberOfAdjacentMines(int row, int col) {
        int numMines = 0;
        for (int r = Math.max(row - 1, 0); r <= Math.min(row + 1, boardWidth - 1); r++) {
            for (int c = Math.max(col - 1, 0); c <= Math.min(col + 1, boardHeight - 1); c++) {
                if (hiddenBoard[r][c].equals("+") || hiddenBoard[r][c].equals("M")) {
                    numMines++;
                }
            }
        }
        return numMines;
    }

    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     * 
     * @return true if it's gameOver,
     *         false if it's not.
     */
    public boolean getGameOver() {
        return gameOver;
    }

    public int getNumMines() {
        return this.numMines;
    }

    public int getNumFlags() {
        return this.numFlags;
    }

    public void setNumFlags(int num) {
        this.numFlags = num;
    }

    public void setNumMines(int num) {
        this.numMines = num;
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, 1 = Player 1, 2 = Player 2
     */
    public String getCell(int c, int r) {
        return board[r][c];
    }

    public void setHiddenCell(int c, int r, String val) {
        hiddenBoard[r][c] = val;
    }

    public String getHiddenCell(int c, int r) {
        try {
            return hiddenBoard[r][c];
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public String[][] getBoard() {
        return Arrays.copyOf(board, board.length);
    }

    public String[][] getHiddenBoard() {
        return Arrays.copyOf(hiddenBoard, hiddenBoard.length);
    }

    public void setBoard(String[][] inputBoard) {
        this.board = inputBoard;
    }

    public void setHiddenBoard(String[][] inputHiddenBoard) {
        this.hiddenBoard = inputHiddenBoard;
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {

    }
}
