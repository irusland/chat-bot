import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

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
            return !IsFinished(board);
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

            }
        }
        return !IsFinished(board);
    }

    private boolean IsFinished(int[][] board) {
        for (var i = 0; i < board.length; i++) {
            if (SumSeries(new Point(i,0), new Point(0, 1), board) == 3)
                return true;
            if (SumSeries(new Point(0,i), new Point(1, 0), board) == 3)
                return true;
        }
        if (SumSeries(new Point(0, 0), new Point(1,1), board) == 3)
            return true;
        if (SumSeries(new Point(0, 2), new Point(1, -1), board) == 3)
            return true;
        return false;
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

