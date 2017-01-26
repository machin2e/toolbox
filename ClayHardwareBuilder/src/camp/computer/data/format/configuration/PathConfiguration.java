package camp.computer.data.format.configuration;

import java.util.HashMap;

public class PathConfiguration {

    public HashMap<String, PortConfigurationConstraint> variables = new HashMap<>();

    public PathConfiguration(PortConfigurationConstraint sourceConfiguration, PortConfigurationConstraint targetConfiguration) {

        this.variables.put("source", sourceConfiguration);
        this.variables.put("target", targetConfiguration);

    }

}
