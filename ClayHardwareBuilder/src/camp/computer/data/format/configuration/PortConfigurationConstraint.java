package camp.computer.data.format.configuration;

public class PortConfigurationConstraint {
    // TODO: Consider renaming to PortConfigurationConstraint for general use in constructs?

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
        BIDIRECTIONAL // Supporting bidirectional variables is different than supporting both the input and output configurations because in the latter case, only one can be specified.
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
    // TODO: public ValueSet<Mode> modes = null;
    public Mode mode = null;

    /**
     * {@code directions} is set to {@code null} when it is not applicable to the {@code PortConfigurationConstraint}. This is
     * distinct from creating a {@code ValueSet} with no elements (i.e., an empty set).
     */
//    public ValueSet<Direction> directions = null;
    public ValueSet directions = null;


    /**
     * {@code voltages} is set to {@code null} when assigning the voltage is not applicable (like with
     * {@code directions}). This is distinct from creating a {@code ValueSet} with no elements (i.e., an empty set).
     */
//    public ValueSet<Voltage> voltages = null;
    public ValueSet voltages = null;
    // </CONFIGURATION_STATE_ASSIGNMENT_CONSTRAINTS>

    // TODO: Bus dependencies (device-device interface level)

    // TODO: PortConfigurationConstraint(State... values)/(variable.values.get(<title>), ...)
//    public PortConfigurationConstraint(Mode mode, ValueSet<Direction> directions, ValueSet<Voltage> voltages) {
    public PortConfigurationConstraint(Mode mode, ValueSet directions, ValueSet voltages) {

        this.mode = mode;
        this.directions = directions;
        this.voltages = voltages;

    }

    // TODO: discoverCompatiblePortSet(<device-a>, <device-b>)

    /**
     * Computes/resolves the pair of "minimal compatible" {@code PortConfigurationConstraint}s
     * given a pair of two {@code PortConfigurationConstraint}s. The resulting {@code PortConfigurationConstraint}s
     * will remove values that cannot be assigned to variables in the {@code PortConfigurationConstraint}.
     *
     * @param sourceConfiguration
     * @param targetConfiguration
     * @return
     */
//    public static ValueSet<PortConfigurationConstraint> computeCompatibleConfigurationSet(PortConfigurationConstraint sourceConfiguration, PortConfigurationConstraint targetConfiguration) {
//
//        // <VERBOSE_LOG>
//        // Source Port
//        boolean ENABLE_VERBOSE_OUTPUT = false;
//        if (ENABLE_VERBOSE_OUTPUT) {
//
//            System.out.print("? (source) " + sourceConfiguration.mode + ";");
//
//            if (sourceConfiguration.directions == null) {
//                System.out.print("null");
//            } else {
//                for (int i = 0; i < sourceConfiguration.directions.values.size(); i++) {
//                    System.out.print("" + sourceConfiguration.directions.values.get(i));
//                    if ((i + 1) < sourceConfiguration.directions.values.size()) {
//                        System.out.print(",");
//                    }
//                }
//            }
//            System.out.print(";");
//
//            if (sourceConfiguration.voltages == null) {
//                System.out.print("null");
//            } else {
//                for (int i = 0; i < sourceConfiguration.voltages.values.size(); i++) {
//                    System.out.print("" + sourceConfiguration.voltages.values.get(i));
//                    if ((i + 1) < sourceConfiguration.voltages.values.size()) {
//                        System.out.print(",");
//                    }
//                }
//            }
//            System.out.println();
//
//            // Target Port(s)
//            System.out.print("  (target) " + targetConfiguration.mode + ";");
//
//            if (targetConfiguration.directions == null) {
//                System.out.print("null");
//            } else {
//                for (int i = 0; i < targetConfiguration.directions.values.size(); i++) {
//                    System.out.print("" + targetConfiguration.directions.values.get(i));
//                    if ((i + 1) < targetConfiguration.directions.values.size()) {
//                        System.out.print(",");
//                    }
//                }
//            }
//            System.out.print(";");
//
//            if (targetConfiguration.voltages == null) {
//                System.out.print("null");
//            } else {
//                for (int i = 0; i < targetConfiguration.voltages.values.size(); i++) {
//                    System.out.print("" + targetConfiguration.voltages.values.get(i));
//                    if ((i + 1) < targetConfiguration.voltages.values.size()) {
//                        System.out.print(",");
//                    }
//                }
//            }
//        }
//        // </VERBOSE_LOG>
//
////        PortConfigurationConstraint compatibleSourcePortConfigurationConstraint = new PortConfigurationConstraint(null, null, null); // populate with valid subset given variables constraints
////        PortConfigurationConstraint compatibleTargetPortConfigurationConstraint = new PortConfigurationConstraint(null, null, null); // populate with valid subset given variables constraints
//        PortConfigurationConstraint compatibleSourcePortConfigurationConstraint = new PortConfigurationConstraint(null, new ValueSet<>(), new ValueSet<>()); // populate with valid subset given variables constraints
//        PortConfigurationConstraint compatibleTargetPortConfigurationConstraint = new PortConfigurationConstraint(null, new ValueSet<>(), new ValueSet<>()); // populate with valid subset given variables constraints
//
//        // <MODE_CONSTRAINT_CHECKS>
//        if (sourceConfiguration.mode == targetConfiguration.mode) {
//            compatibleSourcePortConfigurationConstraint.mode = sourceConfiguration.mode;
//            compatibleTargetPortConfigurationConstraint.mode = targetConfiguration.mode;
//        }
//
//        // TODO: I2C, SPI, UART, etc.
//
//        // </MODE_CONSTRAINT_CHECKS>
//
//        // TODO: compute the "compatible intersection" configurations of the mode constraints of the two configurations
//        // TODO: compute the "compatible intersection" configurations of the directions constraints of the two configurations
//        // TODO: compute the "compatible intersection" configurations of the voltages constraints of the two configurations
//
//        //---
//
//        // CHECK FOR THESE:
//        // x TODO: AddSourceDirection(INPUT); AddTargetDirection(OUTPUT) // add to the "compatible intersection" variables
//        // x TODO: AddSourceDirection(OUTPUT); AddTargetDirection(INPUT) // add to the "compatible intersection" variables
//        // CHECK FOR THESE:
//        // TODO: AddSourceDirection(OUTPUT); AddTargetDirection(INPUT) // add to the "compatible intersection" variables
//        // TODO: AddSourceDirection(INPUT); AddTargetDirection(OUTPUT) // add to the "compatible intersection" variables
//        // CHECK FOR THESE:
//        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(INPUT) // add to the "compatible intersection" variables
//        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(OUTPUT) // add to the "compatible intersection" variables
//        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" variables
//        // TODO: AddSourceDirection(INPUT); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" variables
//        // TODO: AddSourceDirection(OUTPUT); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" variables
//        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" variables
//
//        // <DIRECTION_CONSTRAINT_CHECKS>
//        // Check direction compatibility
//        if (sourceConfiguration.directions == null || targetConfiguration.directions == null) {
//
//            // TODO:
//
//        } else {
//
//            if (sourceConfiguration.directions.values.contains(Direction.INPUT) && targetConfiguration.directions.values.contains(Direction.OUTPUT)) {
//
//                compatibleSourcePortConfigurationConstraint.directions.values.add(Direction.INPUT);
//                compatibleTargetPortConfigurationConstraint.directions.values.add(Direction.OUTPUT);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (1)");
//                }
//            }
//
//            if (sourceConfiguration.directions.values.contains(Direction.OUTPUT) && targetConfiguration.directions.values.contains(Direction.INPUT)) {
//
//                compatibleSourcePortConfigurationConstraint.directions.values.add(Direction.OUTPUT);
//                compatibleTargetPortConfigurationConstraint.directions.values.add(Direction.INPUT);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (2)");
//                }
//            }
//
//            if (sourceConfiguration.directions.values.contains(Direction.BIDIRECTIONAL) && targetConfiguration.directions.values.contains(Direction.INPUT)) {
//
//                compatibleSourcePortConfigurationConstraint.directions.values.add(Direction.BIDIRECTIONAL);
//                compatibleTargetPortConfigurationConstraint.directions.values.add(Direction.INPUT);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//            if (sourceConfiguration.directions.values.contains(Direction.INPUT) && targetConfiguration.directions.values.contains(Direction.BIDIRECTIONAL)) {
//
//                compatibleSourcePortConfigurationConstraint.directions.values.add(Direction.INPUT);
//                compatibleTargetPortConfigurationConstraint.directions.values.add(Direction.BIDIRECTIONAL);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//            if (sourceConfiguration.directions.values.contains(Direction.BIDIRECTIONAL) && targetConfiguration.directions.values.contains(Direction.OUTPUT)) {
//
//                compatibleSourcePortConfigurationConstraint.directions.values.add(Direction.BIDIRECTIONAL);
//                compatibleTargetPortConfigurationConstraint.directions.values.add(Direction.OUTPUT);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//            if (sourceConfiguration.directions.values.contains(Direction.OUTPUT) && targetConfiguration.directions.values.contains(Direction.BIDIRECTIONAL)) {
//
//                compatibleSourcePortConfigurationConstraint.directions.values.add(Direction.OUTPUT);
//                compatibleTargetPortConfigurationConstraint.directions.values.add(Direction.BIDIRECTIONAL);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//            if (sourceConfiguration.directions.values.contains(Direction.BIDIRECTIONAL) && targetConfiguration.directions.values.contains(Direction.BIDIRECTIONAL)) {
//
//                compatibleSourcePortConfigurationConstraint.directions.values.add(Direction.BIDIRECTIONAL);
//                compatibleTargetPortConfigurationConstraint.directions.values.add(Direction.BIDIRECTIONAL);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//        }
//        // </DIRECTION_CONSTRAINT_CHECKS>
//
//        // TODO: null, NONE
//
//        // <VOLTAGE_CONSTRAINT_CHECKS>
//        if (sourceConfiguration.voltages == null || targetConfiguration.voltages == null) {
//
//            // TODO:
//
//        } else {
//
//            // Check voltage compatibility
//            if (sourceConfiguration.voltages.values.contains(Voltage.TTL) && targetConfiguration.voltages.values.contains(Voltage.TTL)) {
//
//                compatibleSourcePortConfigurationConstraint.voltages.values.add(Voltage.TTL);
//                compatibleTargetPortConfigurationConstraint.voltages.values.add(Voltage.TTL);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (4)");
//                }
//            }
//
//            if (sourceConfiguration.voltages.values.contains(Voltage.CMOS) && targetConfiguration.voltages.values.contains(Voltage.CMOS)) {
//
//                compatibleSourcePortConfigurationConstraint.voltages.values.add(Voltage.CMOS);
//                compatibleTargetPortConfigurationConstraint.voltages.values.add(Voltage.CMOS);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (5)");
//                }
//            }
//
//            if (sourceConfiguration.voltages.values.contains(Voltage.COMMON) && targetConfiguration.voltages.values.contains(Voltage.COMMON)) {
//
//                compatibleSourcePortConfigurationConstraint.voltages.values.add(Voltage.COMMON);
//                compatibleTargetPortConfigurationConstraint.voltages.values.add(Voltage.COMMON);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (6)");
//                }
//            }
//
//        }
//
//        // TODO: COMMON - TTL
//        // TODO: COMMON - CMOS
//
//        // TODO: null, NONE
//        // </VOLTAGE_CONSTRAINT_CHECKS>
//
//        if (ENABLE_VERBOSE_OUTPUT) {
//            System.out.println("\n");
//        }
//
//        // TODO: Verify this logic for returning "null"
//        if ((compatibleSourcePortConfigurationConstraint.mode == null || compatibleTargetPortConfigurationConstraint.mode == null)
//                || (compatibleSourcePortConfigurationConstraint.directions.values.size() == 0 || compatibleTargetPortConfigurationConstraint.directions.values.size() == 0)
//                || (compatibleSourcePortConfigurationConstraint.voltages.values.size() == 0 || compatibleTargetPortConfigurationConstraint.voltages.values.size() == 0)) {
//            return null;
//        } else {
//            return new ValueSet<>(compatibleSourcePortConfigurationConstraint, compatibleTargetPortConfigurationConstraint);
//        }
//    }

    // TODO: rankCompatibleConfigurations(<compatible-variables-list>)

}
