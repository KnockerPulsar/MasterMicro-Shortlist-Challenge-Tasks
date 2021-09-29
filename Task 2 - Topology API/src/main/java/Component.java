import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class Component {
    // We can either have this in here but it will screw up the deserialization order
    // Or we can have it in each child class in the proper place, but that would be repeating code.
    // If there are any components without a netlist, the former approach might be better then.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(type, component.type) && Objects.equals(id, component.id) && netlist.equals(component.netlist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, netlist);
    }
}

