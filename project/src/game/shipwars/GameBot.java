package game.shipwars;

import java.awt.*;
import java.util.Random;

class GameBot {
    private final Board board;
    public GameBot(Board board1) {
        board = board1;
    }
    public Point GetChoice() {
        //TODO SOME CHOOSING LOGIC
        var rnd = new Random();
        var x = rnd.nextInt(board.size);
        var y = rnd.nextInt(board.size);
        return new Point(x,y);
    }
}
