package bot;

import channel.ChannelInitializer;
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

public class Bot {
    private static BufferedReader reader;
    private static PrintStream writer;
    public static Queue<String> gateIn;
    public static Queue<String> consoleIn;
    public static ArrayList<String> gateOut;
    private static Boolean isAnswerAfterRead;
    private static Boolean isConsolePlaying;

    public Bot(InputStream in, PrintStream out) throws IOException, ClassNotFoundException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = System.out;
        gateIn = new LinkedList<>();
        consoleIn = new LinkedList<>();
        gateOut = new ArrayList<>();
        isConsolePlaying = false;
        isAnswerAfterRead = false;
        ChannelInitializer.main(new String[]{});
    }

    public static String getAnswer() {
        if (isConsolePlaying) {
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
        isAnswerAfterRead = false;
        return sb.toString();
    }

    private String readQuery() {
        if (isConsolePlaying) {
            AsyncReader.asyncRead(consoleIn);
            while (consoleIn.isEmpty()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            AsyncReader.interrupt();
            isAnswerAfterRead = true;
            return consoleIn.remove();
        }
        while (gateIn.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isAnswerAfterRead = true;
        return gateIn.remove();
    }

    private void sendOutput() {
        if (isConsolePlaying)
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
                        if (!isConsolePlaying) {
                            enqueueOutput("Switched to console");
                        } else {
                            enqueueOutput("Switched to telegram");
                        }
                        isConsolePlaying = !isConsolePlaying;
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

