import game.Game;
import game.Player;
import game.calculator.Calculator;
import game.shipwars.ShipWars;
import game.tictactoe.TicTacToe;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.HashMap;

class Bot {
    private BufferedReader reader;
    private PrintStream writer;

    Bot(InputStream in, PrintStream out) {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = System.out;
    }

    void Start() throws Exception {
        while (true) {
            while (!Auth.loggedIn) {
                writer.println("Choose /login or /register or /exit");
                var c = reader.readLine();
                var name = "";
                var pass = "";
                var registered = false;
                switch (c) {
                    case "/register":
                        name = getName();
                        pass = getPass();
                        register(name, pass);
                        registered = true;
                    case "/login":
                        if (!registered) {
                            name = getName();
                            pass = getPass();
                        }
                        login(name, pass);
                        break;
                    case "/exit":
                        return;
                    default:
                        break;
                }
            }
            while (Auth.loggedIn) {
                writer.println("Выбери игру");
                writer.println("/xo");
                writer.println("/ship");
                writer.println("/calc");
                writer.println("/logout");
                var choice = reader.readLine();
                switch (choice) {
                    case "/xo":
                        Play(TicTacToe.class);
                        break;
                    case "/ship":
                        Play(ShipWars.class);
                        break;
                    case "/calc":
                        Play(Calculator.class);
                        break;
                    case "/logout":
                        Auth.logout();
                        break;
                    default:
                        writer.println("Неправильный выбор");
                }
            }
        }
    }

    private void Play(Class cls) throws Exception {
        System.out.println("Playing " + cls.getSimpleName());
        var classFound = false;
        Game game = null;
        for (var proc : Auth.getProcesses()) {
            if (proc.getClass().isAssignableFrom(cls)) {
                classFound = true;
                game = proc;
                break;
            }
        }
        if (classFound) {
            Continue(game);
        } else {
            game = Create(cls);
            Auth.getProcesses().add(game);
            Continue(game);
        }
    }

    private Game Create(Class cls) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return (Game)cls.getDeclaredConstructor().newInstance();
    }

    private void Continue(Game game) throws Exception {
        writer.println(game.Load());
        while (true) {
            if (game.IsFinished()) {
                Auth.getProcesses().remove(game);
                break;
            }
            var query = reader.readLine();
            if (query.equals("/pause")) {
                writer.println("Game paused");
                break;
            }
            var response = game.Request(query);
            writer.println(response);
        }
    }

    private void login(String name, String pass) {
        writer.println(Auth.login(name, pass));
    }

    private void register(String name, String pass) {
        writer.println(Auth.register(name, pass));
    }

    private String getName() throws IOException {
        writer.println("Введите имя");
        return reader.readLine();
    }

    private String getPass() throws IOException {
        writer.println("Введите пароль");
        return reader.readLine();
    }
}

