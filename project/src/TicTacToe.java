import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class TicTacToe implements Game {
    private Cell playerCell;
    private final Board board;


    public TicTacToe(Integer size) {
        board = new Board(size);
    }

    public String Start() {
        return "Game started with " + board.size + "x" + board.size + " choose side X | O";
    }

    public String Request(String query) throws Exception {
        if (playerCell == null) {
            if (query.equals(Cell.Cross.toString())) {
                playerCell = Cell.Cross;
            } else if (query.equals(Cell.Zero.toString())) {
                playerCell = Cell.Zero;
            } else {
                return "Выберите X | O";
            }
            return "Введите координаты";
        }
        var x = 0;
        var y = 0;
        try {
            x = Integer.parseInt(Character.toString(query.charAt(0)));
            y = Integer.parseInt(Character.toString(query.charAt(1)));
        } catch (Exception e) {
            return "Не верные координаты";
        }
        var answer = Turn(x, y);
        if (answer.hasAnError)
            return answer.info;
        if (IsFinished())
            return "Игра окончена победа: " + playerCell;
        var banswer = BotTurn();
        if (IsFinished())
            return "Игра окончена победа: " + playerCell.Not();
        return board.toString();
    }

    public Boolean IsFinished() {
        return board.GetWinner() != Cell.Free;
    }

    private String BotTurn() throws Exception {
        Random r = new Random();
        var validCoordinates = false;
        var x = 0;
        var y = 0;
        while (!validCoordinates) {
            x = r.nextInt(3);
            y = r.nextInt(3);
            validCoordinates = board.TrySetCell(x, y, playerCell.Not());
        }
        return "Выбор бота, \n" + board;
    }

    private Response Turn(int x, int y) {
        if(board.TrySetCell(x, y, playerCell))
            return new Response(false, board.toString());
        return new Response(true, "Выберите другие координаты \n" + board.toString());
    }

    private enum Cell {
        Free(0, " "),
        Cross(1, "X"),
        Zero(-1, "S");

        private int value;
        private String string;

        private Cell(int value, String string) {
            this.value = value;
            this.string = string;
        }

        public Cell Or(Cell next) {
            if (next.value != Cell.Free.value) {
                return next;
            }
            return this;
        }

        public Cell Not() throws Exception {
            if (this == Cell.Free)
                throw new Exception("Cannot invert Cell.Free");
            if (this == Cell.Cross)
                return Cell.Zero;
            return Cell.Cross;
        }

        public boolean equals(Cell other) {
            return this.value==other.value;
        }

        @Override
        public String toString() {
            return this.string;
        }
    }

    private static class Board {

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
            if (IsFree(x, y)) {
                board[x][y] = cell;
                return true;
            }
            return false;
        }
    }

    private class Response {
        public String info;
        public boolean hasAnError;

        public Response(boolean hasAnError, String info) {
            this.info = info;
            this.hasAnError = hasAnError;
        }

        @Override
        public String toString() {
            return info;
        }
    }
}
