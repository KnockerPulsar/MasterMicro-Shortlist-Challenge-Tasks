import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

// Didn't know where to put this function in the already existing classes, so I just created a class for it.
public class Utils {
    static <T1, T2> HashMap<T1, T2> convertJSONtoHashMap(JsonObject jsonMap) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<T1, T2>>() {}.getType();
        return gson.fromJson(jsonMap, type);
    }
}
