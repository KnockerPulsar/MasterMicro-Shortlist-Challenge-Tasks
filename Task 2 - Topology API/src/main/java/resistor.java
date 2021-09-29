import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class resistor extends Component
{
    public HashMap<String, Double > resistance = new HashMap<>();

    public resistor(Component comp, HashMap<String, Double> resistance)
    {
        super(comp);
        this.resistance=resistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        resistor resistor = (resistor) o;
        return resistance.equals(resistor.resistance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), resistance);
    }
}
