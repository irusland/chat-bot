import TeleBot.BotInitializer;
import auth.Auth;
import game.Game;
import game.calculator.Calculator;
import game.shipwars.ShipWars;
import game.tictactoe.TicTacToe;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

class Bot {
    private BufferedReader reader;
    private PrintStream writer;

    Bot(InputStream in, PrintStream out) throws IOException, ClassNotFoundException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = System.out;

        BotInitializer.main(new String[]{});
    }

    void start() throws Exception {
        while (true) {
            while (!Auth.loggedIn) {
                Auth.load();
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
                        writer.println("Неправильный выбор");
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
        writer.println(game.load());
        while (true) {
            if (game.isFinished()) {
                Auth.getProcesses().remove(game);
                break;
            }
            var query = reader.readLine();
            if (isCommand(query)) {
                switch (query) {
                    case "/pause":
                        writer.println("Game paused");
                        Auth.save();
                        return;
                    default:
                        writer.println("Unknown command");
                        break;
                }
            } else {
                var response = game.request(query);
                writer.println(response);
                Auth.save();
            }
        }
    }

    private boolean isCommand(String query) {
        return query.contains("/");
    }

    private void login(String name, String pass) {
        writer.println(Auth.login(name, pass));
    }

    private void register(String name, String pass) throws IOException {
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

