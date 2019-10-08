package game.shipwars;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Board {

    private ShipWars shipWars;
    public final int size;
    private final GameTile[][] board;

    public Board(ShipWars shipWars, int size) {
        this.shipWars = shipWars;
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

    public String toOpponentString() {
        StringBuilder builder = new StringBuilder();
        builder.append("┌ ");
        builder.append("- ".repeat(Math.max(0, size)));
        builder.append("┐\n");

        for (var i = 0; i < size; i++) {
            builder.append("| ");

            for (var j = 0; j < size; j++) {
                builder.append(board[j][i].toOpponentString());
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
            var current = new ShipShard(shipWars, position, null, null, shipLength);
            board[position.x][position.y] = current;
        }
        if (shipLength == 1)
            return;
        for (var i = 0; i < shipLength; i++) {
            var current = new Point(anchor.x + direction.x * i,anchor.y + direction.y * i);
            var previous = new Point(anchor.x + direction.x * (i - 1),anchor.y + direction.y * (i - 1));
            var next = new Point(anchor.x + direction.x * (i + 1),anchor.y + direction.y * (i + 1));
            ShipShard currentShip = (ShipShard)board[current.x][current.y];
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

    boolean Shoot(Point aim) {
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

    int GetShipsAlive() {
        var shipsAlive = 0;
        for (var y = 0; y < size; y++ ) {
            for (var x = 0; x < size; x++ ) {
                if (board[x][y] instanceof ShipShard && ((ShipShard) board[x][y]).isAlive)
                    shipsAlive++;
            }
        }
        return shipsAlive;
    }
}
