import game.Game;
import game.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Auth {
    public static boolean loggedIn;
    private static Player currentPlayer;
    private static HashMap<Player, ArrayList<Game>> playerProcesses = new HashMap<>();
    public static String login(String name, String pass) {
        var player = getPlayer(name, pass);
        if (player != null)
        {
            loggedIn = true;
            currentPlayer = player;
            return "Logged in " + currentPlayer.name;
        }
        return "Incorrect data";
    }

    private static Player getPlayer(String name, String pass) {
        for (var player : playerProcesses.keySet()) {
            if (name.equals(player.name) && pass.equals(player.password)) {
                return player;
            }
        }
        return null;
    }

    public static String register(String name, String pass) {
        var player = new Player(name, pass);
        if (getPlayer(name, pass) != null)
            return "Player already registered";
        playerProcesses.put(player, new ArrayList<>());
        return "Registered " + name + " " + pass;
    }

    public static String logout() {
        loggedIn = false;
        currentPlayer = null;
        return "logged out";
    }

    public static ArrayList<Game> getProcesses() {
        return playerProcesses.get(currentPlayer);
    }
}
