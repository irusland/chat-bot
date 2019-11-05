package game.calculator;

import java.io.Serializable;
import java.util.ArrayList;

import static java.lang.Math.round;

public class Stat implements Serializable {
    private final ArrayList<String> history;
    private StringBuilder sb;

    public Stat() {
        history = new ArrayList<>();
        sb = new StringBuilder();
    }

    public String toString() {
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
        sb = new StringBuilder();
    }

    public void endAppend(int r) {
        sb.append("=").append(r).append("\n");
    }
}
