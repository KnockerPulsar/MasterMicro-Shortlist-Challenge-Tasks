import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.*;
import java.util.*;

// Stores which components are connected and how they're connected.
public class Topology {
    // Note that GSON ignores static fields during serialization and deserialization

    // Stores all the loaded topologies
    // public to help with testing
    public static HashMap<String, Topology> inMemory = new HashMap<>();

    // Variables exposed to GSON
    @Expose
    String id;
    @Expose
    ArrayList<Component> components;

    // Not exposed, for internal use only
    HashMap<String, ArrayList<Component>> connectedToNode;

    Topology(String id, ArrayList<Component> components) {
        this.id = id;

        // Copy the given array, not point to the same data
        this.components = components;
    }

    public Topology(Topology other) {
        this.id = other.id;
        this.connectedToNode = other.connectedToNode;
        this.components = new ArrayList<>(other.components);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topology topology = (Topology) o;

        // Since connectedToNode is lazily built,
        // it might be a bad idea to include it in the equality logic.

        return Objects.equals(id, topology.id) && components.equals(topology.components);
    }

    /*
         the filepath proceeding `fileName` should be relative
         Takes the file name to be read and loads the topology into an object
         Registers the object if it's not memory already.

         Can also overwrite the object if it's already in memory but that's risky.
         It really depends on how the rest of the program works.
         Worst case scenario, it overwrites the object in memory modifying every instance of this topology.
    */
    static Result readJSON(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            // read json into top
            FileReader reader;
            Topology top;
            try {

                reader = new FileReader(file);

                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .registerTypeAdapter(Component.class, new ComponentDeserializer())
                        .create();

                top = gson.fromJson(reader, Topology.class);

                if (!inMemory.containsKey(top.id))
                    inMemory.put(top.id, top);
                else
                    System.out.println("Can update the topology corresponding to this ID if needed");

            } catch (FileNotFoundException e) {
                // If for some reason the first check fails.
                // Shouldn't really execute this branch unless something goes seriously wrong
                // since we already check existence before this block.
                return Result.Fail;
            }

            return new Result(true, top,file.getAbsolutePath()+top.id+".json");
        } else return Result.Fail;
    }

    // Takes the ID of the topology we want to write
    // Writes a file with the same name as that id in json format
    // Can be easily modified to accept a string as the file's name, but the id works fine.
    static Result writeJSON(String topologyID, String writeDir) {
        if (inMemory.containsKey(topologyID)) {

            Topology top = Topology.inMemory.get(topologyID);

            File file = new File(writeDir);

            // Check if directory exists
            if (!file.exists())
                return Result.Fail;

            // write topology into JSON
            // Note that this overwrites the file.
            // This isn't a major issue assuming each topology is uniquely identified by its id
            try {

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                file = new File(writeDir, topologyID + ".json");

                if (!file.exists())
                    file.createNewFile();

                FileWriter writer = new FileWriter(file);
                gson.toJson(top, writer);
                writer.close();

                return new Result(true, top, file.getAbsolutePath());

            } catch (IOException e) {
                // Should probably be logged into a file in a production build
                // Also, same as with readJSON, shouldn't come here unless something goes seriously wrong.
                e.printStackTrace();

                return Result.Fail;
            }

        } else return Result.Fail;
    }

    // Converts the values of the `inMemory` map to an ArrayList
    // Can return the whole hashmap if needed.
    static ArrayList<Topology> queryTopologies() {
        return new ArrayList<>(inMemory.values());
    }

    // Removes the topology from `inMemory` and returns it if needed.
    static Result deleteTopology(String topologyID) {
        if (inMemory.containsKey(topologyID)) {
            return new Result(true, inMemory.remove(topologyID), null);
        } else return Result.Fail;
    }

    // Checks if the topology is in memory and returns its components as an ArrayList
    static ArrayList<Component> queryDevices(String topologyID) {
        if (inMemory.containsKey(topologyID)) {
            return new ArrayList<>(inMemory.get(topologyID).components);
        } else return null;
    }

    // Builds the node-component list if not built and returns it
    static ArrayList<Component> queryDevicesWithNetlistNode(String topologyID, String netlistNodeID) {
        if (inMemory.containsKey(topologyID)) {
            Topology top = inMemory.get(topologyID);
            top.BuildConnectedToNode();

            if (top.connectedToNode.containsKey(netlistNodeID))
                return top.connectedToNode.get(netlistNodeID);
            else
                return null;
        } else return null;
    }

    /*
     Stores which devices are connected to which netlist node
     Builds the list the first time only.
     Returns true if the list is constructed and false if there's already a list
    */
    public boolean BuildConnectedToNode() {
        if (connectedToNode == null) {
            connectedToNode = new HashMap<>();

            /*
             * Loop over all components in the topology
             * Loop over all device terminals and the nodes connected to them in a specific component/device
             * Check if the terminal is already in the list. If not, adds the node and the component connected to it.
             * If the node is already in the map, just add the component
             */
            for (Component comp : components) {
                for (Map.Entry<String, String> terminalAndNode : comp.netlist.entrySet()) {
                    if (!connectedToNode.containsKey(terminalAndNode.getValue())) {
                        connectedToNode.put(
                                terminalAndNode.getValue(),
                                new ArrayList<>(Arrays.asList(comp))
                        );
                    } else
                        connectedToNode.get(terminalAndNode.getValue()).add(comp);
                }
            }
            return true;
        } else
            return false;
    }
}
