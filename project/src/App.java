import org.json.simple.parser.ParseException;

import java.io.*;


public class App
{
    public static void main(String[] args) throws Exception {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        boolean started = false;
        String name = "";
        Bot bot = null;
        while (true)
        {
            System.out.println("Start bot with /bot");
            String expression = reader.readLine();
            switch (expression)
            {
                case "/help":
                    System.out.println("Привет я Бот, хочешь поговорить?");
                    System.out.println("напиши /start");
                        break;
                case "/bot":
                    bot = new Bot(System.in , System.out);
                    bot.Start();
                        break;
                default:
                        return;
            }
        }
    }
}