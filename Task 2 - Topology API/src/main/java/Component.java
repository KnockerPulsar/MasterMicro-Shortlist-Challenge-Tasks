import java.util.HashMap;
import java.util.Hashtable;

public class Component {
    String type, id;
    HashMap<String,String> netlist = new HashMap<>();

    // https://stackoverflow.com/questions/9625297/initializing-hashtables-in-java/9625403
    // Potential memory leak?
    public HashMap<String, Float > value = new HashMap<>(){
        {
            put("default", 100f);
            put("min", 10f);
            put("max", 1000f);
        }
    };
}
