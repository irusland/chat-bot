import game.Game;
import game.shipwars.ShipWars;
import game.tictactoe.TicTacToe;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Bot {
    private BufferedReader reader;
    private PrintStream writer;
    private ArrayList<Game> processes;

    public Bot(InputStream in, PrintStream out) throws IOException, ParseException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = System.out;
        processes = new ArrayList<>();
    }

    public void Start() throws Exception {
        while (true) {
            writer.println("Выбери игру");
            writer.println("/xo");
            writer.println("/ship");
            var choise = reader.readLine();
            switch (choise) {
                case "/xo":
                    Play(TicTacToe.class);
                    break;
                case "/ship":
                    Play(ShipWars.class);
                    break;
            }
        }
    }

    private void Play(Class cls) throws Exception {
        System.out.println("Playing " + cls.toString());
        var classFound = false;
        Game game = null;
        for (var proc : processes) {
            if (proc.getClass().isAssignableFrom(cls)) {
                classFound = true;
                game = proc;
                break;
            }
        }
        if (classFound) {
            Continue(game);
        } else {
            game = Restore(cls);
            processes.add(game);
            Continue(game);
        }
    }

    private Game Restore(Class cls) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Game game = (Game)cls.getDeclaredConstructor().newInstance();
        writer.println(game.Start());
        return game;
    }

    private void Continue(Game game) throws Exception {
        while (!game.IsFinished()) {
            var query = reader.readLine();
            if (query.equals("/pause")) {
                writer.println("Game paused");
                break;
            }
            var response = game.Request(query);
            writer.println(response);
        }
    }


}

