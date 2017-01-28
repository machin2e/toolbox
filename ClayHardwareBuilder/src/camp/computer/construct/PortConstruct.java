package camp.computer.construct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import camp.computer.data.format.configuration.Configuration;
import camp.computer.data.format.configuration.Variable;

public class PortConstruct extends Construct {

    // Revision 1:
    // Example, single variable:
    // - To create the set of values defining a variable's domain:
    // [STALE] public Variable modeVariable = new Variable("variable-title", new ValueSet("state-1", "state-2", "state-3")); // NOTE: Stored in Manager so they can be shared? Or in container as static, so per-container-class scope/namespace?
    // public ValueSet modeStates = new ValueSet(State("state-1"), State("state-2"), State("state-3"), ...); // NOTE: Stored in Manager so they can be shared? Or in container as static, so per-container-class scope/namespace?
    // To assign state to variable:
    // public State modeState = new State(modeStates.values.get("state-1"));
    // public VariableMap myFirstConfiguration = new VariableMap(modeStates.get("state-1", "state-3"), directionVariable.values.get("state-2"), voltageVaraible.values.get("state-1", "state-2", "state-4")); // NOTE:

    // ---

    // Revision 2: Optimization
    // ValueSet<String> modeValueSet = new ValueSet<>(new State(null), new State("none"), new State("digital"), new State("analog"), ...);
    // ValueSet<String> directionValueSet = new ValueSet<>(new State(null), new State("none"), new State("digital"), new State("analog"), ...);
    // ValueSet<String> voltageValueSet = new ValueSet<>(new State(null), new State("none"), new State("digital"), new State("analog"), ...);
    // Variable modeVariable = new Variable("mode", modeValueSet);
    // Variable directionVariable = new Variable("direction", directionValueSet);
    // Variable voltageVariable = new Variable("voltage", voltageValueSet);
    // VariableMap configurationA = new VariableMap(modeVariable.values.get("", ""), directionVariable.values.get("", "", ""), voltageVariable.values.get("", "", "", ""));
    // VariableMap configurationB = new VariableMap(modeVariable.values.get("", ""), directionVariable.values.get("", "", ""), voltageVariable.values.get("", "", "", ""));
    // VariableMap configurationC = new VariableMap(modeVariable.values.get("", ""), directionVariable.values.get("", "", ""), voltageVariable.values.get("", "", "", ""));
    // ConfigurationSet configurations = new ConfigurationSet(configurationA, configurationB, configurationC); // Tuple<VariableMap> configurationList;

    // Revision 3:
    // Variable modeVariable = new Variable("mode", new ValueSet<String>(new State(null), new State("none"), new State("digital"), new State("analog"), ...));
    // Variable directionVariable = new Variable("direction", new ValueSet<String>(new State(null), new State("none"), new State("digital"), new State("analog"), ...));
    // Variable voltageVariable = new Variable("voltage", new ValueSet<String>(new State(null), new State("none"), new State("digital"), new State("analog"), ...));
    // VariableMap configurationA = new VariableMap(modeVariable.values.get("", ""), directionVariable.values.get("", "", ""), voltageVariable.values.get("", "", "", ""));
    // VariableMap configurationB = new VariableMap(modeVariable.values.get("", ""), directionVariable.values.get("", "", ""), voltageVariable.values.get("", "", "", ""));
    // VariableMap configurationC = new VariableMap(modeVariable.values.get("", ""), directionVariable.values.get("", "", ""), voltageVariable.values.get("", "", "", ""));
    // ConfigurationSet configurations = new ConfigurationSet(configurationA, configurationB, configurationC); // Tuple<VariableMap> configurationList;

    // Revision 4:
    // Variable modeVariable = new Variable("mode", new ValueSet<String>(new State(null), new State("none"), new State("digital"), new State("analog"), ...));
    // Variable directionVariable = new Variable("direction", new ValueSet<String>(new State(null), new State("none"), new State("digital"), new State("analog"), ...));
    // Variable voltageVariable = new Variable("voltage", new ValueSet<String>(new State(null), new State("none"), new State("digital"), new State("analog"), ...));
    // Configuration constraintA = new Configuration(modeVariable.values.get("", ""), directionVariable.values.get("", "", ""), voltageVariable.values.get("", "", "", ""));
    // Configuration constraintB = new Configuration(modeVariable.values.get("", ""), directionVariable.values.get("", "", ""), voltageVariable.values.get("", "", "", ""));
    // Configuration constraintC = new Configuration(modeVariable.values.get("", ""), directionVariable.values.get("", "", ""), voltageVariable.values.get("", "", "", ""));
    // VariableMap configurations = new VariableMap(constraintA, constraintB, constraintC); // Tuple<VariableMap> configurationList;
    // TODO: ConfigurationSet/Tuple consistentConfigurations = VariableMap.computeConfiguration(sourceConfiguration, targetConfiguration [, ...]); // Consistent configurations for the given ports, which can be used to initialize a PathConstruct.

    // <CONFIGURATION_SPACE>
    // TODO: Add Variables and set their States
    // configurations.configurations.get("variable-title").values.get("state-title")
//    public Tuple<PortConfigurationConstraint> portConfigurationConstraints = new ArrayList<>();
    // </CONFIGURATION_SPACE>

//    ValueSet<String> modeValueSet = new ValueSet<>(
//            new State(null),
//            new State("none"),
//            new State("digital"),
//            new State("analog"),
//            new State("power"), // Level (COMMON/0V, TTL/3.3V, CMOS/5V)
//
//            new State("pwm"), // Period (time unit); Duty-Cycle ("on" ratio per period)
//
//            new State("resistive_touch"),
//
//            // I2C Bus
//            new State("i2c(sda)"),
//            new State("i2c(scl)"),
//
//            // SPI Bus
//            new State("spi(sclk)"),
//            new State("spi(mosi)"),
//            new State("spi(miso)"),
//            new State("spi(ss)"),
//
//            // UART Bus
//            new State("uart(rx)"),
//            new State("uart(tx)"));
//
//    ValueSet<String> directionValueSet = new ValueSet<>(
//            new State(null),
//            new State("none"),
//            new State("input"),
//            new State("output"),
//            new State("bidirectional"));
//
//    ValueSet<String> voltageValueSet = new ValueSet<>(
//            new State(null),
//            new State("none"),
//            new State("ttl"), // 3.3V
//            new State("cmos"), // 5V
//            new State("common"));

//    Variable modeVariable = new Variable("mode", modeValueSet);
//    Variable directionVariable = new Variable("direction", directionValueSet);
//    Variable voltageVariable = new Variable("voltage", voltageValueSet); // 0V

    // TODO: Consider renaming VariableMap to State. VariableMap would be a set of configurations AND value assignments to the configurations.
    public HashMap<String, Variable> variables = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?

    public List<Configuration> configurations = new ArrayList<>();

    // Configuration constraintA = new Configuration(modeVariable.values.get("", ""), directionVariable.values.get("", "", ""), voltageVariable.values.get("", "", "", ""));

//    public void setupConfiguration() {
//
//        Variable modeVariable = new Variable("mode");
//        Variable directionVariable = new Variable("direction");
//        Variable voltageVariable = new Variable("voltage"); // 0V
//
//        // TODO: Consider renaming VariableMap to State. VariableMap would be a set of configurations AND value assignments to the configurations.
////        configurations = new VariableMap(modeVariable, directionVariable, voltageVariable);
//        configurations.put("mode", modeVariable);
//        configurations.put("direction", directionVariable);
//        configurations.put("voltage", voltageVariable);
//
//        // Constrains the port configurations
//        configurations.add(
//                new Configuration(
//                        new VariableValueSet("mode", new ValueSet("none")),
//                        new VariableValueSet("direction", new ValueSet("none")),
//                        new VariableValueSet("voltage", new ValueSet("none"))
//                )
//        );
//
//        configurations.add(
//                new Configuration(
//                        new VariableValueSet("mode", new ValueSet("digital")),
//                        new VariableValueSet("direction", new ValueSet("input", "output", "bidirectional")),
//                        new VariableValueSet("voltage", new ValueSet("ttl", "cmos"))
//                )
//        );
//
////        configurations.get("mode").value;
//
//    }

    // TODO: Support adding the "custom device" for adding and setting ports in real time (with configurations, selection from list of consistent port configurations, assignment of selected one; then start/track IASM).

    public PortConstruct() {
    }

}
