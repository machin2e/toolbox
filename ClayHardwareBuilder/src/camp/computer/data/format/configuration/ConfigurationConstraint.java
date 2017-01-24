package camp.computer.data.format.configuration;

public class ConfigurationConstraint {
    // TODO: Consider renaming to ConfigurationConstraint for general use in constructs?

    // TODO: Current Mode: [ AC, DC ]

    // VariableStateSet = [ none, digital, analog, power, pwm, ... ]
    // Variable variable = new Variable(VariableState("digital"), VariableStateSet);

    // <VARIABLES>
    // TODO: Replace with Class + searchable manager
    // Per-Port Modes
    public enum Mode {

        NONE,
        DIGITAL,
        ANALOG,
        POWER, // Level (COMMON/0V, TTL/3.3V, CMOS/5V)

        PWM, // Period (time unit); Duty-Cycle ("on" ratio per period)

        RESISTIVE_TOUCH,

        // I2C Bus
        I2C_SDA,
        I2C_SCL,

        // SPI Bus
        SPI_SCLK,
        SPI_MOSI,
        SPI_MISO,
        SPI_SS,

        // UART Bus
        UART_RX,
        UART_TX

    }

    // TODO: Replace with Class + searchable manager
    public enum Direction {
        NONE,
        INPUT,
        OUTPUT,
        BIDIRECTIONAL // Supporting bidirectional configuration is different than supporting both the input and output configurations because in the latter case, only one can be specified.
    }

    // TODO: Replace with Class + searchable manager
    public enum Voltage {
        NONE,
        TTL, // 3.3V
        CMOS, // 5V
        COMMON // 0V
    }
    // </VARIABLES>

    // "generate iasm device a device b" (or ask/generate after adding path)

    // <CONFIGURATION_STATE_ASSIGNMENT_CONSTRAINTS>
    // TODO: public StateSet<Mode> modes = null;
    public Mode mode = null;

    /**
     * {@code directions} is set to {@code null} when it is not applicable to the {@code ConfigurationConstraint}. This is
     * distinct from creating a {@code StateSet} with no elements (i.e., an empty set).
     */
    public StateSet<Direction> directions = null;


    /**
     * {@code voltages} is set to {@code null} when assigning the voltage is not applicable (like with
     * {@code directions}). This is distinct from creating a {@code StateSet} with no elements (i.e., an empty set).
     */
    public StateSet<Voltage> voltages = null;
    // </CONFIGURATION_STATE_ASSIGNMENT_CONSTRAINTS>

    // TODO: Bus dependencies (device-device interface level)

    // TODO: ConfigurationConstraint(State... states)/(variable.states.get(<title>), ...)
    public ConfigurationConstraint(Mode mode, StateSet<Direction> directions, StateSet<Voltage> voltages) {

        this.mode = mode;
        this.directions = directions;
        this.voltages = voltages;

    }

    // TODO: discoverCompatiblePortSet(<device-a>, <device-b>)

    /**
     * Computes/resolves the pair of "minimal compatible" {@code ConfigurationConstraint}s
     * given a pair of two {@code ConfigurationConstraint}s. The resulting {@code ConfigurationConstraint}s
     * will remove states that cannot be assigned to variables in the {@code ConfigurationConstraint}.
     *
     * @param sourceConfiguration
     * @param targetConfiguration
     * @return
     */
    public static StateSet<ConfigurationConstraint> computeCompatibleConfigurationSet(ConfigurationConstraint sourceConfiguration, ConfigurationConstraint targetConfiguration) {

        // <VERBOSE_LOG>
        // Source Port
        boolean ENABLE_VERBOSE_OUTPUT = false;
        if (ENABLE_VERBOSE_OUTPUT) {

            System.out.print("? (source) " + sourceConfiguration.mode + ";");

            if (sourceConfiguration.directions == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < sourceConfiguration.directions.states.size(); i++) {
                    System.out.print("" + sourceConfiguration.directions.states.get(i));
                    if ((i + 1) < sourceConfiguration.directions.states.size()) {
                        System.out.print(",");
                    }
                }
            }
            System.out.print(";");

            if (sourceConfiguration.voltages == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < sourceConfiguration.voltages.states.size(); i++) {
                    System.out.print("" + sourceConfiguration.voltages.states.get(i));
                    if ((i + 1) < sourceConfiguration.voltages.states.size()) {
                        System.out.print(",");
                    }
                }
            }
            System.out.println();

            // Target Port(s)
            System.out.print("  (target) " + targetConfiguration.mode + ";");

            if (targetConfiguration.directions == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < targetConfiguration.directions.states.size(); i++) {
                    System.out.print("" + targetConfiguration.directions.states.get(i));
                    if ((i + 1) < targetConfiguration.directions.states.size()) {
                        System.out.print(",");
                    }
                }
            }
            System.out.print(";");

            if (targetConfiguration.voltages == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < targetConfiguration.voltages.states.size(); i++) {
                    System.out.print("" + targetConfiguration.voltages.states.get(i));
                    if ((i + 1) < targetConfiguration.voltages.states.size()) {
                        System.out.print(",");
                    }
                }
            }
        }
        // </VERBOSE_LOG>

//        ConfigurationConstraint compatibleSourceConfigurationConstraint = new ConfigurationConstraint(null, null, null); // populate with valid subset given configuration constraints
//        ConfigurationConstraint compatibleTargetConfigurationConstraint = new ConfigurationConstraint(null, null, null); // populate with valid subset given configuration constraints
        ConfigurationConstraint compatibleSourceConfigurationConstraint = new ConfigurationConstraint(null, new StateSet<>(), new StateSet<>()); // populate with valid subset given configuration constraints
        ConfigurationConstraint compatibleTargetConfigurationConstraint = new ConfigurationConstraint(null, new StateSet<>(), new StateSet<>()); // populate with valid subset given configuration constraints

        // <MODE_CONSTRAINT_CHECKS>
        if (sourceConfiguration.mode == targetConfiguration.mode) {
            compatibleSourceConfigurationConstraint.mode = sourceConfiguration.mode;
            compatibleTargetConfigurationConstraint.mode = targetConfiguration.mode;
        }

        // TODO: I2C, SPI, UART, etc.

        // </MODE_CONSTRAINT_CHECKS>

        // TODO: compute the "compatible intersection" configurations of the mode constraints of the two configurations
        // TODO: compute the "compatible intersection" configurations of the directions constraints of the two configurations
        // TODO: compute the "compatible intersection" configurations of the voltages constraints of the two configurations

        //---

        // CHECK FOR THESE:
        // x TODO: AddSourceDirection(INPUT); AddTargetDirection(OUTPUT) // add to the "compatible intersection" configuration
        // x TODO: AddSourceDirection(OUTPUT); AddTargetDirection(INPUT) // add to the "compatible intersection" configuration
        // CHECK FOR THESE:
        // TODO: AddSourceDirection(OUTPUT); AddTargetDirection(INPUT) // add to the "compatible intersection" configuration
        // TODO: AddSourceDirection(INPUT); AddTargetDirection(OUTPUT) // add to the "compatible intersection" configuration
        // CHECK FOR THESE:
        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(INPUT) // add to the "compatible intersection" configuration
        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(OUTPUT) // add to the "compatible intersection" configuration
        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" configuration
        // TODO: AddSourceDirection(INPUT); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" configuration
        // TODO: AddSourceDirection(OUTPUT); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" configuration
        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" configuration

        // <DIRECTION_CONSTRAINT_CHECKS>
        // Check direction compatibility
        if (sourceConfiguration.directions == null || targetConfiguration.directions == null) {

            // TODO:

        } else {

            if (sourceConfiguration.directions.states.contains(Direction.INPUT) && targetConfiguration.directions.states.contains(Direction.OUTPUT)) {

                compatibleSourceConfigurationConstraint.directions.states.add(Direction.INPUT);
                compatibleTargetConfigurationConstraint.directions.states.add(Direction.OUTPUT);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (1)");
                }
            }

            if (sourceConfiguration.directions.states.contains(Direction.OUTPUT) && targetConfiguration.directions.states.contains(Direction.INPUT)) {

                compatibleSourceConfigurationConstraint.directions.states.add(Direction.OUTPUT);
                compatibleTargetConfigurationConstraint.directions.states.add(Direction.INPUT);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (2)");
                }
            }

            if (sourceConfiguration.directions.states.contains(Direction.BIDIRECTIONAL) && targetConfiguration.directions.states.contains(Direction.INPUT)) {

                compatibleSourceConfigurationConstraint.directions.states.add(Direction.BIDIRECTIONAL);
                compatibleTargetConfigurationConstraint.directions.states.add(Direction.INPUT);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (3)");
                }
            }

            if (sourceConfiguration.directions.states.contains(Direction.INPUT) && targetConfiguration.directions.states.contains(Direction.BIDIRECTIONAL)) {

                compatibleSourceConfigurationConstraint.directions.states.add(Direction.INPUT);
                compatibleTargetConfigurationConstraint.directions.states.add(Direction.BIDIRECTIONAL);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (3)");
                }
            }

            if (sourceConfiguration.directions.states.contains(Direction.BIDIRECTIONAL) && targetConfiguration.directions.states.contains(Direction.OUTPUT)) {

                compatibleSourceConfigurationConstraint.directions.states.add(Direction.BIDIRECTIONAL);
                compatibleTargetConfigurationConstraint.directions.states.add(Direction.OUTPUT);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (3)");
                }
            }

            if (sourceConfiguration.directions.states.contains(Direction.OUTPUT) && targetConfiguration.directions.states.contains(Direction.BIDIRECTIONAL)) {

                compatibleSourceConfigurationConstraint.directions.states.add(Direction.OUTPUT);
                compatibleTargetConfigurationConstraint.directions.states.add(Direction.BIDIRECTIONAL);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (3)");
                }
            }

            if (sourceConfiguration.directions.states.contains(Direction.BIDIRECTIONAL) && targetConfiguration.directions.states.contains(Direction.BIDIRECTIONAL)) {

                compatibleSourceConfigurationConstraint.directions.states.add(Direction.BIDIRECTIONAL);
                compatibleTargetConfigurationConstraint.directions.states.add(Direction.BIDIRECTIONAL);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (3)");
                }
            }

        }
        // </DIRECTION_CONSTRAINT_CHECKS>

        // TODO: null, NONE

        // <VOLTAGE_CONSTRAINT_CHECKS>
        if (sourceConfiguration.voltages == null || targetConfiguration.voltages == null) {

            // TODO:

        } else {

            // Check voltage compatibility
            if (sourceConfiguration.voltages.states.contains(Voltage.TTL) && targetConfiguration.voltages.states.contains(Voltage.TTL)) {

                compatibleSourceConfigurationConstraint.voltages.states.add(Voltage.TTL);
                compatibleTargetConfigurationConstraint.voltages.states.add(Voltage.TTL);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (4)");
                }
            }

            if (sourceConfiguration.voltages.states.contains(Voltage.CMOS) && targetConfiguration.voltages.states.contains(Voltage.CMOS)) {

                compatibleSourceConfigurationConstraint.voltages.states.add(Voltage.CMOS);
                compatibleTargetConfigurationConstraint.voltages.states.add(Voltage.CMOS);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (5)");
                }
            }

            if (sourceConfiguration.voltages.states.contains(Voltage.COMMON) && targetConfiguration.voltages.states.contains(Voltage.COMMON)) {

                compatibleSourceConfigurationConstraint.voltages.states.add(Voltage.COMMON);
                compatibleTargetConfigurationConstraint.voltages.states.add(Voltage.COMMON);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (6)");
                }
            }

        }

        // TODO: COMMON - TTL
        // TODO: COMMON - CMOS

        // TODO: null, NONE
        // </VOLTAGE_CONSTRAINT_CHECKS>

        if (ENABLE_VERBOSE_OUTPUT) {
            System.out.println("\n");
        }

        // TODO: Verify this logic for returning "null"
        if ((compatibleSourceConfigurationConstraint.mode == null || compatibleTargetConfigurationConstraint.mode == null)
                || (compatibleSourceConfigurationConstraint.directions.states.size() == 0 || compatibleTargetConfigurationConstraint.directions.states.size() == 0)
                || (compatibleSourceConfigurationConstraint.voltages.states.size() == 0 || compatibleTargetConfigurationConstraint.voltages.states.size() == 0)) {
            return null;
        } else {
            return new StateSet<>(compatibleSourceConfigurationConstraint, compatibleTargetConfigurationConstraint);
        }
    }

    // TODO: rankCompatibleConfigurations(<compatible-configuration-list>)

}
