package game.calculator;

import java.io.Serializable;
import java.util.ArrayList;

import static java.lang.Math.round;

public class Stat implements Serializable {
    public final ArrayList<String> history;
    public StringBuilder sb;

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
}
