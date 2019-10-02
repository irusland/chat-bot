public interface Game {
    String Start();
    String Request(String query) throws Exception;
    Boolean IsFinished();
}
