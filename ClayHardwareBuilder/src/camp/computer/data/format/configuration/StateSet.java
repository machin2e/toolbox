package camp.computer.data.format.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@code VariableValueSet} stores a list of title states for a specified title identified by its unique label.
 */
public class StateSet<T> {

    // TODO: Store reference to containing namespace/scope.

    public List<State<T>> states = new ArrayList<>();

    public StateSet(State<T>... states) {

        for (int stateIndex = 0; stateIndex < states.length; stateIndex++) {
            if (!this.states.contains(states[stateIndex])) {
                this.states.add(states[stateIndex]);
            }
        }

    }

}
