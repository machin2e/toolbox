package camp.computer.data.format.configuration;

/**
 * An {@code VariableValueSet} stores a list of title states for a specified title identified by its unique label.
 */
public class Variable<T> {

    /**
     * {@code title} is a {@code String} that uniquely identifies the {@code Variable} in the
     * containing {@code Configuration}. <em>in the
     * namespace</em>.
     */
    public String title = null; // e.g., mode; direction; voltage

    /**
     * The {@code StateSet} stores the set of {@code State}s that can be assigned to the
     * {@code Variable}. In the parlance of the <em>Constraint Satisfaction Problem</em>, the
     * {@code StateSet} defines the domain of the {@code Variable}.
     */
    public StateSet<T> states = new StateSet<>();

    /**
     * {@code value} references a {@code State} in the {@code states} {@code StateSet}.
     * <p>
     * {@code value} is initialized to {@code null}, which indicates that it is
     * <em>unassigned</em>, or it has not yet been assigned to a {@code State} in {@code states}.
     */
    public State<T> value = null;

    public Variable(String title, StateSet<T> states) {

        this.title = title;
        this.states = states;

    }

}
