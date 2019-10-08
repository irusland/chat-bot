package game.tictactoe;

import game.Game;

import java.util.Random;

public class TicTacToe implements Game {
    private Cell playerCell;
    private final Board board;


    public TicTacToe() {
        board = new Board(3);
    }

    public String Start() {
        return "game.Game started with " + board.size + "x" + board.size + " choose side X | O";
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

}
