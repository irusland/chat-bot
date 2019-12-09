package game.calculator;

import auth.Auth;
import bot.Database;
import game.Statistic;
import bot.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Stat implements Statistic {
    private final ArrayList<String> history;
    private StringBuilder sb;
    private ArrayList<Pair<String, String>> types = new ArrayList<>();
    private String table = "CALCSTAT";

    public Stat() {
        history = new ArrayList<>();
        sb = new StringBuilder();
        types.add(new Pair<>("LINE", "VARCHAR"));

        try {
            Database database = new Database(table, types);


            ArrayList<HashMap<String, String>> rows = database.selectList(types, Auth.getPersonName());
            for (HashMap<String, String> row: rows) {
                for (Pair<String, String> column: types) {
                    String line = row.get(column.getFirst());
                    history.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toStringStat() {
        StringBuilder res = new StringBuilder();
        res.append("Operation history\n");
        for (String s : history) {
            res.append(s);
        }
        return res.toString();
    }

    public void startAppend(int i) {
        sb.append(i);
    }

    public void appendWithBrackets(String o, int i) {
        sb.append(o).append(i).append(")");
        sb.insert(0,"(");
    }

    public void saveToHistory() {
        history.add(sb.toString());

        ArrayList<Pair<String, String>> a = new ArrayList<>();
        a.add(new Pair<>("LINE", sb.toString()));
        try {
            Database database = new Database(table, types);
            database.insert(new ArrayList<>(a), Auth.getPersonName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb = new StringBuilder();
    }

    public void endAppend(int r) {
        sb.append("=").append(r).append("\n");
    }
}
