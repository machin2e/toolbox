package camp.computer.data.format.configuration;

import java.util.HashMap;

public class PathConfiguration {

    public HashMap<String, ConfigurationConstraint> variables = new HashMap<>();

    public PathConfiguration(ConfigurationConstraint sourceConfiguration, ConfigurationConstraint targetConfiguration) {

        this.variables.put("source", sourceConfiguration);
        this.variables.put("target", targetConfiguration);

    }

}
