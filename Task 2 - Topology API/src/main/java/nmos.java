import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.HashMap;
import java.util.Objects;

public class nmos extends Component {

    // To set the name as it was in the read JSON
    @SerializedName(value = "m(l)")
    public HashMap<String, Double> ml = new HashMap<>();

    public nmos(Component comp, HashMap<String, Double> ml) {
        super(comp);
        this.ml = ml;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        nmos nmos = (nmos) o;
        return ml.equals(nmos.ml);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ml);
    }
}