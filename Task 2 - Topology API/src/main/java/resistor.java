import java.util.HashMap;
import com.google.gson.JsonObject;

public class resistor extends Component
{
    public HashMap<String, Float > resistance = new HashMap<>(){
        {
            put("default", 100f);
            put("min", 10f);
            put("max", 1000f);
        }
    };

    public resistor(Component comp, HashMap<String, Float> resistance)
    {
        super(comp);
        this.resistance=resistance;
    }

    public static HashMap<String, String> getNetlist(JsonObject netlistJson)
    {
        HashMap<String, String> netlist = new HashMap<>();
        netlist.put("t1", netlistJson.get("t1").getAsString());
        netlist.put("t2", netlistJson.get("t2").getAsString());
        return netlist;
    }

    public static HashMap<String, Float> getResistance(JsonObject resistanceJson)
    {
        HashMap<String, Float> resistance = new HashMap<>();
        resistance.put("default", resistanceJson.get("default").getAsFloat());
        resistance.put("min", resistanceJson.get("min").getAsFloat());
        resistance.put("max", resistanceJson.get("max").getAsFloat());
        return resistance;
    }
}
