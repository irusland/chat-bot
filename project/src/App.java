import java.io.*;


public class App
{
    public static void main(String[] args) throws Exception {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        boolean started = false;
        String name = "";
        Context context = null;
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
                    context = new Context(System.in , System.out);
                    context.start();
                        break;
                default:
                        break;
            }
        }
    }
}