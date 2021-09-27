import com.google.gson.JsonObject;

import java.util.HashMap;

public class nmos extends Component {
    // https://stackoverflow.com/questions/9625297/initializing-hashtables-in-java/9625403
    // Potential memory leak?
    public HashMap<String, Float > ml = new HashMap<>(){
        {
            put("default", 1.5f);
            put("min", 1f);
            put("max", 2f);
        }
    };

    public nmos(Component comp, HashMap<String , Float> ml)
    {
        super(comp);
        this.ml = ml;
    }

    public static HashMap<String, String> getNetlist(JsonObject netlistJson)
    {
        HashMap<String, String> netlist = new HashMap<>();

        netlist.put("drain", netlistJson.get("drain").getAsString());
        netlist.put("gate", netlistJson.get("gate").getAsString());
        netlist.put("source", netlistJson.get("source").getAsString());

        return netlist;
    }

    public static HashMap<String, Float > getM1(JsonObject m1Json)
    {
        HashMap<String, Float> m1 = new HashMap<>();

        // WARNING: Note that the json file has a type deafult -> default
        m1.put("deafult", m1Json.get("deafult").getAsFloat());
        m1.put("min", m1Json.get("min").getAsFloat());
        m1.put("max", m1Json.get("max").getAsFloat());
        return m1;
    }
}