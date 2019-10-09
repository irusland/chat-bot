package game;

public interface Game {
    String Load();
    String Request(String query) throws Exception;
    Boolean IsFinished();
}
