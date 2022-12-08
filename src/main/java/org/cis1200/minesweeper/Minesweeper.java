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

    private int numMines;
    private boolean player1;
    private boolean gameOver;

    /**
     * Constructor sets up game state.
     */

    public Minesweeper() {
//        reset();
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
            } else if (board[row][col].equals(" ")) {
                board[row][col] = hiddenBoard[row][col];
                if (hiddenBoard[row][col].equals("0")) {
                    for (int r = Math.max(row - 1, 0); r <= Math.min(row + 1, 29); r++) {
                        for (int c = Math.max(col - 1, 0); c <= Math.min(col + 1, 15); c++) {
                            if (!hiddenBoard[r][c].equals("+")) {
                                if (hiddenBoard[r][c].equals("0")) {
                                    playTurn(c, r);
                                }
                                board[r][c] = hiddenBoard[r][c];
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
                } else {
                    board[row][col] = "F";
                }
                numMines--;
            } else if (board[row][col].equals("M") || board[row][col].equals("F")) {
                board[row][col] = " ";
                numMines++;
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
    public int checkWinner() {
        // Check horizontal win
//        for (int i = 0; i < board.length; i++) {
//            if (board[i][0] == board[i][1] &&
//                    board[i][1] == board[i][2] &&
//                    board[i][1] != 0) {
//                gameOver = true;
//                if (player1) {
//                    return 1;
//                } else {
//                    return 2;
//                }
//            }
//        }
//
//        if (numTurns >= 9) {
//            gameOver = true;
//            return 3;
//        } else {
//            return 0;
//        }
        return 0;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        for (int row = 0; row < 30; row++) {
            for (int col = 0; col < 16; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println("");
        }
        System.out.println("\n");
        for (int row = 0; row < 30; row++) {
            for (int col = 0; col < 16; col++) {
                System.out.print(hiddenBoard[row][col] + " ");
            }
            System.out.println("");
        }
    }


    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new String[30][16]; //create new board
        hiddenBoard = new String[30][16]; //create new board
        for (int row = 0; row < 30; row++) { //fill with covered cells
            for (int col = 0; col < 16; col++) {
                board[row][col] = " ";
                hiddenBoard[row][col] = "o";
            }
        }

        //randomly generate positions for numMines
        NumberGenerator ng = new RandomNumberGenerator();
        int i = 0;
        while (i <= numMines) {
            int x = ng.next(30);
            int y = ng.next(16);
            if (!hiddenBoard[x][y].equals("+")) {
                hiddenBoard[x][y] = "+";
                i++;
            }
        }

        for (int row = 0; row < 30; row++) { //fill with covered cells
            for (int col = 0; col < 16; col++) {
                if (hiddenBoard[row][col].equals("o")) {
                    int numMines = 0;
                    for (int r = Math.max(row - 1, 0); r <= Math.min(row + 1, 29); r++) {
                        for (int c = Math.max(col - 1, 0); c <= Math.min(col + 1, 15); c++) {
                            if (hiddenBoard[r][c].equals("+")) {
                                numMines++;
                            }
                        }
                    }
                    hiddenBoard[row][col] = Integer.toString(numMines);
                } else {
                    hiddenBoard[row][col] = "+";
                }
//                board[row][col] = "o";
            }
        }
        printGameState();

//        numTurns = 0;
//        player1 = true;
        gameOver = false;
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
        return numMines;
    }
    public void setNumMines(int num) {
        numMines = num;
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

    public String getHiddenCell(int c, int r) {
        return hiddenBoard[r][c];
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
        Minesweeper t = new Minesweeper();

        t.playTurn(1, 1);
        t.printGameState();

        t.playTurn(0, 0);
        t.printGameState();

        t.playTurn(0, 2);
        t.printGameState();

        t.playTurn(2, 0);
        t.printGameState();

        t.playTurn(1, 0);
        t.printGameState();

        t.playTurn(1, 2);
        t.printGameState();

        t.playTurn(0, 1);
        t.printGameState();

        t.playTurn(2, 2);
        t.printGameState();

        t.playTurn(2, 1);
        t.printGameState();
        System.out.println();
        System.out.println();
        System.out.println("Winner is: " + t.checkWinner());
    }
}
