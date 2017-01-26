package camp.computer.data.format.configuration;

import java.util.HashMap;

/**
 * A {code VariableSet} consists of a set of a set of {@code Variable}s and a set of
 * {@code Constraints} that define the possible {@code State} combinations that can be assigned
 * to the corresponding set of {@code Variables}.
 */
public class VariableSet {

    // TODO: state set title namespace (unique among state sets)
    // TODO: variable title namespace (unique among variables; variables are associated with a state set, which may be a subset derived from another set)

    private HashMap<String, Variable> variables = new HashMap<>();

    // TODO?: public VariableSet(ValueSet<?>... stateSubsets) {
    public VariableSet(Variable... variables) {

        for (int variableIndex = 0; variableIndex < variables.length; variableIndex++) {

            this.variables.put(variables[variableIndex].title, variables[variableIndex]);

        }

    }

    public Variable get(String title) {
        return variables.get(title);
    }

    public Variable put(String title, Variable variable) {
        return this.variables.put(title, variable);
    }

//    public static List<String> getVariableTitles(VariableSet variables) {
//        return new ArrayList<>(variables.variables.keySet());
//    }

}
