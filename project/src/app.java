import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class Bot
{
    private String name;
    private BufferedReader reader;
    private PrintStream writer;
    public Bot(InputStream in, PrintStream out)
    {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = System.out;
    }

    public void makeFriend(String name)
    {
        this.name = name;
    }

    public String ask(String sentence)
    {
        sentence = sentence.toLowerCase();
        switch (sentence) 
        {
            case "hello":
                return "Hello " + name;
                
        }
        return "I dont know what to say";
    }

    private Map<String, String[]> questionVariations = new HashMap<>() {{
        put("name", new String[] {"Как тебя зовут?", "Как имя твоё?", "Какое у тебя имя?"});
        put("nickname", new String[] {"Как тебя называют?"});
        put("surname", new String[] {"Какая у тебя фамилия?"});
        put("middleName", new String[] {"Как твое отчество?"});
        put("mood", new String[] {"Как дела?", "Как настрой?", "Как настроение?"});
    }};

    private Map<String, ArrayList<String>> knowledge = new HashMap<>() {};

    public void Start() throws IOException { // Почему надо писать throws?
        int questionPointer = 0;
        var refused = false;
        writer.println("Сейчас я буду задавать личные вопросы, постарайся отвечать честно!");
        while (questionPointer < questionVariations.keySet().toArray().length) {
            var questionType = GetQuestion(questionPointer);
            Ask(questionType);

            String answer = reader.readLine();
            if (!IsValid(answer, questionType)) {
                refused = true;
                while (refused)
                {
                    Refuse();
                    Ask(questionType);
                    answer = reader.readLine();
                    if (UserCanceled(answer))
                        break;
                    if (IsValid(answer, questionType))
                        refused = false;
                }
            }
            if (refused) {
                writer.println("Понятно, что ты не хочешь отвечать на вопрос");
                continue;
            }
            Approve();
            questionPointer++;
            if (!knowledge.containsKey(questionType))
                knowledge.put(questionType, new ArrayList<>());
            knowledge.get(questionType).add(answer);
//            writer.println(knowledge);
        }
        writer.println("Вот интересная информация о тебе:");
        var info = ProcessData(knowledge);
        writer.println(knowledge);
        writer.println(info);
    }

    private String ProcessData(Map<String, ArrayList<String>> knowledge) {
        return null;
    }

    private boolean UserCanceled(String answer) {
        return false;
    }

    private void Refuse() {
        writer.println(GetRandomItem(botRefuse));
    }

    private String[] botApprove = new String[] {"Окей!", "Ответ записан!", "Хорошо", "Приятно слышать", "Как здорово!"};
    private String[] botRefuse = new String[] {"Не, так не пойдет!", "Не знаю такго!", "Попробую еще раз", "Я не понял тебя"};

    private void Approve() {
        writer.println(GetRandomItem(botApprove));
    }

    private boolean IsValid(String answer, String type) {
        return true; //TODO answer validation
    }

    private String GetRandomItem(String[] questions) {
        Random r = new Random();
        var max = questions.length;
        var rnd = r.nextInt((max));
        return questions[rnd];
    }

    private void Ask(String type) {
        var questions = questionVariations.get(type);
        var question = GetRandomItem(questions);
        writer.println(question);
    }

    private String GetQuestion(int pointer) {
        var list = questionVariations.keySet().toArray();
        return Array.get(list, pointer).toString();
    }
}

public class app
{
    public static void main(String[] args) throws IOException
    {
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
            if (!started & !expression.equals("/start"))
            {
                continue;
            }
            switch (expression)
            {
                case "/help":
                    System.out.println("Привет я Бот, хочешь поговорить?");
                    System.out.println("напиши /start");
                        break;
                case "/start":
                    started = true;
                    bot = new Bot(System.in , System.out);
                    bot.Start();
                    started = false;
                        break;
                default:
                    String answer = bot.ask(expression);
                    System.out.println(answer);
                        break;
            }
        }
    }
}