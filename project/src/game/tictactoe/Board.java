package game.tictactoe;

import java.awt.*;

class Board {

    public final int size;
    private final Cell[][] board;

    public Board(int size) {
        this.size = size;
        board = new Cell[size][size];
        Clear();
    }

    private void Clear() {
        for (var i = 0; i < size; i++) {
            for (var j = 0; j < size; j++) {
                board[j][i] = Cell.Free;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("┌");
        builder.append("-".repeat(Math.max(0, size)));
        builder.append("┐\n");

        for (var i = 0; i < size; i++) {
            builder.append("|");
            for (var j = 0; j < size; j++) {
                builder.append(board[j][i].string);
            }
            builder.append("|\n");
        }
        builder.append("└");
        builder.append("-".repeat(Math.max(0, size)));
        builder.append("┘\n");

        return builder.toString();
    }

    private boolean IsFree(int x, int y) {
        return board[x][y] == Cell.Free;
    }

    public Cell GetWinner() {
        Cell winner = Cell.Free;
        for (var i = 0; i < size; i++) {
            winner = winner.Or(GetWinnerInRow(new Point(i,0), new Point(0, 1)));
            winner = winner.Or(GetWinnerInRow(new Point(0,i), new Point(1, 0)));
        }
        winner = winner.Or(GetWinnerInRow(new Point(0, 0), new Point(1,1)));
        winner = winner.Or(GetWinnerInRow(new Point(0, 2), new Point(1, -1)));
        return winner;
    }

    private Cell GetWinnerInRow(Point anchor, Point direction) {
        Point pos = new Point(anchor);
        var sum = 0;
        for (var i = 0; i < board.length; i++) {
            sum += board[pos.x][pos.y].value;
            pos.translate(direction.x, direction.y);
        }
        if (Math.abs(sum) == size) {
            if (sum < 0) {
                return Cell.Zero;
            } else if (sum > 0) {
                return Cell.Cross;
            } else {
                return Cell.Free;
            }
        }
        return Cell.Free;
    }

    public boolean TrySetCell(int x, int y, Cell cell) {
        try {

        if (IsFree(x, y)) {
            board[x][y] = cell;
            return true;
        }
        return false;

        } catch (Exception e) {
            return false;
        }
    }
}
