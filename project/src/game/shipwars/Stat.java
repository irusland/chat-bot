package game.shipwars;

import java.io.Serializable;
import static java.lang.Math.round;

public class Stat implements Serializable {
    public int gameWins;
    public int playerShots;
    public int playerPreciseShots;
    public int botShots;
    public int botPreciseShots;

    public Stat() {

    }

    public String toString() {
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
