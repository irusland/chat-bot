import org.json.simple.parser.ParseException;

import java.io.*;


public class App
{
    public static void main(String[] args) throws IOException, ParseException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        boolean started = false;
        String name = "";
        Bot bot = null;
        while (true)
        {
            if (!started)
            {
                System.out.println("Start conversation with /start");
            }
            String expression = reader.readLine();
//            if (!started & !expression.equals("/start"))
//            {
//                continue;
//            }
            switch (expression)
            {
                case "/help":
                    System.out.println("Привет я Бот, хочешь поговорить?");
                    System.out.println("напиши /start");
                        break;
                case "/start":
                    started = true;
                    bot = new Bot(System.in , System.out);
                    bot.StartConversation();
                    started = false;
                        break;
                case "/game":
                    started = true;
                    bot = new Bot(System.in , System.out);
                    bot.StartGame();
                    started = false;
                    break;
                default:
//                    String answer = bot.Ask(expression);
//                    System.out.println(answer);
                        break;
            }
        }
    }
}