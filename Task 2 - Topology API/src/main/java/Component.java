import java.util.HashMap;
import java.util.Hashtable;

public class Component {
    public HashMap<String, String> netlist = new HashMap<>();
    String type, id;

    Component(String type, String id, HashMap<String, String> netlist) {
        this.type = type;
        this.id = id;
        this.netlist = netlist;
    }

    Component(Component comp) {
        this.type = comp.type;
        this.id = comp.id;
        this.netlist = comp.netlist;
    }
}

