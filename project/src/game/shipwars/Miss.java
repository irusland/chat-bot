package game.shipwars;

class Miss implements GameTile {
    public String toString() {
        return "^";
    }
    public String toOpponentString() {
        return toString();
    }
}
