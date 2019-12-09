package game.tictactoe;

import auth.Auth;
import bot.Database;
import game.Statistic;
import bot.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class Stat implements Statistic {
    private int crossTotal;
    private int crossWins;
    private int zeroTotal;
    private int zeroWins;
    private ArrayList<Pair<String, String>> types = new ArrayList<>();
    private String table = "TICTACSTAT";

    public Stat() {
        types.add(new Pair<>("crossTotal", "VARCHAR"));
        types.add(new Pair<>("crossWins", "VARCHAR"));
        types.add(new Pair<>("zeroTotal", "VARCHAR"));
        types.add(new Pair<>("zeroWins", "VARCHAR"));

        try {
            Database database = new Database(table, types);
            HashMap<String, String> row = database.selectLast(types, Auth.getPersonName());
            crossTotal = Integer.parseInt(row.get("crossTotal"));
            crossWins = Integer.parseInt(row.get("crossWins"));
            zeroTotal = Integer.parseInt(row.get("zeroTotal"));
            zeroWins = Integer.parseInt(row.get("zeroWins"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incCrossTotal() { crossTotal++; save(); }

    public void incCrossWins() { crossWins++; save(); }

    public void incZeroTotal() { zeroTotal++; save(); }

    public void incZeroWins() { zeroWins++; save(); }

    private void save() {
        try {
            Database database = new Database(table, types);

            ArrayList<Pair<String, String>> vals = new ArrayList<>();
            vals.add(new Pair<>("crossTotal", Integer.toString(crossTotal)));
            vals.add(new Pair<>("crossWins", Integer.toString(crossWins)));
            vals.add(new Pair<>("zeroTotal", Integer.toString(zeroTotal)));
            vals.add(new Pair<>("zeroWins", Integer.toString(zeroWins)));

            database.insertPositional(vals, Auth.getPersonName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toStringStat() {
        StringBuilder sb = new StringBuilder();
        sb.append("Games\n")
                .append("   played: ").append(crossTotal + zeroTotal).append("\n")
                .append("   won:    ").append(crossWins + zeroWins).append("\n");
        sb.append("X\n")
                .append("   played: ").append(crossTotal).append("\n")
                .append("   won:    ").append(crossTotal).append("\n");
        sb.append("0\n")
                .append("   played: ").append(zeroTotal).append("\n")
                .append("   won:    ").append(zeroTotal).append("\n");
        return sb.toString();
    }
}
