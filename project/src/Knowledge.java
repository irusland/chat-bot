import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.*;
import org.json.simple.parser.*;


public class Knowledge {
    public Knowledge() throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader("/Users/irusland/Desktop/UrFU/Java/chat-bot-java/project/src/knowledge.json"));
        System.out.println(obj);
        JSONObject jo = (JSONObject) obj;

        JSONObject questions = (JSONObject) jo.get("questionVariations");
        for (Object item: questions.keySet()) {
            questionVariations.put(item.toString(), (ArrayList<String>) questions.get(item.toString()));
        }

        questions = (JSONObject) jo.get("validAnswers");
        for (Object item: questions.keySet()) {
            validAnswers.put(item.toString(), (ArrayList<String>) questions.get(item.toString()));
        }

        botApprove = (ArrayList<String>) jo.get("botApprove"); //TODO Что сделать?
        botRefuse = (ArrayList<String>) jo.get("botRefuse");

    }

    public static Map<String, ArrayList<String>> questionVariations = new HashMap<>();
    public static Map<String, ArrayList<String>> validAnswers = new HashMap<>();
    public static ArrayList<String> botApprove = new ArrayList<>();
    public static ArrayList<String> botRefuse = new ArrayList<>();

}
