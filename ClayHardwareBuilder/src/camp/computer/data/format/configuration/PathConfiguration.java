package camp.computer.data.format.configuration;

import java.util.HashMap;

public class PathConfiguration {

    public HashMap<String, Constraint> constraints = new HashMap<>();

    public PathConfiguration(Constraint sourceConfiguration, Constraint targetConfiguration) {

        this.constraints.put("source", sourceConfiguration);
        this.constraints.put("target", targetConfiguration);

    }

}
