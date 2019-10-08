package game.conversation;

import java.io.File;
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
        File file = new File ("project/src/knowledge.json");
        Object obj = new JSONParser().parse(new FileReader(file.getAbsoluteFile()));
//        System.out.println(obj);
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

    public final Map<String, ArrayList<String>> questionVariations = new HashMap<>();
    public final Map<String, ArrayList<String>> validAnswers = new HashMap<>();
    public final ArrayList<String> botApprove;
    public final ArrayList<String> botRefuse;

}
