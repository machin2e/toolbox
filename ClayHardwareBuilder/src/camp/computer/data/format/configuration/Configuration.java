package camp.computer.data.format.configuration;

import java.util.HashMap;

public class Configuration {

    public HashMap<String, Variable<?>> variables = new HashMap<>();

    // TODO?: public Configuration(StateSet<?>... stateSubsets) {
    public Configuration(Variable<?>... variables) {

        for (int variableIndex = 0; variableIndex < variables.length; variableIndex++) {

            this.variables.put(variables[variableIndex].title, variables[variableIndex]);

        }

//        this.variables.put("source", sourceConfiguration);
//        this.variables.put("target", targetConfiguration);

    }

}
