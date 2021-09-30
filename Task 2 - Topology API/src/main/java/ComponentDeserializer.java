import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;

// Might be better if the field values are not hardcoded?
// Perhaps in a real production environment, you'd read those from a config file.
// https://howtodoinjava.com/gson/custom-serialization-deserialization/
public class ComponentDeserializer implements JsonDeserializer {

    @Override
    // Gets an object of type `Component` then reads the data depending on which child class it is.
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        HashMap<String, String> netlist = Utils.convertJSONtoHashMap(jsonObject.get("netlist").getAsJsonObject());

        switch (type) {
            case "resistor" -> {
                HashMap<String, Double> resistance =
                        Utils.convertJSONtoHashMap(jsonObject.get("resistance").getAsJsonObject());

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
                HashMap<String, Double> m1 =
                        Utils.convertJSONtoHashMap(jsonObject.get("m(l)").getAsJsonObject());

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



