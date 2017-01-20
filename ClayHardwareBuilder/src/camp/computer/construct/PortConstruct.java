package camp.computer.construct;

import camp.computer.data.format.configuration.PortConfiguration;

import java.util.ArrayList;
import java.util.List;

public class PortConstruct extends Construct {

    // TODO: label/title (i.e., "VCC", "AREF", "GND", "A0", "Port 3", "3", etc.

    // TODO: List of (portConfiguration string, INPUT | OUTPUT | IO, 0V | 3.3V | 5V – or range) + path constraint/relation resolver/arithmetic

    // <configuration_label>; [<direction_scope>]; [<voltage_scope>];
    // Power, (Output), (0V, 3.3V, 5V), (Off, On)
    // Power, (Input), (0V), (Common)
    // Discrete, (Input, Output), (3.3V, 5V), (True, False)
    // Continuous, (Input, Output), (3.3V, 5V), (<function>)
    // PWM, (Input, Output), (3.3V, 5V), (Period; Duty Cycle)

    // TODO: Add supported portConfigurations for Clay 7 device (see above)
    // TODO: Add supported portConfigurations for IR rangefinder device
    // TODO: Add supported portConfigurations for Ultrasonic Rangefinder device

    public List<PortConfiguration> portConfigurations = new ArrayList<>();

    // <REPLACE>
    public PortConfiguration.Mode mode = null;
    public PortConfiguration.Direction direction = null;
    public PortConfiguration.Voltage voltage = null;
    // </REPLACE>

    public PortConstruct() {
    }

}
