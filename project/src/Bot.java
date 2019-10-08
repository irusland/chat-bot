import game.Game;
import game.shipwars.ShipWars;
import game.tictactoe.TicTacToe;
import org.json.simple.parser.ParseException;

import java.io.*;

public class Bot {
    private BufferedReader reader;
    private PrintStream writer;

    public Bot(InputStream in, PrintStream out) throws IOException, ParseException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = System.out;
    }

    public void Start() throws Exception {
        while (true) {
            writer.println("Выбери игру");
            writer.println("/xo");
            writer.println("/ship");
            var choise = reader.readLine();
            switch (choise) {
                case "/xo":
                    Play(new TicTacToe(3));
                    break;
                case "/ship":
                    Play(new ShipWars());
                    break;
            }
        }
    }

    private void Play(Game game) throws Exception {
        writer.println(game.Start());
        while (!game.IsFinished()) {
            var query = reader.readLine();
            var response = game.Request(query);
            writer.println(response);
        }
    }
}

