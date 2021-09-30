import java.util.HashMap;
import java.util.Objects;

// Base class for components, any component should extend this.
public class Component {
    String type, id;

    // We can either have this in here but, it will mess up the deserialization order
    // Or we can have it in each child class in the proper place, but that would be repeating code.
    // If there are any components without a netlist, the former approach might be better then.
    public HashMap<String, String> netlist;


    Component(String type, String id, HashMap<String, String> netlist) {
        this.type = type;
        this.id = id;
        this.netlist = netlist;
    }

    // Copy constructor
    Component(Component comp) {
        this.type = comp.type;
        this.id = comp.id;
        this.netlist = comp.netlist;
    }

    // Comparison function for easy comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(type, component.type) && Objects.equals(id, component.id) && netlist.equals(component.netlist);
    }
}

