package camp.computer.data.format.configuration;

public class PortConfigurationConstraint {
    // TODO: Consider renaming to ConfigurationConstraint for general use in constructs?

    // TODO: Current Mode: [ AC, DC ]

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

    // "generate iasm device a device b" (or ask/generate after adding path)

    public Mode mode = null;

    /**
     * {@code directions} is set to {@code null} when it is not applicable to the {@code PortConfigurationConstraint}. This is
     * distinct from creating a {@code ValueSet} with no elements (i.e., an empty set).
     */
    public ValueSet<Direction> directions = null;


    /**
     * {@code voltages} is set to {@code null} when assigning the voltage is not applicable (like with
     * {@code directions}). This is distinct from creating a {@code ValueSet} with no elements (i.e., an empty set).
     */
    public ValueSet<Voltage> voltages = null;

//    public List<AttributeValueSet> attributes = new ArrayList<>();

    // TODO: Bus dependencies (device-device interface level)

    public PortConfigurationConstraint(Mode mode, ValueSet<Direction> directions, ValueSet<Voltage> voltages) {

        this.mode = mode;

        this.directions = directions;

        this.voltages = voltages;

    }

    public static boolean isCompatible(PortConfigurationConstraint sourceConfiguration, PortConfigurationConstraint targetConfiguration) {

        // <VERBOSE_LOG>
        // Source Port
        boolean ENABLE_VERBOSE_OUTPUT = false;
        if (ENABLE_VERBOSE_OUTPUT) {
            System.out.print("? (source) " + sourceConfiguration.mode + ";");

            if (sourceConfiguration.directions == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < sourceConfiguration.directions.values.size(); i++) {
                    System.out.print("" + sourceConfiguration.directions.values.get(i));
                    if ((i + 1) < sourceConfiguration.directions.values.size()) {
                        System.out.print(",");
                    }
                }
            }
            System.out.print(";");

            if (sourceConfiguration.voltages == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < sourceConfiguration.voltages.values.size(); i++) {
                    System.out.print("" + sourceConfiguration.voltages.values.get(i));
                    if ((i + 1) < sourceConfiguration.voltages.values.size()) {
                        System.out.print(",");
                    }
                }
            }
            System.out.println();

            // Target Port(s)
            System.out.print("  (target) " + sourceConfiguration.mode + ";");

            if (targetConfiguration.directions == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < targetConfiguration.directions.values.size(); i++) {
                    System.out.print("" + targetConfiguration.directions.values.get(i));
                    if ((i + 1) < targetConfiguration.directions.values.size()) {
                        System.out.print(",");
                    }
                }
            }
            System.out.print(";");

            if (targetConfiguration.voltages == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < targetConfiguration.voltages.values.size(); i++) {
                    System.out.print("" + targetConfiguration.voltages.values.get(i));
                    if ((i + 1) < targetConfiguration.voltages.values.size()) {
                        System.out.print(",");
                    }
                }
            }
        }
        // </VERBOSE_LOG>

//        Mode compatibleMode = null;
//        ValueSet<Direction> compatibleDirections = null;
//        ValueSet<Voltage> compatibleVoltages = null;
        PortConfigurationConstraint sourcePortConfigurationSubset = new PortConfigurationConstraint(null, null, null); // populate with valid subset given configuration constraints
        PortConfigurationConstraint targetPortConfigurationSubset = new PortConfigurationConstraint(null, null, null); // populate with valid subset given configuration constraints

        boolean hasCompatibleMode = false;
        boolean hasCompabibleDirection = false;
        boolean hasCompabibleVoltage = false;

        if (sourceConfiguration.mode == targetConfiguration.mode) {
//            compatibleMode = sourceConfiguration.mode;
            hasCompatibleMode = true;
        }

        if (hasCompatibleMode) {

            // TODO: compute the "compatible intersection" configurations of the mode constraints of the two configurations
            // TODO: compute the "compatible intersection" configurations of the directions constraints of the two configurations
            // TODO: compute the "compatible intersection" configurations of the voltages constraints of the two configurations

            // Check direction compatibility
            if (sourceConfiguration.directions.values.contains(Direction.INPUT) && targetConfiguration.directions.values.contains(Direction.OUTPUT)) {
//                if (compatibleDirections == null) {
//                    compatibleDirections = new ValueSet<>();
//                }
//                compatibleDirections.values.add(Direction.INPUT);

                // CHECK FOR THESE:
                // TODO: AddSourceDirection(INPUT); AddTargetDirection(OUTPUT) // add to the "compatible intersection" configuration
                // TODO: AddSourceDirection(OUTPUT); AddTargetDirection(INPUT) // add to the "compatible intersection" configuration

                hasCompabibleDirection = true;
                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (1)");
                }
            }

            if (sourceConfiguration.directions.values.contains(Direction.OUTPUT) && targetConfiguration.directions.values.contains(Direction.INPUT)) {
//                if (compatibleDirections == null) {
//                    compatibleDirections = new ValueSet<>();
//                }
//                compatibleDirections.values.add(Direction.OUTPUT);

                // CHECK FOR THESE:
                // TODO: AddSourceDirection(OUTPUT); AddTargetDirection(INPUT) // add to the "compatible intersection" configuration
                // TODO: AddSourceDirection(INPUT); AddTargetDirection(OUTPUT) // add to the "compatible intersection" configuration

                hasCompabibleDirection = true;
                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (2)");
                }
            }

            if (sourceConfiguration.directions.values.contains(Direction.BIDIRECTIONAL) && targetConfiguration.directions.values.contains(Direction.INPUT)
                    || sourceConfiguration.directions.values.contains(Direction.BIDIRECTIONAL) && targetConfiguration.directions.values.contains(Direction.OUTPUT)
                    || sourceConfiguration.directions.values.contains(Direction.BIDIRECTIONAL) && targetConfiguration.directions.values.contains(Direction.BIDIRECTIONAL)) {

                // CHECK FOR THESE:
                // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(INPUT) // add to the "compatible intersection" configuration
                // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(OUTPUT) // add to the "compatible intersection" configuration
                // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" configuration
                // TODO: AddSourceDirection(INPUT); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" configuration
                // TODO: AddSourceDirection(OUTPUT); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" configuration
                // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" configuration

                hasCompabibleDirection = true;
                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (3)");
                }
            }
            // TODO: null, NONE

//            System.out.println(">>> COMPATIBLE: (" + configuration.mode + ", ...) --- (" + otherConfiguration.mode + ", ...)");

        }
        // TODO: null, NONE

        if (hasCompabibleDirection) {

            // Check voltage compatibility
            if (sourceConfiguration.voltages.values.contains(Voltage.TTL) && targetConfiguration.voltages.values.contains(Voltage.TTL)) {
                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (4)");
                }
                hasCompabibleVoltage = true;
            }

            if (sourceConfiguration.voltages.values.contains(Voltage.CMOS) && targetConfiguration.voltages.values.contains(Voltage.CMOS)) {
                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (5)");
                }
                hasCompabibleVoltage = true;
            }

            if (sourceConfiguration.voltages.values.contains(Voltage.COMMON) && targetConfiguration.voltages.values.contains(Voltage.COMMON)) {
                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (6)");
                }
                hasCompabibleVoltage = true;
            }
            // TODO: null, NONE

        }

        // TODO: I2C, SPI, UART

        if (ENABLE_VERBOSE_OUTPUT) {
            System.out.println("\n");
        }

        return (hasCompatibleMode && hasCompabibleDirection && hasCompabibleVoltage);
    }

    public static ValueSet<PortConfigurationConstraint> getCompatibleConfiguration(PortConfigurationConstraint sourceConfiguration, PortConfigurationConstraint targetConfiguration) {

        // <VERBOSE_LOG>
        // Source Port
        boolean ENABLE_VERBOSE_OUTPUT = false;
        if (ENABLE_VERBOSE_OUTPUT) {
            System.out.print("? (source) " + sourceConfiguration.mode + ";");

            if (sourceConfiguration.directions == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < sourceConfiguration.directions.values.size(); i++) {
                    System.out.print("" + sourceConfiguration.directions.values.get(i));
                    if ((i + 1) < sourceConfiguration.directions.values.size()) {
                        System.out.print(",");
                    }
                }
            }
            System.out.print(";");

            if (sourceConfiguration.voltages == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < sourceConfiguration.voltages.values.size(); i++) {
                    System.out.print("" + sourceConfiguration.voltages.values.get(i));
                    if ((i + 1) < sourceConfiguration.voltages.values.size()) {
                        System.out.print(",");
                    }
                }
            }
            System.out.println();

            // Target Port(s)
            System.out.print("  (target) " + sourceConfiguration.mode + ";");

            if (targetConfiguration.directions == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < targetConfiguration.directions.values.size(); i++) {
                    System.out.print("" + targetConfiguration.directions.values.get(i));
                    if ((i + 1) < targetConfiguration.directions.values.size()) {
                        System.out.print(",");
                    }
                }
            }
            System.out.print(";");

            if (targetConfiguration.voltages == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < targetConfiguration.voltages.values.size(); i++) {
                    System.out.print("" + targetConfiguration.voltages.values.get(i));
                    if ((i + 1) < targetConfiguration.voltages.values.size()) {
                        System.out.print(",");
                    }
                }
            }
        }
        // </VERBOSE_LOG>

//        PortConfigurationConstraint compatibleSourcePortConfiguration = new PortConfigurationConstraint(null, null, null); // populate with valid subset given configuration constraints
//        PortConfigurationConstraint compatibleTargetPortConfiguration = new PortConfigurationConstraint(null, null, null); // populate with valid subset given configuration constraints
        PortConfigurationConstraint compatibleSourcePortConfiguration = new PortConfigurationConstraint(null, new ValueSet<>(), new ValueSet<>()); // populate with valid subset given configuration constraints
        PortConfigurationConstraint compatibleTargetPortConfiguration = new PortConfigurationConstraint(null, new ValueSet<>(), new ValueSet<>()); // populate with valid subset given configuration constraints

        // <MODE_CONSTRAINT_CHECKS>
        if (sourceConfiguration.mode == targetConfiguration.mode) {
            compatibleSourcePortConfiguration.mode = sourceConfiguration.mode;
            compatibleTargetPortConfiguration.mode = targetConfiguration.mode;
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

            if (sourceConfiguration.directions.values.contains(Direction.INPUT) && targetConfiguration.directions.values.contains(Direction.OUTPUT)) {

                compatibleSourcePortConfiguration.directions.values.add(Direction.INPUT);
                compatibleTargetPortConfiguration.directions.values.add(Direction.OUTPUT);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (1)");
                }
            }

            if (sourceConfiguration.directions.values.contains(Direction.OUTPUT) && targetConfiguration.directions.values.contains(Direction.INPUT)) {

                compatibleSourcePortConfiguration.directions.values.add(Direction.OUTPUT);
                compatibleTargetPortConfiguration.directions.values.add(Direction.INPUT);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (2)");
                }
            }

            if (sourceConfiguration.directions.values.contains(Direction.BIDIRECTIONAL) && targetConfiguration.directions.values.contains(Direction.INPUT)) {

                compatibleSourcePortConfiguration.directions.values.add(Direction.BIDIRECTIONAL);
                compatibleTargetPortConfiguration.directions.values.add(Direction.INPUT);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (3)");
                }
            }

            if (sourceConfiguration.directions.values.contains(Direction.INPUT) && targetConfiguration.directions.values.contains(Direction.BIDIRECTIONAL)) {

                compatibleSourcePortConfiguration.directions.values.add(Direction.INPUT);
                compatibleTargetPortConfiguration.directions.values.add(Direction.BIDIRECTIONAL);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (3)");
                }
            }

            if (sourceConfiguration.directions.values.contains(Direction.BIDIRECTIONAL) && targetConfiguration.directions.values.contains(Direction.OUTPUT)) {

                compatibleSourcePortConfiguration.directions.values.add(Direction.BIDIRECTIONAL);
                compatibleTargetPortConfiguration.directions.values.add(Direction.OUTPUT);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (3)");
                }
            }

            if (sourceConfiguration.directions.values.contains(Direction.OUTPUT) && targetConfiguration.directions.values.contains(Direction.BIDIRECTIONAL)) {

                compatibleSourcePortConfiguration.directions.values.add(Direction.OUTPUT);
                compatibleTargetPortConfiguration.directions.values.add(Direction.BIDIRECTIONAL);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (3)");
                }
            }

            if (sourceConfiguration.directions.values.contains(Direction.BIDIRECTIONAL) && targetConfiguration.directions.values.contains(Direction.BIDIRECTIONAL)) {

                compatibleSourcePortConfiguration.directions.values.add(Direction.BIDIRECTIONAL);
                compatibleTargetPortConfiguration.directions.values.add(Direction.BIDIRECTIONAL);

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
            if (sourceConfiguration.voltages.values.contains(Voltage.TTL) && targetConfiguration.voltages.values.contains(Voltage.TTL)) {

                compatibleSourcePortConfiguration.voltages.values.add(Voltage.TTL);
                compatibleTargetPortConfiguration.voltages.values.add(Voltage.TTL);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (4)");
                }
            }

            if (sourceConfiguration.voltages.values.contains(Voltage.CMOS) && targetConfiguration.voltages.values.contains(Voltage.CMOS)) {

                compatibleSourcePortConfiguration.voltages.values.add(Voltage.CMOS);
                compatibleTargetPortConfiguration.voltages.values.add(Voltage.CMOS);

                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (5)");
                }
            }

            if (sourceConfiguration.voltages.values.contains(Voltage.COMMON) && targetConfiguration.voltages.values.contains(Voltage.COMMON)) {

                compatibleSourcePortConfiguration.voltages.values.add(Voltage.COMMON);
                compatibleTargetPortConfiguration.voltages.values.add(Voltage.COMMON);

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
        if ((compatibleSourcePortConfiguration.mode == null || compatibleTargetPortConfiguration.mode == null)
                || (compatibleSourcePortConfiguration.directions.values.size() == 0 || compatibleTargetPortConfiguration.directions.values.size() == 0)
                || (compatibleSourcePortConfiguration.voltages.values.size() == 0 || compatibleTargetPortConfiguration.voltages.values.size() == 0)) {
            return null;
        } else {
            return new ValueSet<>(compatibleSourcePortConfiguration, compatibleTargetPortConfiguration);
        }
    }

}
