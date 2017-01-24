package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

import camp.computer.data.format.configuration.ConfigurationConstraint;

public class PortConstruct extends Construct {

    // <CONFIGURATION_SPACE>
    // TODO: Add Variables and set their States
    // configuration.variables.get("variable-title").states.get("state-title")
    public List<ConfigurationConstraint> configurationConstraints = new ArrayList<>();
    // </CONFIGURATION_SPACE>

    // Revision 1:
    // Example, single variable:
    // - To create the set of states defining a variable's domain:
    // [STALE] public Variable modeVariable = new Variable("variable-title", new StateSet("state-1", "state-2", "state-3")); // NOTE: Stored in Manager so they can be shared? Or in container as static, so per-container-class scope/namespace?
    // public StateSet modeStates = new StateSet(State("state-1"), State("state-2"), State("state-3"), ...); // NOTE: Stored in Manager so they can be shared? Or in container as static, so per-container-class scope/namespace?
    // To assign state to variable:
    // public State modeState = new State(modeStates.states.get("state-1"));
    // public Configuration myFirstConfiguration = new Configuration(modeStates.get("state-1", "state-3"), directionVariable.states.get("state-2"), voltageVaraible.states.get("state-1", "state-2", "state-4")); // NOTE:

    // ---

    // Revision 2: Optimization
    // StateSet<String> modeStateSet = new StateSet<>(new State(null), new State("none"), new State("digital"), new State("analog"), ...);
    // StateSet<String> directionStateSet = new StateSet<>(new State(null), new State("none"), new State("digital"), new State("analog"), ...);
    // StateSet<String> voltageStateSet = new StateSet<>(new State(null), new State("none"), new State("digital"), new State("analog"), ...);
    // Variable modeVariable = new Variable("mode", modeStateSet);
    // Variable directionVariable = new Variable("direction", directionStateSet);
    // Variable voltageVariable = new Variable("voltage", voltageStateSet);
    // Configuration configurationA = new Configuration(modeVariable.states.get("", ""), directionVariable.states.get("", "", ""), voltageVariable.states.get("", "", "", ""));
    // Configuration configurationB = new Configuration(modeVariable.states.get("", ""), directionVariable.states.get("", "", ""), voltageVariable.states.get("", "", "", ""));
    // Configuration configurationC = new Configuration(modeVariable.states.get("", ""), directionVariable.states.get("", "", ""), voltageVariable.states.get("", "", "", ""));
    // ConfigurationSet configurations = new ConfigurationSet(configurationA, configurationB, configurationC); // List<Configuration> configurationList;

    // Revision 3:
    // Variable modeVariable = new Variable("mode", new StateSet<String>(new State(null), new State("none"), new State("digital"), new State("analog"), ...));
    // Variable directionVariable = new Variable("direction", new StateSet<String>(new State(null), new State("none"), new State("digital"), new State("analog"), ...));
    // Variable voltageVariable = new Variable("voltage", new StateSet<String>(new State(null), new State("none"), new State("digital"), new State("analog"), ...));
    // Configuration configurationA = new Configuration(modeVariable.states.get("", ""), directionVariable.states.get("", "", ""), voltageVariable.states.get("", "", "", ""));
    // Configuration configurationB = new Configuration(modeVariable.states.get("", ""), directionVariable.states.get("", "", ""), voltageVariable.states.get("", "", "", ""));
    // Configuration configurationC = new Configuration(modeVariable.states.get("", ""), directionVariable.states.get("", "", ""), voltageVariable.states.get("", "", "", ""));
    // ConfigurationSet configurations = new ConfigurationSet(configurationA, configurationB, configurationC); // List<Configuration> configurationList;

    // Revision 4:
    // Variable modeVariable = new Variable("mode", new StateSet<String>(new State(null), new State("none"), new State("digital"), new State("analog"), ...));
    // Variable directionVariable = new Variable("direction", new StateSet<String>(new State(null), new State("none"), new State("digital"), new State("analog"), ...));
    // Variable voltageVariable = new Variable("voltage", new StateSet<String>(new State(null), new State("none"), new State("digital"), new State("analog"), ...));
    // Constraint constraintA = new Constraint(modeVariable.states.get("", ""), directionVariable.states.get("", "", ""), voltageVariable.states.get("", "", "", ""));
    // Constraint constraintB = new Constraint(modeVariable.states.get("", ""), directionVariable.states.get("", "", ""), voltageVariable.states.get("", "", "", ""));
    // Constraint constraintC = new Constraint(modeVariable.states.get("", ""), directionVariable.states.get("", "", ""), voltageVariable.states.get("", "", "", ""));
    // Configuration configuration = new Configuration(constraintA, constraintB, constraintC); // List<Configuration> configurationList;
    // TODO: ConfigurationSet/List consistentConfigurations = Configuration.computeConfiguration(sourceConfiguration, targetConfiguration [, ...]); // Consistent configurations for the given ports, which can be used to initialize a PathConstruct.

    // TODO: Support adding the "custom device" for adding and setting ports in real time (with constraints, selection from list of consistent port configurations, assignment of selected one; then start/track IASM).

    // <STATE>
    public ConfigurationConstraint.Mode mode = null;
    public ConfigurationConstraint.Direction direction = null;
    public ConfigurationConstraint.Voltage voltage = null;
    // </STATE>

    public PortConstruct() {
    }

}
