package game;

import java.io.Serializable;

public interface Game extends Serializable {
    String load();
    default String reset() {
        return "No reset for this game";
    }
    String request(String query) throws Exception;
    Boolean isFinished();
    default String getStatistics() {
        return "No stats shown for this game";
    }
}
