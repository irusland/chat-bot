import game.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Auth implements Serializable {
    public static boolean loggedIn;
    private static Player currentPlayer;
    private static HashMap<Player, ArrayList<Game>> playerProcesses = new HashMap<>();
    public static String login(String name, String pass) {
        var player = getPlayer(name);
        if (player != null && player.password.equals(pass))
        {
            loggedIn = true;
            currentPlayer = player;
            return "Logged in " + currentPlayer.name;
        }
        return "Incorrect data";
    }

    private static Player getPlayer(String name) {
        for (var player : playerProcesses.keySet()) {
            if (name.equals(player.name)) {
                return player;
            }
        }
        return null;
    }

    public static String register(String name, String pass) throws IOException {
        if (getPlayer(name) != null)
            return "Player already registered";
        var player = new Player(name, pass);
        playerProcesses.put(player, new ArrayList<>());
        Auth.save();
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

    public static void save() throws IOException {
        FileOutputStream fos = new FileOutputStream("data.ser");
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(playerProcesses);
        out.close();
//        System.out.println("Saved" + playerProcesses);
    }

    public static void load() throws IOException, ClassNotFoundException {
        try {
            FileInputStream fis = new FileInputStream("data.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            playerProcesses = (HashMap<Player, ArrayList<Game>>) ois.readObject();
            ois.close();
//            System.out.println("Loaded" + playerProcesses);
        } catch (FileNotFoundException e) {
            Auth.save();
        }
    }
}
