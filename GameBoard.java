import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;

public class GameBoard extends JPanel {

    private Minesweeper ms; // model for the game
    private JLabel status; // current status text
    private boolean flagMode;

    // Game constants
    public static final int BOARD_WIDTH = 630;
    public static final int BOARD_HEIGHT = 630;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);

        ms = new Minesweeper();
        status = statusInit;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                if (!flagMode) {
                    ms.playTurn(p.x / 70, p.y / 70);
                } else {
                    ms.flag(p.x / 70, p.y / 70);
                }

                updateStatus();
                repaint();
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        ms.reset();
        flagMode = false;
        status.setText("Click a cell to begin");
        repaint();
        requestFocusInWindow();
    }

    public void flag() {
        flagMode = !flagMode;
        if (flagMode) {
            status.setText("Click a cell to flag it");
        } else {
            status.setText("Click a cell to reveal it");
        }
    }

    public void save() {
        try {
            PrintWriter pw = new PrintWriter("game.txt");
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    pw.write(ms.getCell(j, i) + " ");
                }
                pw.write("\n");
            }

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    pw.write(ms.isFlagged(j, i) + " ");
                }
                pw.write("\n");
            }
            pw.close();
            status.setText("Successfully saved game");
        } catch (IOException e) {
            status.setText("Failed saving game");
        }
    }

    public void load() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("game.txt"));
            int[][] arr = new int[9][9];
            for (int i = 0; i < 9; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int j = 0; j < 9; j++) {
                    arr[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            boolean[][] f = new boolean[9][9];
            for (int i = 0; i < 9; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int j = 0; j < 9; j++) {
                    f[i][j] = Boolean.parseBoolean(st.nextToken());
                }
            }

            ms = new Minesweeper(arr, f);
            repaint();
            status.setText("Successfully loaded game");
        } catch (IOException e) {
            status.setText("Error loading game");
        } catch (Exception e) {
            status.setText("Invalid gamestate");
        }
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (ms.isGameOver()) {
            if (ms.isWin()) {
                status.setText("You win!");
            } else {
                status.setText("You lose!");
            }
        } else {
            if (flagMode) {
                status.setText("Click a cell to flag it");
            } else {
                status.setText("Click a cell to reveal it");
            }
        }
    }

    /**
     * Draws the game board.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        int unitWidth = BOARD_WIDTH / 9;
        int unitHeight = BOARD_HEIGHT / 9;

        for (int i = 1; i <= 8; i++) {
            g.drawLine(0, unitHeight * i, BOARD_WIDTH, unitHeight * i);
            g.drawLine(unitWidth * i, 0, unitWidth * i, BOARD_HEIGHT);
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int state = ms.getCell(j, i);
                if (state == 999) {
                    g.setColor(Color.BLUE);
                    g.drawString("M", 30 + 70 * j, 40 + 70 * i);
                } else if (state > 0) {
                    int n = state - 1;
                    switch (n) {
                        case 0:
                            g.setColor(Color.BLACK);
                            break;
                        case 1:
                            g.setColor(new Color(0, 0, 255));
                            break;
                        case 2:
                            g.setColor(new Color(0, 128, 0));
                            break;
                        case 3:
                            g.setColor(new Color(255, 0, 0));
                            break;
                        case 4:
                            g.setColor(new Color(0, 0, 128));
                            break;
                        case 5:
                            g.setColor(new Color(128, 0, 0));
                            break;
                        case 6:
                            g.setColor(new Color(0, 128, 128));
                            break;
                        case 7:
                            g.setColor(new Color(0, 0, 0));
                            break;
                        case 8:
                            g.setColor(new Color(128, 128, 128));
                            break;
                        default:
                            break;
                    }
                    g.drawString(String.valueOf(n), 30 + 70 * j, 40 + 70 * i);
                } else if (ms.isFlagged(j, i)) {
                    g.setColor(Color.RED);
                    g.fillRect(30 + 70 * j, 30 + 70 * i, 10, 10);
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
