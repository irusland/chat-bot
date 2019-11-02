package game;

import java.io.Serializable;

public interface Game {
    String load();
    String request(String query) throws Exception;
    Boolean isFinished();
    default String getStatistics() {
        return "No stats shown for this game";
    }
}
