package game.shipwars;

import game.Game;

import java.awt.*;

public class ShipWars implements Game {
    private final Board board;
    private final Board opponentBoard;
    private final GameBot opponent;
    private boolean prepared;
    private String cache;

    public ShipWars() {
        var size = 10;
        board = new Board(this, size);
        opponentBoard = new Board(this, size);
        opponent = new GameBot(opponentBoard);
        prepared = false;
        board.Shuffle();
        opponentBoard.Shuffle();
        cache = "generated \n" + board.toString() + opponentBoard.toOpponentString();
    }

    public String Load() {
        return cache;
    }

    public String Request(String query) throws Exception {
        board.Shoot(opponent.GetChoice());
        var x = 0;
        var y = 0;
        try {
            x = Integer.parseInt(Character.toString(query.charAt(0)));
            y = Integer.parseInt(Character.toString(query.charAt(1)));
        } catch (Exception e) {
            return "Не верные координаты";
        }
        if (opponentBoard.Shoot(new Point(x, y))) {
            cache = "\n" + "Ранил \n" + board.toString() + opponentBoard.toOpponentString();
            return cache;
        }
        cache = "\n" + "Мимо \n" + board.toString() + opponentBoard.toOpponentString();
        return cache;
    }

    public Boolean IsFinished() {
        var humanShipsCount = board.GetShipsAlive();
        var botShipsCount = opponentBoard.GetShipsAlive();
        if (humanShipsCount == 0 || botShipsCount == 0)
            return true;
        return false;
    }

}
