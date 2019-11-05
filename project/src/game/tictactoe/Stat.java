package game.tictactoe;

import java.io.Serializable;

public class Stat implements Serializable {
    public int crossTotal;
    public int crossWins;
    public int zeroTotal;
    public int zeroWins;

    public Stat() {

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
