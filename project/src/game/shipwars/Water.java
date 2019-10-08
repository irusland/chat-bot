package game.shipwars;

class Water implements GameTile {
    public String toString() {
        return ".";
    }
    public String toOpponentString() { return toString();};
}
