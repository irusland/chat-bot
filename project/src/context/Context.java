package context;

import bot.BotInitializer;
import auth.Auth;
import game.Game;
import game.calculator.Calculator;
import game.shipwars.ShipWars;
import game.tictactoe.TicTacToe;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Context {
    private static BufferedReader reader;
    private static PrintStream writer;
    public static Queue<String> gateIn;
    public static ArrayList<String> gateOut;

    public Context(InputStream in, PrintStream out) throws IOException, ClassNotFoundException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = System.out;
        gateIn = new LinkedList<>();
        gateOut = new ArrayList<>();
        BotInitializer.main(new String[]{});
    }

    public static String getAnswer() {
        while (gateOut.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
        }
        StringBuilder sb = new StringBuilder();
        for (String s : gateOut) {
            sb.append(s);
            sb.append('\n');
        }
        gateOut.clear();
        return sb.toString();
    }

    private String readQuery() {
//        String str_result= new RunInBackGround().execute().get();
        while (gateIn.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return gateIn.remove();
    }

    private void print(String s) {
        gateOut.add(s);
        writer.println(s);
    }

    public void start() throws Exception {
        while (true) {
            while (!Auth.loggedIn) {
                Auth.load();
                print("Choose /login or /register or /exit");
                var c = readQuery();
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
                print("Выбери игру\n" + "\n/xo" + "\n/ship" + "\n/calc" + "\n/logout");
                var choice = readQuery();
                switch (choice) {
                    case "/xo":
                        play(TicTacToe.class);
                        break;
                    case "/ship":
                        play(ShipWars.class);
                        break;
                    case "/calc":
                        play(Calculator.class);
                        break;
                    case "/logout":
                        Auth.logout();
                        break;
                    default:
                        print("Неправильный выбор");
                }
            }
        }
    }

    private void play(Class cls) throws Exception {
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
            resume(game);
        } else {
            game = create(cls);
            Auth.getProcesses().add(game);
            resume(game);
        }
    }

    private Game create(Class cls) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return (Game)cls.getDeclaredConstructor().newInstance();
    }

    private void resume(Game game) throws Exception {
        print(game.load());
        while (true) {
            if (game.isFinished()) {
                Auth.getProcesses().remove(game);
                break;
            }
            var query = readQuery();
            if (isCommand(query)) {
                switch (query) {
                    case "/pause":
                        print("Game paused");
                        Auth.save();
                        return;
                    default:
                        print("Unknown command");
                        break;
                }
            } else {
                var response = game.request(query);
                print(response);
                Auth.save();
            }
        }
    }

    private boolean isCommand(String query) {
        return query.contains("/");
    }

    private void login(String name, String pass) {
        print(Auth.login(name, pass));
    }

    private void register(String name, String pass) throws IOException {
        print(Auth.register(name, pass));
    }

    private String getName() throws IOException {
        print("Введите имя");
        return readQuery();
    }

    private String getPass() throws IOException {
        print("Введите пароль");
        return readQuery();
    }
}

