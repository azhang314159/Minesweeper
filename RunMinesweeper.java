import javax.swing.*;
import java.awt.*;

public class RunMinesweeper implements Runnable {
    public void run() {
        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        final JButton flag = new JButton("Flag");
        flag.addActionListener(e -> board.flag());
        control_panel.add(flag);

        final JButton save = new JButton("Save");
        save.addActionListener(e -> board.save());
        control_panel.add(save);

        final JButton load = new JButton("Load");
        load.addActionListener(e -> board.load());
        control_panel.add(load);

        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(e -> JOptionPane.showMessageDialog(null,
                "<html><body><p style='width: 200px;'>" +
                        "Welcome to Minesweeper! There are 10 mines hidden in this 9 x 9 grid, " +
                        "and your task is to locate these mines by clicking all 71 non-mine " +
                        "cells. Clicking a non-mine cells reveals how many mines are in the 8 " +
                        "cells around it, while clicking a mine cell instantly loses. You may " +
                        "flag cells that you think contain mines by clicking the Flag button. " +
                        "Clicking this button again will return normal clicking mode. If you " +
                        "wish to save the game to a file, press the Save button; this also saves " +
                        "your flags. To load a game from a file, press the Load button. If you " +
                        "wish to restart, press the Restart button." +
                        "</p></body></html>"
                ));
        control_panel.add(instructions);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}