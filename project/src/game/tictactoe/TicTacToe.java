package game.tictactoe;

import game.Game;

import java.util.Random;

public class TicTacToe implements Game {
    private Cell playerCell;
    private final Board board;
    private String cache;

    public TicTacToe() {
        board = new Board(3);
        cache = "started with " + board.size + "x" + board.size + " choose side X | O";
    }

    public String Load() {
        return cache;
    }

    public String Request(String query) throws Exception {
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
        var answer = Turn(x, y);
        if (answer.hasAnError) {
            cache = answer.info;
            return cache;
        }
        if (IsFinished()) {
            cache = "\n" + "Игра окончена победа: " + playerCell;
            return cache;
        }
        var banswer = BotTurn();
        if (IsFinished()) {
            cache = "Игра окончена победа: " + playerCell.Not();
            return cache;
        }
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

}
