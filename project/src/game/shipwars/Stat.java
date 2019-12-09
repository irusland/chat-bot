package game.shipwars;

import auth.Auth;
import bot.Database;
import bot.Pair;
import game.Statistic;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.round;

public class Stat implements Statistic {
    private int gameWins;
    private int playerShots;
    private int playerPreciseShots;
    private int botShots;
    private int botPreciseShots;
    private ArrayList<Pair<String, String>> types = new ArrayList<>();
    private String table = "SHIPSTAT";

    public Stat() {
        types.add(new Pair<>("gameWins", "VARCHAR"));
        types.add(new Pair<>("playerShots", "VARCHAR"));
        types.add(new Pair<>("playerPreciseShots", "VARCHAR"));
        types.add(new Pair<>("botShots", "VARCHAR"));
        types.add(new Pair<>("botPreciseShots", "VARCHAR"));

        try {
            Database database = new Database(table, types);
            HashMap<String, String> row = database.selectLast(types, Auth.getPersonName());
            gameWins = Integer.parseInt(row.get("gameWins"));
            playerShots = Integer.parseInt(row.get("playerShots"));
            playerPreciseShots = Integer.parseInt(row.get("playerPreciseShots"));
            botShots = Integer.parseInt(row.get("botShots"));
            botPreciseShots = Integer.parseInt(row.get("botPreciseShots"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incGameWins() {
        gameWins++;
        save();
    }

    public void incPlayerShots() {
        playerShots++;
        save();
    }

    public void incPlayerPreciseShots() {
        playerPreciseShots++;
        save();
    }

    public void incBotShots() {
        botShots++;
        save();
    }

    public void incBotPreciseShots() {
        botPreciseShots++;
        save();
    }

    private void save() {
        try {
            Database database = new Database(table, types);

            ArrayList<Pair<String, String>> vals = new ArrayList<>();
            vals.add(new Pair<>("gameWins", Integer.toString(gameWins)));
            vals.add(new Pair<>("playerShots", Integer.toString(playerShots)));
            vals.add(new Pair<>("playerPreciseShots", Integer.toString(playerPreciseShots)));
            vals.add(new Pair<>("botShots", Integer.toString(botShots)));
            vals.add(new Pair<>("botPreciseShots", Integer.toString(botPreciseShots)));

            database.insertPositional(vals, Auth.getPersonName());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
