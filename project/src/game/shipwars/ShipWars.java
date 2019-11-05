package game.shipwars;

import game.Game;

import java.awt.*;
import java.io.Serializable;

public class ShipWars implements Game, Serializable {
    private Board board;
    private Board opponentBoard;
    private GameBot opponent;
    private String cache;

    private Stat stat;

    public ShipWars() {
        var size = 10;
        board = new Board(this, size);
        opponentBoard = new Board(this, size);
        opponent = new GameBot(opponentBoard);
        board.shuffle();
        opponentBoard.shuffle();
        stat = new Stat();
        cache = "generated \n" + board.toString() + opponentBoard.toOpponentString();
    }

    @Override
    public String reset() {
        var size = 10;
        board = new Board(this, size);
        opponentBoard = new Board(this, size);
        opponent = new GameBot(opponentBoard);
        board.shuffle();
        opponentBoard.shuffle();
        cache = "generated \n" + board.toString() + opponentBoard.toOpponentString();
        return "Game reset";
    }

    public String load() {
        return cache;
    }

    public String request(String query) throws Exception {
        var x = 0;
        var y = 0;
        try {
            x = Integer.parseInt(Character.toString(query.charAt(0)));
            y = Integer.parseInt(Character.toString(query.charAt(1)));
        } catch (Exception e) {
            return "Не верные координаты";
        }
        if (board.shoot(opponent.getChoice())) {
            stat.incBotPreciseShots();
        }
        stat.incBotShots();
        stat.incPlayerShots();
        if (opponentBoard.shoot(new Point(x, y))) {
            stat.incPlayerPreciseShots();
            cache = "\n" + "Ранил \n" + board.toString() + opponentBoard.toOpponentString();
            return cache;
        }
        cache = "\n" + "Мимо \n" + board.toString() + opponentBoard.toOpponentString();
        return cache;
    }

    public Boolean isFinished() {
        var humanShipsCount = board.getShipsAlive();
        var botShipsCount = opponentBoard.getShipsAlive();
        if (humanShipsCount == 0 || botShipsCount == 0) {
            stat.incGameWins();
            return true;
        }
        return false;
    }

    @Override
    public String getStatistics() {
        return stat.toString();
    }
}
