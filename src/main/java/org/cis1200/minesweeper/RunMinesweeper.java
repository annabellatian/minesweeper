package org.cis1200.minesweeper;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunMinesweeper implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(100, 100);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(25, 25, 99, status);
        frame.add(board, BorderLayout.CENTER);
        frame.setPreferredSize(board.getPreferredSize());

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(
            e -> JOptionPane.showMessageDialog(frame,
            "Minesweeper is a game where mines are hidden in a grid of squares. " +
                    " Safe squares have \nnumbers telling you how many mines touch the square. " +
                    "You can use the number clues to \n solve the game by opening all " +
                    "of the safe squares. If you click on a mine you lose \n" +
                    "the game!\n" + "\n" + "You open squares with the left mouse button " +
                    "and put flags on mines with the right mouse \n" +
                    "button. Pressing the right mouse button again removes the flag. " +
                    "When you open a square \n that does not touch any mines, it will " +
                    "be empty and the adjacent squares will \n" +
                    "automatically open in all directions until reaching squares that c" +
                    "ontain numbers. A \ncommon strategy for starting games is to randomly " +
                    "click until you get a big opening \nwith lots of numbers.\n" + "\n" +
                    "The status bar shows how many mines remain based on the number " +
                    "of flags placed. If a \nsquare is flagged, but there is no mine, " +
                    "the status will still update. You cannot place \n" +
                    "more flags down than there are mines on the board - the flag " +
                    "function will automatically \ndisable. Once you uncover all the " +
                    "squares with no mines, you win the game! \n",
                    "Instructions for Minesweeper",
                    JOptionPane.INFORMATION_MESSAGE
                )
        );
        control_panel.add(instructions);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        final JButton save = new JButton("Save Game");
        save.addActionListener(e -> board.save());
        control_panel.add(save);

        final JButton resume = new JButton("Resume Prev Game");
        resume.addActionListener(e -> board.resume());
        control_panel.add(resume);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}