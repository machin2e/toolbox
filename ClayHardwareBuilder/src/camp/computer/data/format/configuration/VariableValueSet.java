package camp.computer.data.format.configuration;

/**
 * Constrains the values (or values) that can be assigned to the variable uniquely identified by
 * {@code title} to the values (or values) specified in {@code values}.
 * <p>
 * A {@code VariableValueSet} specifies a set of values that can be assigned to the variable
 * {@code title}.
 */
public class VariableValueSet {

    /**
     * {@code title} is a {@code String} that uniquely identifies the {@code Variable} in the
     * containing {@code VariableSet}. <em>in the
     * namespace</em>.
     */
    public String title = null; // e.g., mode; direction; voltage

    /**
     * The {@code ValueSet} stores the set of {@code State}s that can be assigned to the
     * {@code Variable}. In the parlance of the <em>Constraint Satisfaction Problem</em>, the
     * {@code ValueSet} defines the domain of the {@code Variable}.
     */
    public ValueSet values = new ValueSet(); // TODO: null

    public VariableValueSet(String variableTitle, ValueSet values) {

        this.title = variableTitle;
        this.values = values;

    }

}
