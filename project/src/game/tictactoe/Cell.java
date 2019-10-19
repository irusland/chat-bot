package game.tictactoe;

import java.io.Serializable;

enum Cell implements Serializable {
    Free(0, " "),
    Cross(1, "X"),
    Zero(-1, "0");

    int value;
    String string;

    Cell(int value, String string) {
        this.value = value;
        this.string = string;
    }

    public Cell or(Cell next) {
        if (next.value != Cell.Free.value) {
            return next;
        }
        return this;
    }

    public Cell not() throws Exception {
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
