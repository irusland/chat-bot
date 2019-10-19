package game.shipwars;

import java.io.Serializable;

class Miss implements GameTile, Serializable {
    public String toString() {
        return "^";
    }
    public String toOpponentString() {
        return toString();
    }
}
