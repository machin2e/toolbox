package camp.computer.data.format.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Constrains the values (or values) that can be assigned <em>simultaneously</em> to each of the
 * variables <em>in combination with each other</em> uniquely identified by each
 * {@code variableConstraint[i].title} to the values (or values) specified in {@code variableConstraint[i].values}.
 * <p>
 * Therefore {@code Constraint} specifies a constraint on the valid mutual state assignments among
 * the specified variables.
 * <p>
 * In other words, this constrains the set of valid <em>combinitoric value permutations</em> that
 * can be assigned to variables at the same time.
 */
public class Constraint {

    // [ (Variable, CompatibleStateSubset), (Variable, CompatibleStateSubset), ..., (Variable, CompatibleStateSubset) ]

    //    public List<Variable<?>> stateSubsets = null; // (Variable, CompatibleStateSubset) pairs
    public List<VariableValueSet> variableValueSets = new ArrayList<>();

    // TODO: PortConfigurationConstraint(State... values)/(variable.values.get(<title>), ...)
    //public Constraint(Mode mode, ValueSet<Direction> directions, ValueSet<Voltage> voltages) {
//    public Constraint(ValueSet<?>... stateSubsets) {
    public Constraint(VariableValueSet... variableValueSets) {

        for (int variableIndex = 0; variableIndex < variableValueSets.length; variableIndex++) {
            this.variableValueSets.add(variableValueSets[variableIndex]);
        }

    }

    public Constraint(List<VariableValueSet> variableValueSets) {

        for (int variableIndex = 0; variableIndex < variableValueSets.size(); variableIndex++) {
            this.variableValueSets.add(variableValueSets.get(variableIndex));
        }

    }

    public static ValueSet getValues(Constraint constraint, String variableTitle) {

        for (int i = 0; i < constraint.variableValueSets.size(); i++) {
            if (constraint.variableValueSets.get(i).title.equals(variableTitle)) {
                return constraint.variableValueSets.get(i).values;
            }
        }

        return null;
    }

    public ValueSet getValues(String variableTitle) {
        for (int i = 0; i < variableValueSets.size(); i++) {
            if (variableValueSets.get(i).title.equals(variableTitle)) {
                return variableValueSets.get(i).values;
            }
        }

        return null;
    }

    // TODO: discoverCompatiblePortSet(<device-a>, <device-b>)

    // TODO: computeCompatibleConfigurations/computeCompatibleConfigurationSets(<device-a>, <device-b>)

    // TODO: rankCompatibleConfigurations(<compatible-variables-list>)

}
