import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Bot {

    private String name;
    private BufferedReader reader;
    private PrintStream writer;
    public Bot(InputStream in, PrintStream out)
    {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = System.out;
    }

    private Map<String, String[]> questionVariations = new HashMap<>() {{
        put("name", new String[] {"Как тебя зовут?", "Как имя твоё?", "Какое у тебя имя?"});
        put("nickname", new String[] {"Как тебя называют?", "Какой твой ник?"});
        put("surname", new String[] {"Какая у тебя фамилия?", "Твоя фамилия?"});
        put("middleName", new String[] {"Как твое отчество?", "Твое отчество?"});
        put("mood", new String[] {"Как дела?", "Как настрой?", "Как настроение?"});
    }};

    private List<String> a = new ArrayList<>() {};

    private Map<String, ArrayList<String>> knowledge = new HashMap<>() {};

    public void Start() throws IOException, ParseException {
        var knowledgeQuestions = new Knowledge();
        int questionPointer = 0;
        var refused = false;
        writer.println("Сейчас я буду задавать личные вопросы, постарайся отвечать честно!");
        while (questionPointer < questionVariations.keySet().toArray().length) {
            var questionType = GetQuestion(questionPointer);
            Ask(questionType);

            String answer = reader.readLine();
            if (!IsValid(answer, questionType)) {
                var previous = answer;
                refused = true;
                while (refused)
                {
                    Refuse();
                    Ask(questionType);
                    answer = reader.readLine();
                    if (previous.equalsIgnoreCase(answer))
                    {
                        Learn(answer, questionType);
                        writer.println("Необычный ответ, но я запомнил");
                        refused = false;
                        break;
                    }
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
        }
        writer.println(knowledge);
        var info = ProcessData(knowledge);
        writer.println("Вот интересная информация о тебе: " + info);
    }

    private void Learn(String answer, String questionType) {
<<<<<<< HEAD
        var array = validAnswers.get(questionType);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(array));
        list.add(answer);
        validAnswers.put(questionType, list.toArray(new String[0]));
=======
        var list = Knowledge.validAnswers.get(questionType);
        list.add(answer);
>>>>>>> 032ce09... JSON Done
    }

    private String ProcessData(Map<String, ArrayList<String>> knowledge) {
        return null;
    }

    private boolean UserCanceled(String answer) {
        return (answer.equalsIgnoreCase("Не хочу отвечать"));
    }

    private void Refuse() {
        writer.println(GetRandomItem(botRefuse));
    }

    private String[] botApprove = new String[] {"Окей!", "Ответ записан!", "Хорошо", "Приятно слышать", "Как здорово!"};
    private String[] botRefuse = new String[] {"Не, так не пойдет!", "Не знаю такого!", "Попробую еще раз", "Я не понял тебя"};

    private void Approve() {
        writer.println(GetRandomItem(botApprove));
    }

    private Map<String, String[]> validAnswers = new HashMap<>() {{
        put("name", new String[] {"Александр", "Руслан"});
        put("nickname", new String[] {"import_alex", "irusland"});
        put("surname", new String[] {"Зиятдинов", "Сиражетдинов"});
        put("middleName", new String[] {"Рудельевич", "Владимирович"});
        put("mood", new String[] {"Хорошо", "Отлично", "Круто", "Великолепно"});
    }};

    private boolean IsValid(String answer, String type) {
<<<<<<< HEAD
        var array = validAnswers.get(type);
        return Arrays.asList(array).contains(answer);
=======
        var list = Knowledge.validAnswers.get(type);
        return list.contains(answer);
>>>>>>> 032ce09... JSON Done
    }

    private String GetRandomItem(String[] questions) {
        Random r = new Random();
<<<<<<< HEAD
        var max = questions.length;
        var rnd = r.nextInt((max));
        return questions[rnd];
    }

    private void Ask(String type) {
        var questions = questionVariations.get(type);
=======
        var max = questions.size();
        System.out.println(questions);
        var rnd = r.nextInt(max);
        return questions.get(rnd);
    }

    private void Ask(String type) {
        var questions = Knowledge.questionVariations.get(type);
        System.out.println(type);
>>>>>>> 032ce09... JSON Done
        var question = GetRandomItem(questions);
        writer.println(question);
    }

    private String GetQuestion(int pointer) {
<<<<<<< HEAD
        var list = questionVariations.keySet().toArray();
        return Array.get(list, pointer).toString();
=======
        var array = Knowledge.questionVariations.keySet().toArray();
        return array[pointer].toString();
>>>>>>> 032ce09... JSON Done
    }
}

