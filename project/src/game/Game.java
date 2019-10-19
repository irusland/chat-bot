package game;

import java.io.Serializable;

public interface Game {
    String load();
    String request(String query) throws Exception;
    Boolean isFinished();
}
