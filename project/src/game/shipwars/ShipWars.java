package game.shipwars;

import game.Game;

import java.awt.*;
import java.io.Serializable;

import static java.lang.Math.round;

public class ShipWars implements Game, Serializable {
    private Board board;
    private Board opponentBoard;
    private GameBot opponent;
    private boolean prepared;
    private String cache;

    private int gameWins;
    private int playerShots;
    private int playerPreciseShots;
    private int botShots;
    private int botPreciseShots;

    public ShipWars() {
        var size = 10;
        board = new Board(this, size);
        opponentBoard = new Board(this, size);
        opponent = new GameBot(opponentBoard);
        prepared = false;
        board.shuffle();
        opponentBoard.shuffle();
        cache = "generated \n" + board.toString() + opponentBoard.toOpponentString();
    }

    @Override
    public String reset() {
        var size = 10;
        board = new Board(this, size);
        opponentBoard = new Board(this, size);
        opponent = new GameBot(opponentBoard);
        prepared = false;
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
            botPreciseShots++;
        }
        botShots++;
        playerShots++;
        if (opponentBoard.shoot(new Point(x, y))) {
            playerPreciseShots++;
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
            gameWins++;
            return true;
        }
        return false;
    }

    @Override
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("Games won:  ").append(gameWins).append("\n");
        sb.append("Ships down: ").append(playerShots).append("\n");
        int playerPrecision = 0;
        if (playerShots != 0)
            playerPrecision = round((((float)playerPreciseShots / playerShots) * 100));
        sb.append("Player shot precision: ").append(playerPrecision).append("%").append("\n");
        int botPrecision = 0;
        if (playerShots != 0)
            botPrecision = round((((float)botPreciseShots / botShots) * 100));
        sb.append("Bot shot precision:    ").append(botPrecision).append("%").append("\n");
        return sb.toString();
    }
}
