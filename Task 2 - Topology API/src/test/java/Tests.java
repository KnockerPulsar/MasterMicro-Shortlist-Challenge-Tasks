import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class TopologyAPITests {
    @Test
    void ReadNonExistentJSON() {
        Result res = Topology.readJSON("src/test/resources/ne.json");
        Result expected = new Result(false, null, null);
        assertEquals(res, expected);
    }

    @Test
    void ReadExistentJSON() {
        Result res = Topology.readJSON("src/test/resources/topology.json");

        Component[] comps = {
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
        };
        Topology top = new Topology("top1", comps);

        assertEquals(res.returned, top);
    }
}