package org.cis1200.minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private GameBoard testBoard;
    @BeforeEach
    public void setUp() {
        JLabel status = new JLabel("testNumberOfAdjacentMines");
        testBoard = new GameBoard(4, 4, 5, status);
        testBoard.setHiddenCell(0, 0, "0");
        testBoard.setHiddenCell(0, 1, "1");
        testBoard.setHiddenCell(0, 2, "2");
        testBoard.setHiddenCell(0, 3, "2");
        testBoard.setHiddenCell(1, 0, "2");
        testBoard.setHiddenCell(1, 1, "3");
        testBoard.setHiddenCell(1, 2, "+");
        testBoard.setHiddenCell(1, 3, "+");
        testBoard.setHiddenCell(2, 0, "+");
        testBoard.setHiddenCell(2, 1, "+");
        testBoard.setHiddenCell(2, 2, "4");
        testBoard.setHiddenCell(2, 3, "3");
        testBoard.setHiddenCell(3, 0, "2");
        testBoard.setHiddenCell(3, 1, "3");
        testBoard.setHiddenCell(3, 2, "+");
        testBoard.setHiddenCell(3, 3, "1");
        // 0 1 2 2
        // 2 3 + +
        // + + 4 3
        // 2 3 + 1
    }
    @Test
    public void testGetCellMine() {
        JLabel status = new JLabel("testGetCellMine");
        GameBoard board = new GameBoard(4, 4, 2, status);
        board.setHiddenCell(1, 1, "M");
        assertEquals("M", "M", board.getCell(1, 1));
    }

    @Test
    public void testNegativeWidth() {
        JLabel status = new JLabel("testNegativeWidth");
        assertThrows(IllegalArgumentException.class, () -> new GameBoard(-4, 4, 2, status));
    }

    @Test
    public void testNegativeHeight() {
        JLabel status = new JLabel("testNegativeHeight");
        assertThrows(IllegalArgumentException.class, () -> new GameBoard(4, -4, 2, status));
    }

    @Test
    public void testGetCellInvalid() {
        JLabel status = new JLabel("testGetCellInvalid");
        GameBoard board = new GameBoard(4, 4, 2, status);
        assertThrows(IllegalArgumentException.class, () -> board.getCell(0, -1));
    }

    @Test
    public void testNumberOfAdjacentMines() {
        JLabel status = new JLabel("testNumberOfAdjacentMines");
        GameBoard board = new GameBoard(4, 4, 2, status);
        board.setHiddenCell(0, 0, "o");
        board.setHiddenCell(0, 1, "M");
        board.setHiddenCell(0, 2, "o");
        board.setHiddenCell(0, 3, "o");
        board.setHiddenCell(1, 0, "o");
        board.setHiddenCell(1, 1, "+");
        board.setHiddenCell(1, 2, "o");
        assertEquals(2, board.numberOfAdjacentMines(0, 0));
    }

    @Test
    public void testGameOver() {
        JLabel status = new JLabel("testGameOver");
        GameBoard board = new GameBoard(2, 2, 2, status);
        board.setHiddenCell(0, 0, "+");
        board.playTurn(0, 0);
        assertEquals("*", board.getCell(0, 0));
        assertTrue(board.getGameOver());
    }

    @Test
    public void testPlayTurn() {
        assertEquals(" ", testBoard.getCell(1, 1));
        testBoard.playTurn(0, 0);
        assertEquals("0", testBoard.getCell(0, 0));
        assertEquals("1", testBoard.getCell(0, 1));
        assertEquals("2", testBoard.getCell(1, 0));
        assertEquals("3", testBoard.getCell(1, 1));
        assertEquals(" ", testBoard.getCell(0, 2));
    }
    @Test
    public void testPlayFlags() {
        JLabel status = new JLabel("testPlayFlags");
        GameBoard board = new GameBoard(2, 2, 2, status);
        board.setHiddenCell(0, 0, "+");
        board.setHiddenCell(0, 1, "1");
        board.setHiddenCell(1, 0, "1");
        board.setHiddenCell(1, 1, "1");
        board.playFlag(0, 0);
        assertEquals("M", board.getCell(0, 0));
        board.playTurn(0, 0);
        assertEquals("M", board.getCell(0, 0));
        board.playFlag(0, 0);
        assertEquals(" ", board.getCell(0, 0));
        board.playFlag(0, 1);
        assertEquals("F", board.getCell(0, 1));
    }

    @Test
    public void testCheckWinner() {
        testBoard.playTurn(0, 0);
        testBoard.playTurn(0, 2);
        testBoard.playTurn(0, 3);
        testBoard.playTurn(2, 2);
        testBoard.playTurn(2, 3);
        testBoard.playTurn(3, 0);
        testBoard.playTurn(3, 1);
        testBoard.playTurn(3, 3);
        assertTrue(testBoard.checkWinner());
    }

    @Test
    public void testSaveAndResume() throws IOException {
        testBoard.playTurn(0, 0);
        testBoard.playFlag(1, 2);
        testBoard.save();
        BufferedReader br = FileLineIterator.fileToReader("files/savedGame.txt");
        FileLineIterator li = new FileLineIterator(br);
        String[][] board = new String[4][4];
        String[][] hiddenBoard = new String[4][4];
        for (int i = 0; i < board.length; i++) {
            board[i] = li.next().split(",");
        }
        li.next();
        for (int i = 0; i < board.length; i++) {
            hiddenBoard[i] = li.next().split(",");
        }
        br.close();
        assertArrayEquals(testBoard.getBoard(), board);
        assertArrayEquals(testBoard.getHiddenBoard(), hiddenBoard);
    }

    @Test
    public void testPlayFlagOnUncoveredCell() {
        testBoard.playTurn(0, 0);
        assertEquals("1", testBoard.getCell(0, 1));
        testBoard.playFlag(0, 1);
        assertEquals("1", testBoard.getCell(0, 1));
    }
    @Test
    public void testDisabledFlags() {
        testBoard.playFlag(0, 0);
        testBoard.playFlag(0, 1);
        testBoard.playFlag(0, 2);
        testBoard.playFlag(0, 3);
        testBoard.playFlag(1, 0);
        assertEquals("F", testBoard.getCell(1, 0));
        testBoard.playFlag(1, 1);
        assertEquals(" ", testBoard.getCell(1, 1));
        testBoard.playFlag(1, 0);
        testBoard.playFlag(1, 2);
        assertEquals(" ", testBoard.getCell(1, 0));
        assertEquals("M", testBoard.getCell(1, 2));
        assertFalse(testBoard.playFlag(1, 3));
    }

    @Test
    public void testRecursionNoMines() {
        JLabel status = new JLabel("testPlayFlags");
        GameBoard board = new GameBoard(3, 3, 0, status);
        board.setHiddenCell(0, 0, "0");
        board.setHiddenCell(0, 1, "0");
        board.setHiddenCell(0, 2, "0");
        board.setHiddenCell(1, 0, "0");
        board.setHiddenCell(1, 1, "0");
        board.setHiddenCell(1, 2, "0");
        board.setHiddenCell(2, 0, "0");
        board.setHiddenCell(2, 1, "0");
        board.setHiddenCell(2, 2, "0");
        assertEquals(" ", board.getCell(0, 0));
        board.playTurn(0, 0);
        assertEquals("0", board.getCell(0, 0));
        assertEquals("0", board.getCell(1, 2));
        assertEquals("0", board.getCell(2, 2));

    }

}
