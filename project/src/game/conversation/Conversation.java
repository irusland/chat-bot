package game.conversation;

import game.Game;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class Conversation implements Game {
    private List<String> a = new ArrayList<>() {};
    private Map<String, ArrayList<String>> knowledgeAnswers = new HashMap<>() {};
    private Knowledge knowledge;

    public Conversation() throws IOException, ParseException {
        knowledge = new Knowledge();
    }

    public void StartConversation() throws IOException, ParseException {
//        int questionPointer = 0;
//        var refused = false;
//        writer.println("Сейчас я буду задавать личные вопросы, постарайся отвечать честно!");
//        while (questionPointer < knowledge.questionVariations.keySet().toArray().length) {
//            var questionType = GetQuestion(questionPointer);
//            Ask(questionType);
//
//            String answer = reader.readLine();
//            if (!IsValid(answer, questionType)) {
//                var previous = answer;
//                refused = true;
//                while (refused)
//                {
//                    Refuse();
//                    Ask(questionType);
//                    answer = reader.readLine();
//                    if (previous.equalsIgnoreCase(answer))
//                    {
//                        Learn(answer, questionType);
//                        writer.println("Необычный ответ, но я запомнил");
//                        refused = false;
//                        break;
//                    }
//                    if (UserCanceled(answer))
//                        break;
//                    if (IsValid(answer, questionType))
//                        refused = false;
//                }
//            }
//            if (refused) {
//                writer.println("Понятно, что ты не хочешь отвечать на вопрос");
//                continue;
//            }
//            Approve();
//            questionPointer++;
//            if (!knowledgeAnswers.containsKey(questionType))
//                knowledgeAnswers.put(questionType, new ArrayList<>());
//            knowledgeAnswers.get(questionType).add(answer);
//        }
//        writer.println(knowledgeAnswers);
//        var info = ProcessData(knowledgeAnswers);
//        writer.println("Вот интересная информация о тебе: " + info);
    }

    private void Learn(String answer, String questionType) {
        var list = knowledge.validAnswers.get(questionType);
        list.add(answer);
    }

    private String ProcessData(Map<String, ArrayList<String>> knowledge) {
        return null;
    }

    private boolean UserCanceled(String answer) {
        return (answer.equalsIgnoreCase("Не хочу отвечать"));
    }

    private void Refuse() {
//        writer.println(GetRandomItem(knowledge.botRefuse));
    }

    private void Approve() {
//        writer.println(GetRandomItem(knowledge.botApprove));
    }

    private boolean IsValid(String answer, String type) {
        var list = knowledge.validAnswers.get(type);
        return list.contains(answer);
    }

    private String GetRandomItem(ArrayList<String> questions) {
        Random r = new Random();
        var max = questions.size();
        var rnd = r.nextInt(max);
        return questions.get(rnd);
    }

    private void Ask(String type) {
        var questions = knowledge.questionVariations.get(type);
        var question = GetRandomItem(questions);
//        writer.println(question);
    }

    private String GetQuestion(int pointer) {
        var array = knowledge.questionVariations.keySet().toArray();
        return array[pointer].toString();
    }

    @Override
    public String Start() {
        return null;
    }

    @Override
    public String Request(String query) throws Exception {
        return null;
    }

    @Override
    public Boolean IsFinished() {
        return null;
    }
}
