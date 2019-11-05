package game.tictactoe;

import game.Game;

import java.io.Serializable;
import java.util.Random;

public class TicTacToe implements Game, Serializable {
    private Cell playerCell;
    private Board board;
    private String cache;

    private Stat stat;

    public TicTacToe() {
        board = new Board(3);
        stat = new Stat();
        cache = "started with " + board.size + "x" + board.size + " choose side X | O";
    }

    @Override
    public String reset() {
        board = new Board(3);
        cache = "started with " + board.size + "x" + board.size + " choose side X | O";
        playerCell = null;
        return "Game reset";
    }

    public String load() {
        return cache;
    }

    public String request(String query) throws Exception {
        if (playerCell == null) {
            if (query.equals(Cell.Cross.toString())) {
                playerCell = Cell.Cross;
            } else if (query.equals(Cell.Zero.toString())) {
                playerCell = Cell.Zero;
            } else {
                cache = "Выберите X | O";
                return cache;
            }
            cache = "Введите координаты";
            return cache;
        }
        var x = 0;
        var y = 0;
        try {
            x = Integer.parseInt(Character.toString(query.charAt(0)));
            y = Integer.parseInt(Character.toString(query.charAt(1)));
        } catch (Exception e) {
            cache = "Не верные координаты";
            return cache;
        }
        var answer = turn(x, y);
        if (answer.hasAnError) {
            cache = answer.info;
            return cache;
        }
        if (isFinished()) {
            if (playerCell == Cell.Cross) {
                stat.incCrossTotal();
                stat.incCrossWins();
            } else if (playerCell == Cell.Zero) {
                stat.incZeroTotal();
                stat.incZeroWins();
            }
            cache = "\n" + "Игра окончена победа: " + playerCell;
            return cache;
        }
        var banswer = botTurn();
        if (isFinished()) {
            if (playerCell == Cell.Cross) {
                stat.incCrossTotal();
            } else if (playerCell == Cell.Zero) {
                stat.incZeroTotal();
            }
            cache = "Игра окончена победа: " + playerCell.not();
            return cache;
        }
        return board.toString();
    }

    public Boolean isFinished() {
        return board.getWinner() != Cell.Free;
    }

    private String botTurn() throws Exception {
        Random r = new Random();
        var validCoordinates = false;
        var x = 0;
        var y = 0;
        while (!validCoordinates) {
            x = r.nextInt(3);
            y = r.nextInt(3);
            validCoordinates = board.trySetCell(x, y, playerCell.not());
        }
        return "Выбор бота, \n" + board;
    }

    private Response turn(int x, int y) {
        if(board.trySetCell(x, y, playerCell))
            return new Response(false, board.toString());
        return new Response(true, "Выберите другие координаты \n" + board.toString());
    }

    @Override
    public String getStatistics() {
        return stat.toStringStat();
    }
}
