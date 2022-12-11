package org.cis1200.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class RunMinesweeper implements Runnable {
    public void run() {

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(100, 100);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(5, 5, 5, status);
        frame.add(board, BorderLayout.CENTER);
        frame.setPreferredSize(board.getPreferredSize());

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Instructions button
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(
            e -> JOptionPane.showMessageDialog(frame,
            "Minesweeper is a game where mines are hidden in a grid of squares. \n" +
                    "Safe squares have numbers telling you how many mines are in adjacent \n" +
                    "squares. You can use the number clues to solve the game by opening all \n" +
                    "of the safe squares. If you click on a mine you lose the game and all \n" +
                    "the mines will be revealed!\n" + "\n" +
                    "To open squares, click with the left mouse button and to place down \n" +
                    "flags on mines, click with the right mouse button. Pressing the right \n" +
                    "mouse button again removes the flag. When you open a square that does \n" +
                    "not touch any mines, it will be empty and the adjacent squares will \n" +
                    "automatically open in all directions until reaching squares that \n" +
                    "contain numbers. A common strategy for starting games is to randomly \n" +
                    "click until you get a big opening with lots of numbers.\n" + "\n" +
                    "The status bar shows how many mines remain based on the number of \n" +
                    "flags placed. If a square is flagged, but there is no mine, the status \n" +
                    "will still update. You cannot place more flags down than there are \n" +
                    "mines on the board - the flag function will automatically disable and \n" +
                    "you will not be able to place down another flag until you remove an \n" +
                    "existing flag. Once you uncover all the squares with no mines, you win " +
                    "the game! \n" + "\n" +
                    "To save your game progress, you can click 'Save Game' in the control \n" +
                    "panel - your game is not automatically saved. To resume the last saved \n" +
                    "game/game state, click on 'Resume Game' in the control panel.",
                    "Instructions for Minesweeper",
                    JOptionPane.INFORMATION_MESSAGE
                )
        );
        control_panel.add(instructions);

        // Reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        // Save Game button
        final JButton save = new JButton("Save Game");
        save.addActionListener(e -> {
            try {
                board.save();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        control_panel.add(save);

        // Resume button
        final JButton resume = new JButton("Resume Game");
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