package camp.computer.data.format.configuration;

import camp.computer.construct.PortConstruct;

public class PathConfiguration {

    public PortConfigurationConstraint sourceConfiguration = null;
    public PortConfigurationConstraint targetConfiguration = null;

//    public PortConstruct sourcePortConstruct = null;
//
//    public PortConstruct targetPortConstruct = null;
//
//    public PortConfigurationConstraint.Mode mode = null;
//
//    /**
//     * {@code directions} is set to {@code null} when it is not applicable to the {@code PortConfigurationConstraint}. This is
//     * distinct from creating a {@code ValueSet} with no elements (i.e., an empty set).
//     */
//    public ValueSet<PortConfigurationConstraint.Direction> directions = null;
//
//
//    /**
//     * {@code voltages} is set to {@code null} when assigning the voltage is not applicable (like with
//     * {@code directions}). This is distinct from creating a {@code ValueSet} with no elements (i.e., an empty set).
//     */
//    public ValueSet<PortConfigurationConstraint.Voltage> voltages = null;

    public PathConfiguration(PortConfigurationConstraint sourceConfiguration, PortConfigurationConstraint targetConfiguration) {

        this.sourceConfiguration = sourceConfiguration;
        this.targetConfiguration = targetConfiguration;

        /*
        this.sourcePortConstruct = sourcePortConstruct;

        this.targetPortConstruct = targetPortConstruct;

        this.mode = mode;

        this.directions = directions;

        this.voltages = voltages;
        */

    }

}
