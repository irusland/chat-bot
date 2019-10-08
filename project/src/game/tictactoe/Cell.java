package game.tictactoe;

enum Cell {
    Free(0, " "),
    Cross(1, "X"),
    Zero(-1, "0");

    int value;
    String string;

    private Cell(int value, String string) {
        this.value = value;
        this.string = string;
    }

    public Cell Or(Cell next) {
        if (next.value != Cell.Free.value) {
            return next;
        }
        return this;
    }

    public Cell Not() throws Exception {
        if (this == Cell.Free)
            throw new Exception("Cannot invert Cell.Free");
        if (this == Cell.Cross)
            return Cell.Zero;
        return Cell.Cross;
    }

    public boolean equals(Cell other) {
        return this.value==other.value;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
