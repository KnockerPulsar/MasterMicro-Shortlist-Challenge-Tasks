import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import jdk.jshell.spi.ExecutionControl;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Topology {
    // Note that GSON ignores static fields during serialization and deserialization

    // Where JSON files will be written.
    public static String writeDir = "/src/main/resources/";

    public static HashMap<String, Component> netLists = new HashMap<>();

    // Just a shortcut to (Topology)null since IDEA likes to complain
    public static Topology empty = null;

    // Stores all the loaded topologies
    private static HashMap<String, Topology> inMemory = new HashMap<>();

    // Variables exposed to GSON
    @Expose String id;
    @Expose Component[] components;

    // Not exposed
    HashMap<String, ArrayList<Component>> connectedToNode;

    // fileName should be relative
    // Takes the file name to be read and loads the topology into an object
    // Registers the object if it's not memory already.
    // Can also overwrite the object if it's already in memory but that's risky.
    // It really depends on how the rest of the program works.
    // Worst case scenario, it overwrites the object in memory modifying every instance of this topology.
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
                return new Result(false, Topology.empty);
            }

            return new Result(true, top);
        } else return new Result(false, Topology.empty);
    }

    // Takes the ID of the topology we want to write
    // Writes a file with the same name as that id in json format
    // Can be easily modified to accept a string as the file's name, but the id works fine.
    static Result writeJSON(String topologyID) {
        if (inMemory.containsKey(topologyID)) {

            Topology top = Topology.inMemory.get(topologyID);
            File file;
            // write topology into JSON
            // Note that this overwrites the file.
            // This isn't a major issue assuming each topology is uniquely identified by its id
            try {

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                File temp = new File("");
                file = new File(temp.getAbsolutePath() + Topology.writeDir, topologyID+".json");

                if(!file.exists())
                    file.createNewFile();

                FileWriter writer = new FileWriter(file);
                gson.toJson(top, writer);
                writer.close();
                return new Result(true, top, file.getAbsolutePath() );

            } catch (IOException e) {
                e.printStackTrace();
                return new Result(false, Topology.empty);
            }

        } else return new Result(false, Topology.empty);
    }

    // Converts the values of the `inMemory` map to an ArrayList
    // Can return the whole hashmap if needed.
    static ArrayList<Topology> queryTopologies() {
        return new ArrayList<Topology>(inMemory.values());
    }

    // Removes the topology from `inMemory` and returns it if needed.
    static Result deleteTopology(String topologyID) {
        if (inMemory.containsKey(topologyID)) {
            return new Result(true, inMemory.remove(topologyID));
        } else return new Result(false, Topology.empty);
    }

    // Checks if the topology is in memory and returns its components as an ArrayList
    static ArrayList<Component> queryDevices(String topologyID) {
        if (inMemory.containsKey(topologyID)) {
            return new
                    ArrayList<Component>(
                    Arrays.asList(inMemory.get(topologyID).components)
            );
        } else return null;
    }

    // Builds the node-component list if not built and returns it
    static ArrayList<Component> queryDevicesWithNetlistNode(String topologyID, String netlistNodeID) {
        if (inMemory.containsKey(topologyID)) {
            Topology top = inMemory.get(topologyID);
            top.BuildConnectedToNode();
            return top.connectedToNode.get(netlistNodeID);
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
                                new ArrayList<Component>(Arrays.asList(comp))
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
