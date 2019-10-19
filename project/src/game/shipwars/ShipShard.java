package game.shipwars;

import java.awt.*;
import java.io.Serializable;

class ShipShard implements GameTile, Serializable {
    private ShipWars shipWars;
    public Point anchor;
    public ShipShard back;
    public ShipShard front;
    public boolean isAlive;
    public int length;
    public String toOpponentString() {
        if (!isAlive)
            return "X";
        return new Water().toString();
    }

    public ShipShard(ShipWars shipWars, Point anchor, ShipShard front, ShipShard back, int len) {
        this.shipWars = shipWars;
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
