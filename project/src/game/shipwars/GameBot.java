package game.shipwars;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

class GameBot implements Serializable {
    private final Board board;
    public GameBot(Board board1) {
        board = board1;
    }
    public Point getChoice() {
        //TODO SOME CHOOSING LOGIC
        var rnd = new Random();
        var x = rnd.nextInt(board.size);
        var y = rnd.nextInt(board.size);
        return new Point(x,y);
    }
}
