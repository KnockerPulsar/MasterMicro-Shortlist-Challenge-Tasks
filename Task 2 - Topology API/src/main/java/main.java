import com.google.gson.*;
import java.util.ArrayList;

// Consider this a scratchpad for experimentation/debugging
public class main {
    public static void main(String[] args) {
        Gson gson = new Gson();

        // Read
        Result top = Topology.readJSON("src/main/resources/topology.json");

        // Write
        if(top.success) {
            Result write = Topology.writeJSON(top.returned.id, "src/java/resources/");
        }

        // Check loaded topologies
        ArrayList<Topology> tops = Topology.queryTopologies();

        // Check what components are in a topology
        ArrayList<Component> comps = Topology.queryDevices(top.returned.id);

        // Check what components/devices are connected to a node
        ArrayList<Component> n1Comps = Topology.queryDevicesWithNetlistNode(top.returned.id, "n1");

        // Remove a topology from memory
        Result top2 = Topology.deleteTopology(top.returned.id);
    }
}
