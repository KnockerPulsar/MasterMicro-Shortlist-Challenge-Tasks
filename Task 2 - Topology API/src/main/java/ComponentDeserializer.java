import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;

public class ComponentDeserializer implements JsonDeserializer {

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        switch (type) {
            case "resistor" -> {
                HashMap<String, Float> resistance =
                        resistor.getResistance(jsonObject.get("resistance").getAsJsonObject());

                HashMap<String, String> netlist =
                        resistor.getNetlist(jsonObject.get("netlist").getAsJsonObject());

                return new resistor(
                        new Component(
                                type,
                                jsonObject.get("id").getAsString(),
                                netlist
                        ),
                        resistance
                );
            }
            case "nmos" -> {
                HashMap<String, Float> m1 =
                        nmos.getM1(jsonObject.get("m(l)").getAsJsonObject());

                HashMap<String, String> netlist =
                        nmos.getNetlist(jsonObject.get("netlist").getAsJsonObject());

                return new nmos(
                        new Component(
                                type,
                                jsonObject.get("id").getAsString(),
                                netlist
                        ),
                        m1
                );
            }
            default -> {
                return null;
            }
        }
    }
}



