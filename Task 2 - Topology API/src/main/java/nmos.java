import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Objects;

// Extension of the Component class to handle NMOS transistor values (min, max, and deafult)
// Yes, "deafult" is a type, I just wanted to stick to the given material.
public class nmos extends Component {

    // To set the name as it was in the read JSON
    @SerializedName(value = "m(l)")
    public HashMap<String, Double> ml;

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

}