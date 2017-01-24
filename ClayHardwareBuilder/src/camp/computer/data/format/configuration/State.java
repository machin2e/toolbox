package camp.computer.data.format.configuration;

/**
 * An {@code VariableValueSet} stores a list of title states for a specified title identified by its unique label.
 */
public class State<T> {

    public T value = null; // e.g., for T as String: mode; direction; voltage

    public State(T value) {

        this.value = value;

    }

}
