import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TopologyAPITests {

    public static Topology testTopology = new Topology("top1", new ArrayList<Component>(Arrays.asList(
            new resistor(
                    new Component("resistor", "res1", new HashMap<>() {
                        {
                            put("t1", "vdd");
                            put("t2", "n1");
                        }
                    }
                    ),
                    new HashMap<>() {
                        {
                            put("default", 100.0);
                            put("min", 10.0);
                            put("max", 1000.0);
                        }
                    }
            )
            ,
            new nmos(
                    new Component("nmos", "m1", new HashMap<>() {
                        {
                            put("drain", "n1");
                            put("gate", "vin");
                            put("source", "vss");
                        }
                    }
                    ),
                    new HashMap<>() {
                        {
                            put("deafult", 1.5);
                            put("min", 1.0);
                            put("max", 2.0);
                        }
                    }
            )
    )));

    @BeforeEach
    void registerDefaultTopology() {
        if (!Topology.inMemory.containsKey(testTopology.id))
            Topology.inMemory.put(testTopology.id, testTopology);
    }

    @Test
    void ReadNonExistentJSON() {
        Result res = Topology.readJSON("src/test/resources/ne.json");
        assertFalse(res.success, "Should return false when reading a non-existent file");
    }

    @Test
    void ReadExistentJSON() {
        Result res = Topology.readJSON("src/test/resources/topology.json");
        assertEquals(res.returned, testTopology, "The read topology should be the same as testTopology. Make sure testTopology and the read topology are the same before debugging.");
    }

    @Test
    void writeNotInMemory() {
        Result res = Topology.writeJSON("non existent", "src/test/resources");
        assertFalse(res.success, "Should return false when trying to write a topology that's not in memory");
    }

    @Test
    void writeToInvalidPath() {
        Result res = Topology.writeJSON(testTopology.id, "src/test/invalidPath");
        assertFalse(res.success, "Should return false when reading from an invalid path");
    }

    @Test
    void writeValidTopology() {
        String temp = testTopology.id;
        testTopology.id = "validWriteCase";

        Topology.inMemory.put(testTopology.id, testTopology);

        Result res = Topology.writeJSON(testTopology.id, "src/test/resources");

        Topology.inMemory.remove(testTopology.id);
        testTopology.id = temp;
        assertTrue(res.success, "Should return true when reading a valid topology");
    }

    @Test
    void queryEmptyMemory() {
        Topology.inMemory.remove(testTopology.id);

        assertEquals(Topology.queryTopologies().size(), 0, "Size of list when no topologies are not in memory should be zero");

    }

    @Test
    void queryNonEmptyMemory() {
        Topology testTop2 = new Topology(testTopology);
        testTop2.id = "top2";
        testTop2.components.add(
                new nmos(
                        new Component(
                                "nmos",
                                "nmos2",
                                new HashMap<>() {{
                                    put("source", "k1");
                                    put("drain", "vcc");
                                    put("gate", "vin");
                                }}),
                        new HashMap<>() {{
                            put("deafult", 3.2);
                            put("min", 2.1);
                            put("max", 5.3);
                        }}
                )
        );
        Topology.inMemory.put(testTop2.id,  testTop2);
        assertEquals(Topology.queryTopologies().size(), 2, "Querying  a memory with 2 topologies should return exactly 2 topologies");
        Topology.inMemory.remove(testTop2.id);
    }

    @Test
    void deleteNonExistentTopology() {
        assertFalse(Topology.deleteTopology("ne").success, "Deleting a non-existent topology should return false");
    }

    @Test
    void deleteExistentTopology() {
        assertTrue(Topology.deleteTopology(testTopology.id).success, "Deleting a topology that's in memory should return true");
    }

    @Test
    void queryForNonExistentDevices() {
        assertNull(Topology.queryDevices("ne"), "Should return null when querying for the devices of a non-existent topology");
    }

    @Test
    void queryForExistentDevices() {
        assertEquals(Topology.queryDevices(testTopology.id), testTopology.components, "Querying for the devices of a topology in memory should return the ArrayList of components it has");
    }

    @Test
    void queryNodeNonExistentNode() {
        assertNull(Topology.queryDevicesWithNetlistNode("non existent", testTopology.id), "Querying for the devices connected to a non-existent node should return null");
    }

    @Test
    void queryNodeExistentNode() {
        String nodeId = "n1";
        assertEquals(Topology.queryDevicesWithNetlistNode(testTopology.id, nodeId ), testTopology.connectedToNode.get(("n1")), "Querying for the devices connected to an existent node should return an ArrayList identical to that of the object");
    }

    @Test
    void queryNodeNoneExistentTopology() {
        String nodeId = "n1";
        assertNotNull(Topology.queryDevicesWithNetlistNode(testTopology.id, nodeId), "Querying for the devices connected to an existent node should return a non null result");
    }
}