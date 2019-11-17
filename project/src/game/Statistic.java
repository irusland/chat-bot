package game;

import java.io.Serializable;

public interface Statistic extends Serializable {
    default String toStringStat() {
        return "Statistic is not set for this game";
    }
}
