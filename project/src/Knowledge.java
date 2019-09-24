import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Knowledge {
    public Knowledge() {

    }

    public static Map<String, ArrayList<String>> questionVariations = new HashMap<>();
    public static ArrayList<String> botApprove = new ArrayList<String>();
    public static ArrayList<String> botRefuse = new ArrayList<>();
    public static Map<String, String[]> validAnswers = new HashMap<>();

}
