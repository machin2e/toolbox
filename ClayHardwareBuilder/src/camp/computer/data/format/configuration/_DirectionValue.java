package camp.computer.data.format.configuration;

import java.util.ArrayList;
import java.util.List;

public class _DirectionValue<T> {

    public List<T> values = new ArrayList<>();

    public _DirectionValue(T... values) {

        for (int valueIndex = 0; valueIndex < values.length; valueIndex++) {
            if (!this.values.contains(values[valueIndex])) {
                this.values.add(values[valueIndex]);
            }
        }

    }

}
