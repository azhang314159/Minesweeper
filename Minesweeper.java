import java.util.*;

public class Minesweeper {

    private int[][] board;
    private boolean gameOver;
    private boolean win;
    private boolean firstTurn;
    private int revealedCells;
    private boolean[][] flagged;

    /**
     * Constructor sets up game state.
     */
    public Minesweeper() {
        reset();
    }

    public Minesweeper(int[][] arr, boolean[][] f) {
        if (arr.length != 9 || f.length != 9) {
            throw new IllegalArgumentException("Invalid board size");
        }
        for (int i = 0; i < 9; i++) {
            if (arr[i].length != 9 || f[i].length != 9) {
                throw new IllegalArgumentException("Invalid board size");
            }
        }

        board = arr;
        flagged = f;
        firstTurn = true;
        TreeSet<Integer> valid = new TreeSet<>(Arrays.asList(-999, -9, -8, -7, -6, -5, -4, -3, -2,
                -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 999));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!valid.contains(getCell(j, i))) {
                    throw new IllegalArgumentException("Invalid cell in board");
                }

                int expectedMines = 0;
                if (Math.abs(getCell(j, i)) != 999) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (i + dx >= 0 && i + dx < 9 && j + dy >= 0 && j + dy < 9 &&
                                    Math.abs(getCell(j + dy, i + dx)) == 999) {
                                expectedMines++;
                            }
                        }
                    }

                    if ((expectedMines + 1) != Math.abs(getCell(j, i))) {
                        throw new IllegalArgumentException("Invalid cell in board");
                    }
                }

                if (getCell(j, i) > 0) {
                    firstTurn = false;
                    if (isFlagged(j, i)) {
                        throw new IllegalArgumentException("Revealed cells should not be flagged");
                    }
                    if (getCell(j, i) == 999) {
                        gameOver = true;
                        win = false;
                    }
                    revealedCells++;
                }
            }
        }
        if (!gameOver && revealedCells == 71) {
            gameOver = true;
            win = true;
        }
    }

    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed.
     *
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     */
    public boolean playTurn(int c, int r) {
        if (c < 0 || c > 8 || r < 0 || r > 8 || board[r][c] > 0 || gameOver || flagged[r][c]) {
            return false;
        }

        if (board[r][c] == -999) {
            gameOver = true;
            board[r][c] = -1 * board[r][c];
            revealedCells++;
            return true;
        }

        if (firstTurn) {
            flagged = new boolean[9][9];
            dfs(c, r);
            firstTurn = false;
        } else {
            board[r][c] = -1 * board[r][c];
            revealedCells++;
            if (revealedCells == 71) {
                gameOver = true;
                win = true;
            }
        }
        return true;
    }

    /**
     * Performs a DFS that recursively reveals non-mine cells until 20 total cells have been
     * revealed, or until no more non-mine cells are adjacent to the revealed patch of cells.
     */
    public void dfs(int c, int r) {
        if (c >= 0 && c < 9 && r >= 0 && r < 9) {
            if (revealedCells < 20) {
                if (board[r][c] < 0 && board[r][c] != -999) {
                    board[r][c] = -1 * board[r][c];
                    revealedCells++;

                    dfs(c + 1, r);
                    dfs(c - 1, r);
                    dfs(c, r + 1);
                    dfs(c, r - 1);
                }
            }
        }
    }
    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[9][9];
        flagged = new boolean[9][9];

        Random r = new Random();
        TreeSet<Integer> mines = new TreeSet<>();
        while (mines.size() < 10) {
            mines.add(r.nextInt(81));
        }
        generateBoard(mines);

        gameOver = false;
        firstTurn = true;
        revealedCells = 0;
    }

    public void generateBoard(Set<Integer> mines) {
        board = new int[9][9];

        for (int m : mines) {
            if (m < 0 || m >= 81) {
                throw new IllegalArgumentException("Mine locations must be between 0 and 80.");
            }

            int row = m / 9;
            int col = m - 9 * row;
            board[row][col] = -999;
        }

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] != -999) {
                    board[row][col] = -1;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (row + i >= 0 && row + i < 9 && col + j >= 0 && col + j < 9 &&
                                    board[row + i][col + j] == -999) {
                                board[row][col] -= 1;
                            }
                        }
                    }
                }
            }
        }
    }

    public void flag(int c, int r) {
        if (c < 0 || c > 8 || r < 0 || r > 8 || gameOver || getCell(c, r) > 0) {
            return;
        }

        flagged[r][c] = !flagged[r][c];
    }

    public boolean isFlagged(int c, int r) {
        return flagged[r][c];
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board.
     */
    public int getCell(int c, int r) {
        return board[r][c];
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isWin() {
        return win;
    }

}
