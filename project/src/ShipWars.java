import javax.xml.stream.FactoryConfigurationError;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public class ShipWars implements Game {
    public ShipWars() {

    }

    public String Start() {
        return null;
    }

    public String Request(String query) throws Exception {
        return null;
    }

    public Boolean IsFinished() {
        return null;
    }

    private class Board {

        public final int size;
        private final ShipShard[][] board;

        public Board(int size) {
            this.size = size;
            board = new ShipShard[size][size];
            Clear();
        }

        private void Clear() {
            for (var i = 0; i < size; i++) {
                for (var j = 0; j < size; j++) {
                    board[j][i] = null;
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("┌");
            builder.append("-".repeat(Math.max(0, size)));
            builder.append("┐\n");

            for (var i = 0; i < size; i++) {
                builder.append("|");
                for (var j = 0; j < size; j++) {
                    builder.append(board[j][i]);
                }
                builder.append("|\n");
            }
            builder.append("└");
            builder.append("-".repeat(Math.max(0, size)));
            builder.append("┘\n");

            return builder.toString();
        }

        private boolean TrySetShip(Point anchor, Point direction, int length) {
            if (IsFreeSpace(anchor, direction, length)) {
                VectorToShardShip(anchor, direction, length);
            }
        }

        private void VectorToShardShip(Point anchor, Point direction, int shipLength) {
            for (var i = 0; i < shipLength; i++) {
                var position = new Point(anchor.x + direction.x * i,anchor.y + direction.y * i);
                var current = new ShipShard(position, null);
                board[position.x][position.y] = current;
                if (i > 0) {
                    var prevPos = new Point(anchor.x + direction.x * (i - 1),anchor.y + direction.y * (i - 1));
                    board[prevPos.x][prevPos.y] = current;
                }
            }
        }

        private ShipShard GetCell(Point position) {
            return board[position.x][position.y];
        }

        private boolean IsFreeSpace(Point anchor, Point direction, int length) {
            var isFreeSpace = true;
            for (var i = 0; i < length; i++) {
                var position = new Point(anchor.x + direction.x * i,anchor.y + direction.y * i);
                if (!IsFreeArea(position))
                    isFreeSpace = false;
            }
            return isFreeSpace;
        }

        private boolean IsFreeArea(Point point) {
            var isFree = true;
            var offsets = new int[] {-1, 0, 1};
            for (int dx : offsets) {
                for (int dy : offsets) {
                    if (board[point.x + dx][point.y + dy] != null)
                        isFree = false;
                }
            }
            return isFree;
        }
    }

    private class ShipShard {
        public Point anchor;
        public ShipShard next;

        public ShipShard(Point anchor, ShipShard next) {
            this.anchor = anchor;
            this.next = next;
        }


    }
}
