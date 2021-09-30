import java.util.HashMap;
import java.util.Objects;

// Extension of the Component class to support resistor resistances (min, max, and default)
public class resistor extends Component
{
    public HashMap<String, Double > resistance;

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
}
