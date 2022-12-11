package org.cis1200.minesweeper;

import java.util.Arrays;

public class Minesweeper {
    private String[][] board;
    private String[][] hiddenBoard;
    private static int boardWidth = 25;
    private static int boardHeight = 25;
    public static int numMinesMax = 99;
    private static int numMines;
    private static int numFlags = 0;
    private boolean gameOver;

    /**
     * Constructor sets up game state.
     */

    public Minesweeper(int width, int height, int numMines) {
        boardWidth = width;
        boardHeight = height;
        Minesweeper.numMinesMax = numMines;
        Minesweeper.numMines = numMines;
        reset();
    }

    /**
     * playTurn allows the user to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or results in game over. If the turn is successful and the game
     * has not ended, the cell is updated.
     *
     * @param col column to play in
     * @param row row to play in
     * @return whether the turn was successful
     */
    public boolean playTurn(int col, int row) {
        if (!gameOver) {
            if (board[row][col].equals("M") || board[row][col].equals("F")) {
                return false;
            } else if (hiddenBoard[row][col].equals("+")) {
                board[row][col] = "*";
                gameOver = true;
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

    /**
     * playFlag allows the user to play a turn. Returns true if the move is
     * successful and false if a user tries to play in a location that a
     * flag cannot be placed or if the game is over. If the turn is successful
     * and the game has not ended, the flag is placed and cell is updated.
     *
     * @param col column to play in
     * @param row row to play in
     * @return whether the flag was successful
     */
    public boolean playFlag(int col, int row) {
        if (!gameOver) {
            if (board[row][col].equals(" ")) {
                if (numFlags == numMinesMax) {
                    return false;
                } else if (hiddenBoard[row][col].equals("+")) {
                    board[row][col] = "M";
                    numMines--;
                } else {
                    board[row][col] = "F";
                }
                numFlags++;
            } else if (board[row][col].equals("M") || board[row][col].equals("F")) {
                board[row][col] = " ";
                numMines++;
                numFlags--;
            }
            return true;
        }
        return false;
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     *
     * @return whether the game is won or not
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
        return true;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        for (String[] strings : board) {
            for (int col = 0; col < board[0].length; col++) {
                System.out.print(strings[col] + " ");
            }
            System.out.println("");
        }
        System.out.println("\n");
        for (String[] strings : hiddenBoard) {
            for (int col = 0; col < hiddenBoard[0].length; col++) {
                System.out.print(strings[col] + " ");
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
        while (i <= numMinesMax) {
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

    /**
     * numberOfAdjacentMines calculates how many mines border a given cell.
     *
     * @param col column of cell
     * @param row row of cell
     * @return how many adjacent mines there are
     */
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

    public boolean getGameOver() {
        return gameOver;
    }

    public int getNumMines() {
        return numMines;
    }

    public int getNumFlags() {
        return numFlags;
    }

    public void setNumFlags(int num) {
        numFlags = num;
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
     * @return the contents of the cell at the given row and column
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

    public static void main(String[] args) {

    }
}
