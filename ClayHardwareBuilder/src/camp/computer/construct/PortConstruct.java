package camp.computer.construct;

import camp.computer.data.format.configuration.PortConfigurationConstraint;

import java.util.ArrayList;
import java.util.List;

public class PortConstruct extends Construct {

    public List<PortConfigurationConstraint> portConfigurationConstraints = new ArrayList<>();

    // <REPLACE>
    public PortConfigurationConstraint.Mode mode = null;
    public PortConfigurationConstraint.Direction direction = null;
    public PortConfigurationConstraint.Voltage voltage = null;
    // </REPLACE>

    public PortConstruct() {
    }

}
