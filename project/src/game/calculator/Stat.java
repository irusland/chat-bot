package game.calculator;

import bot.Database;
import game.Statistic;

import java.util.ArrayList;
import java.util.HashMap;

public class Stat implements Statistic {
    private final ArrayList<String> history;
    private StringBuilder sb;
    private HashMap<String, String> types = new HashMap<>();

    public Stat() {
        history = new ArrayList<>();
        sb = new StringBuilder();
        types.put("LINE", "VARCHAR");

        try {
            Database database = new Database("CALCSTAT", types);

            HashMap<String, String> res = database.selectLast(new ArrayList<>(types.keySet()));
            System.out.println(res);
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

        ArrayList<String> a = new ArrayList<>();
        a.add(sb.toString());
        try {
            Database database = new Database("CALCSTAT", types);
            database.insert(new ArrayList<>(a));
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb = new StringBuilder();
    }

    public void endAppend(int r) {
        sb.append("=").append(r).append("\n");
    }
}
