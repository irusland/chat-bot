package bot;

import console_channel.ConsoleChannel;
import telegram_channel.Channel;
import telegram_channel.ChannelInitializer;
import auth.Auth;
import game.Game;
import game.calculator.Calculator;
import game.shipwars.ShipWars;
import game.tictactoe.TicTacToe;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Bot {
    private static PrintStream writer;
    public static ArrayList<String> gateOut;

    private static HashMap<String, bot.Channel> channels;
    private static String active_channel;

    public Bot(InputStream in, PrintStream out) throws IOException, ClassNotFoundException {
        writer = System.out;
        gateOut = new ArrayList<>();
        ChannelInitializer.main(new String[]{});
        channels = new HashMap<>();
        channels.put("telegram", ChannelInitializer.channel);
        channels.put("console", new ConsoleChannel());
        active_channel = "telegram";
    }

    public static String getAnswer() {
        if (active_channel.equals("console")) {
            return "Player is in console";
        }
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
        return channels.get(active_channel).readQuery();
    }

    private void sendOutput() {
        if (active_channel.equals("console"))
            writer.println(sb.toString());
        else
            gateOut.add(sb.toString());
        sb = new StringBuilder();
    }

    private static StringBuilder sb = new StringBuilder();

    private void enqueueOutput(String s) {
        sb.append(s).append("\n");
    }

    public void start() throws Exception {
        while (true) {
            while (!Auth.loggedIn) {
                Auth.load();
                enqueueOutput("Choose /login or /register or /exit");
                sendOutput();
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
                enqueueOutput("Выбери игру\n" + "\n/xo" + "\n/ship" + "\n/calc" + "\n/logout" + "\n/switch");
                sendOutput();
                var choice = readQuery();
//                writer.println(choice);
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
                    case "/switch":
                        if (!active_channel.equals("console")) {
                            enqueueOutput("Switched to console");
                            active_channel = "console";
                        } else {
                            enqueueOutput("Switched to telegram");
                            active_channel = "telegram";
                        }
                        break;
                    default:
                        enqueueOutput("Неправильный выбор");
                }
                sendOutput();
            }
        }
    }

    private void play(Class cls) throws Exception {
//        System.out.println("Playing " + cls.getSimpleName());
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
        enqueueOutput(game.load());
        sendOutput();
        while (true) {
            if (game.isFinished()) {
//                Auth.getProcesses().remove(game);
                enqueueOutput(game.reset());
                break;
            }
            var query = readQuery();
            if (isCommand(query)) {
                switch (query) {
                    case "/pause":
                        enqueueOutput("Game paused");
                        Auth.save();
                        return;
                    case "/stat":
                        enqueueOutput(game.getStatistics());
                        break;
                    case "/reset":
                        enqueueOutput(game.reset());
                        break;
                    default:
                        enqueueOutput("Unknown command");
                        break;
                }
            } else {
                var response = game.request(query);
                enqueueOutput(response);
                Auth.save();
            }
            sendOutput();
        }
    }

    private boolean isCommand(String query) {
        return query.contains("/");
    }

    private void login(String name, String pass) {
        enqueueOutput(Auth.login(name, pass));
    }

    private void register(String name, String pass) throws IOException {
        enqueueOutput(Auth.register(name, pass));
    }

    private String getName() throws IOException {
        enqueueOutput("Введите имя");
        sendOutput();
        return readQuery();
    }

    private String getPass() throws IOException {
        enqueueOutput("Введите пароль");
        sendOutput();
        return readQuery();
    }
}

