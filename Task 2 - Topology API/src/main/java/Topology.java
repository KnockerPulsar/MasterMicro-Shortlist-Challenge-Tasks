import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Topology {
    public static HashMap<String, Topology> inMemory = new HashMap<>();
    public static  HashMap<String, Component> netLists = new HashMap<>();
    String id;
    Component[] components;

    static Result readJSON(String fileName) {
        Boolean fileExists = true;
        if (fileExists) {
            Topology top = new Topology();
            // read json into top
            return new Result(true, top);
        } else return new Result(false, null);
    }

    static Result writeJSON(String topologyID) {
        if (inMemory.containsKey(topologyID)) {
            Topology top = new Topology();
            // write topology into JSON
            return new Result(true, top);
        } else return new Result(false, null);
    }

    static ArrayList<Topology> queryTopologies() {
        return new ArrayList<Topology>(inMemory.values());
    }

    static Result deleteTopology(String topologyID) {
        if (inMemory.containsKey(topologyID)) {
            return new Result(true, inMemory.remove(topologyID));
        } else return new Result(false, null);
    }

    static ArrayList<Component> queryDevices(String topologyID) {
        if (inMemory.containsKey(topologyID)) {
            return new ArrayList<Component>(Arrays.asList(inMemory.get(topologyID).components));
        } else return null;
    }

    static ArrayList<Component> queryDevicesWithNetlistNode(String topologyID, String netlistNodeID) throws ExecutionControl.NotImplementedException {
        // No idea what to do here...
        // The examples don't show that netlists have an id...
        throw new ExecutionControl.NotImplementedException("AAA");
        return null;
    }
}
