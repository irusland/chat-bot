package game.shipwars;

import game.Statistic;

import static java.lang.Math.round;

public class Stat implements Statistic {
    private int gameWins;
    private int playerShots;
    private int playerPreciseShots;
    private int botShots;
    private int botPreciseShots;

    public Stat() {

    }

    public void incGameWins() {
        gameWins++;
    }

    public void incPlayerShots() {
        playerShots++;
    }

    public void incPlayerPreciseShots() {
        playerPreciseShots++;
    }

    public void incBotShots() {
        botShots++;
    }

    public void incBotPreciseShots() {
        botPreciseShots++;
    }

    public String toStringStat() {
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
