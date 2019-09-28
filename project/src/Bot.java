import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Bot {

    private String name;
    private BufferedReader reader;
    private PrintStream writer;
    private List<String> a = new ArrayList<>() {};
    private Map<String, ArrayList<String>> knowledge = new HashMap<>() {};

    public Bot(InputStream in, PrintStream out)
    {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = System.out;
    }

    public void StartGame() throws IOException {
        var inGame = false;
        var isCross = true;
        var board = new int[3][3];
        Clear(board);
        while (!inGame) {
            writer.println("За кого играем?");
            var choise = reader.readLine();
            if (choise.equals("X")) {
                isCross = true;
                inGame = true;
            } else if (choise.equals("0") || choise.equals("O")) {
                isCross = false;
                inGame = true;
            }
        }
        while (inGame) {
            writer.println("Выбери координаты");
            var coordinates = reader.readLine();
            var x = Integer.parseInt(Character.toString(coordinates.charAt(0)));
            var y = Integer.parseInt(Character.toString(coordinates.charAt(1)));
            if (Mark(x, y, board, isCross)) {
                PrintBoard(board);
            } else {
                writer.println("Координаты заняты");
            }
        }
    }

    private void Clear(int[][] board) {
        for (var i = 0; i < board.length; i++) {
            Arrays.fill(board[i], -1);
        }
    }

    private void PrintBoard(int[][] board) {
        writer.println(board);
    }

    private boolean Mark(int x, int y, int[][] board, boolean isCross) {
        if (IsFree(x, y, board)) {
            if (isCross) {
                board[x][y] = 1;
            } else {
                board[x][y] = 0;
            }
            return true;
        }
        return false;
    }

    private boolean IsFree(int x, int y, int[][] board) {
        var place = board[x][y];
        return place == -1;
    }

    public void StartConversation() throws IOException, ParseException {
        var knowledgeQuestions = new Knowledge();
        int questionPointer = 0;
        var refused = false;
        writer.println("Сейчас я буду задавать личные вопросы, постарайся отвечать честно!");
        while (questionPointer < Knowledge.questionVariations.keySet().toArray().length) {
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
        var list = Knowledge.validAnswers.get(questionType);
        list.add(answer);
    }

    private String ProcessData(Map<String, ArrayList<String>> knowledge) {
        return null;
    }

    private boolean UserCanceled(String answer) {
        return (answer.equalsIgnoreCase("Не хочу отвечать"));
    }

    private void Refuse() {
        writer.println(GetRandomItem(Knowledge.botRefuse));
    }

    private void Approve() {
        writer.println(GetRandomItem(Knowledge.botApprove));
    }

    private boolean IsValid(String answer, String type) {
        var list = Knowledge.validAnswers.get(type);
        return list.contains(answer);
    }

    private String GetRandomItem(ArrayList<String> questions) {
        Random r = new Random();
        var max = questions.size();
        var rnd = r.nextInt(max);
        return questions.get(rnd);
    }

    private void Ask(String type) {
        var questions = Knowledge.questionVariations.get(type);
        var question = GetRandomItem(questions);
        writer.println(question);
    }

    private String GetQuestion(int pointer) {
        var array = Knowledge.questionVariations.keySet().toArray();
        return array[pointer].toString();
    }
}

