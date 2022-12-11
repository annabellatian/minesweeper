package org.cis1200.minesweeper;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    @Test
    public void testGetCellMine() {
        // create a 4 by 4 MineSweeperBoard with 2 mines
        JLabel status = new JLabel("testGetCellMine");
        GameBoard board = new GameBoard(4, 4, 2, status);
        // put a mine at (1, 1)
        board.setHiddenCell(1, 1, "M");
        // assert that a Mine was returned
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

//    @Test
//    public void testPlayFlags() {
//        JLabel status = new JLabel("testPlayFlags");
//        GameBoard board = new GameBoard(2, 2, 2, status);
//        board.setHiddenCell(0, 0, "+");
//        board.setHiddenCell(0, 1, "1");
//        board.setHiddenCell(1, 0, "1");
//        board.setHiddenCell(1, 1, "1");
////        board.playFlag(0, 0);
//        assertEquals("M", board.getCell(0, 0));
//        board.playTurn(0, 0);
//        assertEquals("M", board.getCell(0, 0));
////        board.playFlag(0, 0);
//        assertEquals(" ", board.getCell(0, 0));
////        board.playFlag(0, 1);
//        assertEquals("F", board.getCell(0, 1));
//    }
}
