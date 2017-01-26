package camp.computer.data.format.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@code VariableValueSet} stores a list of title values for a specified title identified by its unique label.
 */
public class ValueSet {

    // TODO: ValueSet<T>

    public List<String> values = new ArrayList<>();

    public ValueSet(String... values) {

        for (int valueIndex = 0; valueIndex < values.length; valueIndex++) {
            if (!this.values.contains(values[valueIndex])) {
                this.values.add(values[valueIndex]);
            }
        }

    }

    // NOTE: This is not needed if values are strings and not a separate class.
//    public ValueSet subset(String... stateTitles) {
//        ValueSet<T> subset = new ValueSet<>();
//        for (int i = 0; i < stateTitles.length; i++) {
//            for (int j = 0; j < values.size(); j++) {
//                if (stateTitles[i].equals(values.get(i))) {
//                    subset.values.add(new String(values.get(i)));
//                }
//            }
//        }
//        return subset;
//    }

}
