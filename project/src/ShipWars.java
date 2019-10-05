import java.awt.*;
import java.util.*;

public class ShipWars implements Game {
    private final Board board;
    public ShipWars() {
        board = new Board(8);
    }

    public String Start() {
        board.Shuffle();
        return "Game generated \n" + board.toString();
    }

    public String Request(String query) throws Exception {
        var x = 0;
        var y = 0;
        try {
            x = Integer.parseInt(Character.toString(query.charAt(0)));
            y = Integer.parseInt(Character.toString(query.charAt(1)));
        } catch (Exception e) {
            return "Не верные координаты";
        }
        if (board.Shoot(new Point(x, y))) {
            return "Ранил \n" + board.toString();
        }
        return "Мимо \n" + board.toString();
    }

    public Boolean IsFinished() {
        return false;
    }

    private class Board {

        public final int size;
        private final GameTile[][] board;

        public Board(int size) {
            this.size = size;
            board = new GameTile[size][size];
            Clear();
        }

        private void Clear() {
            for (var i = 0; i < size; i++) {
                for (var j = 0; j < size; j++) {
                    board[j][i] = new Water();
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("┌ ");
            builder.append("- ".repeat(Math.max(0, size)));
            builder.append("┐\n");

            for (var i = 0; i < size; i++) {
                builder.append("| ");

                for (var j = 0; j < size; j++) {
                    builder.append(board[j][i]);
                    builder.append(" ");
                }

                builder.append("|\n");
            }
            builder.append("└ ");
            builder.append("- ".repeat(Math.max(0, size)));
            builder.append("┘\n");

            return builder.toString();
        }

        private boolean TrySetShip(Point anchor, Point direction, int length) {
            if (IsFreeSpace(anchor, direction, length)) {
                VectorToLinkedShip(anchor, direction, length);
                return true;
            }
            return false;
        }

        private void VectorToLinkedShip(Point anchor, Point direction, int shipLength) {
            for (var i = 0; i < shipLength; i++) {
                var position = new Point(anchor.x + direction.x * i,anchor.y + direction.y * i);
                var current = new ShipShard(position, null, null, shipLength);
                board[position.x][position.y] = current;
            }
            if (shipLength == 1)
                return;
            for (var i = 0; i < shipLength; i++) {
                var current = new Point(anchor.x + direction.x * i,anchor.y + direction.y * i);
                var previous = new Point(anchor.x + direction.x * (i - 1),anchor.y + direction.y * (i - 1));
                var next = new Point(anchor.x + direction.x * (i + 1),anchor.y + direction.y * (i + 1));
                if (i == 0) {
                    ((ShipShard)board[current.x][current.y]).back = (ShipShard) board[next.x][next.y];
                } else if (i == shipLength - 1) {
                    ((ShipShard)board[current.x][current.y]).front = (ShipShard) board[previous.x][previous.y];
                } else {
                    ((ShipShard)board[current.x][current.y]).back = (ShipShard) board[next.x][next.y];
                    ((ShipShard)board[current.x][current.y]).front = (ShipShard) board[previous.x][previous.y];
                }
            }
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
            try {
                var a = board[point.x ][point.y];
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
            for (int dx : offsets) {
                for (int dy : offsets) {
                    try {
                        if (!(board[point.x + dx][point.y + dy] instanceof Water))
                            return false;
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }
            }
            return isFree;
        }

        public void Shuffle() {
            var directions = new ArrayList<Point>();
            directions.add(new Point(0, 1));
            directions.add(new Point(0, -1));
            directions.add(new Point(1, 0));
            directions.add(new Point(-1, 0));

            var ships = new HashMap<Integer, Integer>();
            ships.put(1, 4);
            ships.put(2, 3);
            ships.put(3, 2);
            ships.put(4, 1);

            var rnd = new Random();
            var currLength = 4;
            while (!IsEmpty(ships) ) {
                var set = false;
                var dirPointer = 0;
                while (!set) {
                    var x = rnd.nextInt(size);
                    var y = rnd.nextInt(size);
                    var anchor = new Point(x, y);
                    var dir = directions.get(dirPointer);
                    var length = ships.get(currLength);
                    if (TrySetShip(anchor, dir, length)) {
                        set = true;
                        ships.put(currLength, ships.get(currLength) - 1);
                        if (ships.get(currLength) == 0)
                            currLength--;
                    }
                    dirPointer = (dirPointer + 1) % 4;
                }
            }
        }

        private boolean IsEmpty(HashMap<Integer, Integer> ships) {
            for (var l : ships.keySet()) {
                if (ships.get(l) > 0)
                    return false;
            }
            return true;
        }

        private boolean Shoot(Point aim) {
            try {
                var tile = board[aim.x][aim.y];
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
            var tile = board[aim.x][aim.y];
            if (tile instanceof ShipShard) {
                ((ShipShard) tile).isAlive = false;
                return true;
            }
            if (tile instanceof Water) {
                board[aim.x][aim.y] = new Miss();
                return false;
            }
            return false;
        }
    }

    private class Water implements GameTile {
        public String toString() {
            return ".";
        }
    }

    private class Miss implements GameTile {
        public String toString() {
            return "^";
        }
    }

    public class ShipShard implements GameTile {
        public Point anchor;
        public ShipShard back;
        public ShipShard front;
        public boolean isAlive;
        public int length;

        public ShipShard(Point anchor, ShipShard front, ShipShard back, int len) {
            this.anchor = anchor;
            this.front = front;
            this.back = back;
            isAlive = true;
            length = len;
        }

        @Override
        public String toString() {
            if (isAlive)
                return "" + length;
            return "X";
        }
    }
}
