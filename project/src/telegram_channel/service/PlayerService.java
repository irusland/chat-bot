package telegram_channel.service;

import telegram_channel.Player;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public final class PlayerService {

    private final Set<Player> players;

    public PlayerService() {
        players = new HashSet<>();
    }

    public boolean setUserDisplayedName(User user, String name) {

        if (!isDisplayedNameTaken(name)) {
            players.stream().filter(a -> a.getUser().equals(user)).forEach(a -> a.setDisplayedName(name));
            return true;
        }

        return false;
    }

    public boolean removeAnonymous(User user) {
        return players.removeIf(a -> a.getUser().equals(user));
    }

    public boolean addAnonymous(Player anonymous) {
        return players.add(anonymous);
    }

    public boolean hasAnonymous(User user) {
        return players.stream().anyMatch(a -> a.getUser().equals(user));
    }

    public String getDisplayedName(User user) {

        Player anonymous = players.stream().filter(a -> a.getUser().equals(user)).findFirst().orElse(null);

        if (anonymous == null) {
            return null;
        }
        return anonymous.getDisplayedName();
    }

    public Stream<Player> anonymouses() {
        return players.stream();
    }

    private boolean isDisplayedNameTaken(String name) {
        return players.stream().anyMatch(a -> Objects.equals(a.getDisplayedName(), name));
    }
}