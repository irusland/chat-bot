package game.tictactoe;

import java.io.Serializable;

public class Stat implements Serializable {
    private int crossTotal;
    private int crossWins;
    private int zeroTotal;
    private int zeroWins;

    public Stat() {

    }

    public void incCrossTotal() {
        crossTotal++;
    }

    public void incCrossWins() {
        crossWins++;
    }

    public void incZeroTotal() {
        zeroTotal++;
    }

    public void incZeroWins() {
        zeroWins++;
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
