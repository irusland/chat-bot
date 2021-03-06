package auth;

import game.Game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Player implements Serializable {
    public final String name;
    public final String password;

    public Player(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name) &&
                Objects.equals(password, player.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }
}
