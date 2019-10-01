import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Bot {

    private String name;
    private BufferedReader reader;
    private PrintStream writer;
<<<<<<< HEAD
    public Bot(InputStream in, PrintStream out)
    {
=======
    private List<String> a = new ArrayList<>() {};
    private Map<String, ArrayList<String>> knowledgeAnswers = new HashMap<>() {};
    private knowledge knowledge;

    public Bot(InputStream in, PrintStream out) throws IOException, ParseException {
>>>>>>> 6315ce5... Ext lib added
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = System.out;
        knowledge = new knowledge();
    }

<<<<<<< HEAD
    private Map<String, String[]> questionVariations = new HashMap<>() {{
        put("name", new String[] {"Как тебя зовут?", "Как имя твоё?", "Какое у тебя имя?"});
        put("nickname", new String[] {"Как тебя называют?", "Какой твой ник?"});
        put("surname", new String[] {"Какая у тебя фамилия?", "Твоя фамилия?"});
        put("middleName", new String[] {"Как твое отчество?", "Твое отчество?"});
        put("mood", new String[] {"Как дела?", "Как настрой?", "Как настроение?"});
    }};

    private List<String> a = new ArrayList<>() {};

    private Map<String, ArrayList<String>> knowledge = new HashMap<>() {};
=======
    public void StartGame() throws Exception {
        var inGame = false;
        var isCross = true;
        var board = new int[3][3];
        Clear(board);
        while (!inGame) {
            writer.println("За кого играем?");
            var choise = reader.readLine();
            if (choise.equals("X")  || choise.equals("x")) {
                isCross = true;
                inGame = true;
            } else if (choise.equals("0") || choise.equals("O") || choise.equals("o")) {
                isCross = false;
                inGame = true;
            }
        }
        while (inGame) {
            inGame = Turn(board, isCross);
            if (!inGame)
                break;
            inGame = BotTurn(board, !isCross);
        }
        writer.println();
        writer.println("___Game ended___");
        PrintBoard(board);
    }

    private boolean BotTurn(int[][] board, boolean isCross) throws Exception {
        Random r = new Random();
        var invalidCoordinates = true;
        var x = 0;
        var y = 0;
        while (invalidCoordinates) {
             x = r.nextInt(3);
             y = r.nextInt(3);
             invalidCoordinates = !IsFree(x, y, board);
        }
        if (Mark(x, y, board, isCross)) {
            writer.println("Выбор бота");
            PrintBoard(board);
            return IsRunning(board);
        }
        PrintBoard(board);
        throw new Exception(x + "," + y + " was incorrect bot choice");
    }

    private boolean Turn(int[][] board, boolean isCross) throws IOException {
        var incorrect = true;
        while (incorrect) {
            writer.println("Выбери координаты");
            try {
                var coordinates = reader.readLine();
                var x = Integer.parseInt(Character.toString(coordinates.charAt(0)));
                var y = Integer.parseInt(Character.toString(coordinates.charAt(1)));
                if (Mark(x, y, board, isCross)) {
                    incorrect = false;
                } else {
                    writer.println("Координаты заняты");
                }
                writer.println("Ваш выбор");
                PrintBoard(board);
            } catch (Exception e ) {
                writer.println("Координаты не верны");
            }
        }
        return IsRunning(board);
    }

    private boolean IsRunning(int[][] board) {
        for (var i = 0; i < board.length; i++) {
            if (SumSeries(new Point(i,0), new Point(0, 1), board) == 3)
                return false;
            if (SumSeries(new Point(0,i), new Point(1, 0), board) == 3)
                return false;
        }
        if (SumSeries(new Point(0, 0), new Point(1,1), board) == 3)
            return false;
        return SumSeries(new Point(0, 2), new Point(1, -1), board) != 3;
    }

    private int SumSeries(Point anchor, Point vector, int[][] board) {
        Point pos = new Point(anchor.x, anchor.y);
        var sum = 0;
        for (var i = 0; i < board.length; i++) {
            sum += board[pos.x][pos.y];
            pos = new Point(pos.x + vector.x, pos.y + vector.y );
        }
        return Math.abs(sum);
    }

    private void Clear(int[][] board) {
        for (var i = 0; i < board.length; i++) {
            for (var j = 0; j < board.length; j++) {
                board[j][i] = 0;
            }
        }
    }

    private void PrintBoard(int[][] board) {
        writer.print("┌");
        for (var i = 0; i < board.length; i++) {
            writer.print("-");
        }
        writer.println("┐");

        for (var i = 0; i < board.length; i++) {
            writer.print("|");
            for (var j = 0; j < board.length; j++) {
                if (board[j][i] == 1)
                    writer.print("X");
                if (board[j][i] == 0)
                    writer.print(" ");
                if (board[j][i] == -1)
                    writer.print("0");
            }
            writer.println("|");
        }
        writer.print("└");
        for (var i = 0; i < board.length; i++) {
            writer.print("-");
        }
        writer.println("┘");
    }

    private boolean Mark(int x, int y, int[][] board, boolean isCross) {
        if (IsFree(x, y, board)) {
            if (isCross) {
                board[x][y] = 1;
            } else {
                board[x][y] = -1;
            }
            return true;
        }
        return false;
    }

    private boolean IsFree(int x, int y, int[][] board) {
        var place = board[x][y];
        return place == 0;
    }
>>>>>>> 8f48e77... Tic Tac Toe done

<<<<<<< HEAD
    public void Start() throws IOException, ParseException {
        var knowledgeQuestions = new Knowledge();
        int questionPointer = 0;
        var refused = false;
        writer.println("Сейчас я буду задавать личные вопросы, постарайся отвечать честно!");
        while (questionPointer < questionVariations.keySet().toArray().length) {
=======
    public void StartConversation() throws IOException, ParseException {
        int questionPointer = 0;
        var refused = false;
        writer.println("Сейчас я буду задавать личные вопросы, постарайся отвечать честно!");
        while (questionPointer < knowledge.questionVariations.keySet().toArray().length) {
>>>>>>> 6315ce5... Ext lib added
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
            if (!knowledgeAnswers.containsKey(questionType))
                knowledgeAnswers.put(questionType, new ArrayList<>());
            knowledgeAnswers.get(questionType).add(answer);
        }
        writer.println(knowledgeAnswers);
        var info = ProcessData(knowledgeAnswers);
        writer.println("Вот интересная информация о тебе: " + info);
    }

    private void Learn(String answer, String questionType) {
<<<<<<< HEAD
<<<<<<< HEAD
        var array = validAnswers.get(questionType);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(array));
        list.add(answer);
        validAnswers.put(questionType, list.toArray(new String[0]));
=======
        var list = Knowledge.validAnswers.get(questionType);
=======
        var list = knowledge.validAnswers.get(questionType);
>>>>>>> 6315ce5... Ext lib added
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
<<<<<<< HEAD
        writer.println(GetRandomItem(botRefuse));
=======
        writer.println(GetRandomItem(knowledge.botRefuse));
>>>>>>> 6315ce5... Ext lib added
    }

    private String[] botApprove = new String[] {"Окей!", "Ответ записан!", "Хорошо", "Приятно слышать", "Как здорово!"};
    private String[] botRefuse = new String[] {"Не, так не пойдет!", "Не знаю такого!", "Попробую еще раз", "Я не понял тебя"};

    private void Approve() {
<<<<<<< HEAD
        writer.println(GetRandomItem(botApprove));
=======
        writer.println(GetRandomItem(knowledge.botApprove));
>>>>>>> 6315ce5... Ext lib added
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
<<<<<<< HEAD
        var array = validAnswers.get(type);
        return Arrays.asList(array).contains(answer);
=======
        var list = Knowledge.validAnswers.get(type);
=======
        var list = knowledge.validAnswers.get(type);
>>>>>>> 6315ce5... Ext lib added
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
<<<<<<< HEAD
        var questions = Knowledge.questionVariations.get(type);
        System.out.println(type);
>>>>>>> 032ce09... JSON Done
=======
        var questions = knowledge.questionVariations.get(type);
>>>>>>> 6315ce5... Ext lib added
        var question = GetRandomItem(questions);
        writer.println(question);
    }

    private String GetQuestion(int pointer) {
<<<<<<< HEAD
<<<<<<< HEAD
        var list = questionVariations.keySet().toArray();
        return Array.get(list, pointer).toString();
=======
        var array = Knowledge.questionVariations.keySet().toArray();
=======
        var array = knowledge.questionVariations.keySet().toArray();
>>>>>>> 6315ce5... Ext lib added
        return array[pointer].toString();
>>>>>>> 032ce09... JSON Done
    }
}

