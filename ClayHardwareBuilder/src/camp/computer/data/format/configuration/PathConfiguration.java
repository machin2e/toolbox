package camp.computer.data.format.configuration;

import java.util.HashMap;

public class PathConfiguration {

    public HashMap<String, Constraint> variables = new HashMap<>();

    public PathConfiguration(Constraint sourceConfiguration, Constraint targetConfiguration) {

        this.variables.put("source", sourceConfiguration);
        this.variables.put("target", targetConfiguration);

    }

}
