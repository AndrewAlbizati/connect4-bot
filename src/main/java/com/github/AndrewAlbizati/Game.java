package com.github.AndrewAlbizati;

import org.javacord.api.entity.user.User;

public class Game {
    private Tile[][] board;
    public static final int WIDTH = 7;
    public static final int LENGTH = 6;

    private final User player1;
    private final User player2;

    private User currentTurn;

    public Game(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentTurn = player1;

        board = new Tile[LENGTH][WIDTH];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                board[r][c] = Tile.EMPTY;
            }
        }
    }

    public User getPlayer1() {
        return player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public User getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Switch the player who is allowed to place a tile.
     * Player1 --> Player2
     * Player2 --> Player1
     */
    public void alternateTurn() {
        if (currentTurn == player1) {
            currentTurn = player2;
        } else {
            currentTurn = player1;
        }
    }

    /**
     * Places a tile on the board and alternates the player who is allowed to place a tile.
     * The turn isn't alternated if the tile is placed out of bounds.
     * @param pos The column of the tile.
     */
    public void placeTile(int pos) {
        int r = 0;
        if (board[r][pos] != Tile.EMPTY) {
            return;
        }

        while (inBounds(r + 1, pos) && board[r + 1][pos] == Tile.EMPTY) {
            r++;
        }

        if (currentTurn == player1) {
            board[r][pos] = Tile.YELLOW;
        } else {
            board[r][pos] = Tile.RED;
        }
        alternateTurn();
    }

    /**
     * Test if a position on the board will raise an ArrayIndexOutOfBoundsException.
     * @param r Row of tile.
     * @param c Column of tile.
     * @return If the position is valid or not.
     */
    private boolean inBounds(int r, int c) {
        return (r >= 0 && c >= 0) && (r < board.length && c < board[0].length);
    }

    /**
     * Determines if a player has 4 tiles in a row, either across, vertical, or diagonal.
     * @return The User who was won, or null if no winner.
     */
    public User getWinner() {
        // Check for 4 across
        for (int row = 0; row < board.length; row++){
            for (int col = 0;col < board[0].length - 3;col++) {
                if (board[row][col] != Tile.EMPTY &&
                        board[row][col + 1] == board[row][col] &&
                        board[row][col + 2] == board[row][col] &&
                        board[row][col + 3] == board[row][col]) {
                    return (board[row][col] == Tile.YELLOW ? player1 : player2);
                }
            }
        }
        // Check for 4 up and down
        for (int row = 0; row < board.length - 3; row++){
            for(int col = 0; col < board[0].length; col++){
                if (board[row][col] != Tile.EMPTY &&
                        board[row + 1][col] == board[row][col] &&
                        board[row + 2][col] == board[row][col] &&
                        board[row + 3][col] == board[row][col]) {
                    return (board[row][col] == Tile.YELLOW ? player1 : player2);
                }
            }
        }
        // Check upward diagonal
        for (int row = 3; row < board.length; row++){
            for(int col = 0; col < board[0].length - 3; col++){
                if (board[row][col] != Tile.EMPTY &&
                        board[row - 1][col + 1] == board[row][col] &&
                        board[row - 2][col + 2] == board[row][col] &&
                        board[row - 3][col + 3] == board[row][col]) {
                    return (board[row][col] == Tile.YELLOW ? player1 : player2);
                }
            }
        }
        // Check downward diagonal
        for (int row = 0; row < board.length - 3; row++){
            for(int col = 0; col < board[0].length - 3; col++){
                if (board[row][col] != Tile.EMPTY &&
                        board[row + 1][col + 1] == board[row][col] &&
                        board[row + 2][col + 2] == board[row][col] &&
                        board[row + 3][col + 3] == board[row][col]) {
                    return (board[row][col] == Tile.YELLOW ? player1 : player2);
                }
            }
        }
        return null;
    }

    /**
     * Determines if the board has been completely filled with non-empty tiles.
     * @return If the board is completely filled.
     */
    public boolean isFilled() {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == Tile.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Shows the board as well as the column numbers.
     * @return A square of Discord emojis representing the board.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int r = 1; r <= board[0].length; r++) {
            builder.append(switch(r) {
                case 1 -> ":one:";
                case 2 -> ":two:";
                case 3 -> ":three:";
                case 4 -> ":four:";
                case 5 -> ":five:";
                case 6 -> ":six:";
                case 7 -> ":seven:";
                case 8 -> ":eight:";
                case 9 -> ":nine:";
                case 10 -> ":ten:";
                default -> throw new IllegalStateException("Unexpected value: " + r);
            });
            builder.append(" ");
        }
        builder.append("\n");

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                builder.append(board[r][c].emoji);
                builder.append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
