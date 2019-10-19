package game.shipwars;

import java.io.Serializable;

class Water implements GameTile, Serializable {
    public String toString() {
        return ".";
    }
    public String toOpponentString() { return toString();};
}
