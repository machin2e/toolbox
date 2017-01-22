package camp.computer.data.format.configuration;

public class PortConfigurationConstraint {
    // TODO: Consider renaming to ConfigurationConstraint for general use in constructs?

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

    public static boolean isCompatible(PortConfigurationConstraint configuration, PortConfigurationConstraint otherConfiguration) {

        // Source Port
        boolean ENABLE_VERBOSE_OUTPUT = false;
        if (ENABLE_VERBOSE_OUTPUT) {
            System.out.print("? (source) " + configuration.mode + ";");

            if (configuration.directions == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < configuration.directions.values.size(); i++) {
                    System.out.print("" + configuration.directions.values.get(i));
                    if ((i + 1) < configuration.directions.values.size()) {
                        System.out.print(",");
                    }
                }
            }
            System.out.print(";");

            if (configuration.voltages == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < configuration.voltages.values.size(); i++) {
                    System.out.print("" + configuration.voltages.values.get(i));
                    if ((i + 1) < configuration.voltages.values.size()) {
                        System.out.print(",");
                    }
                }
            }
            System.out.println();

            // Target Port(s)
            System.out.print("  (target) " + configuration.mode + ";");

            if (otherConfiguration.directions == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < otherConfiguration.directions.values.size(); i++) {
                    System.out.print("" + otherConfiguration.directions.values.get(i));
                    if ((i + 1) < otherConfiguration.directions.values.size()) {
                        System.out.print(",");
                    }
                }
            }
            System.out.print(";");

            if (otherConfiguration.voltages == null) {
                System.out.print("null");
            } else {
                for (int i = 0; i < otherConfiguration.voltages.values.size(); i++) {
                    System.out.print("" + otherConfiguration.voltages.values.get(i));
                    if ((i + 1) < otherConfiguration.voltages.values.size()) {
                        System.out.print(",");
                    }
                }
            }
        }

        boolean hasCompatibleMode = false;
        boolean hasCompabibleDirection = false;
        boolean hasCompabibleVoltage = false;

        if (configuration.mode == otherConfiguration.mode) {
            hasCompatibleMode = true;
        }

        if (hasCompatibleMode) {

            // Check direction compatibility
            if (configuration.directions.values.contains(Direction.INPUT) && otherConfiguration.directions.values.contains(Direction.OUTPUT)) {
                hasCompabibleDirection = true;
                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (1)");
                }
            }

            if (configuration.directions.values.contains(Direction.OUTPUT) && otherConfiguration.directions.values.contains(Direction.INPUT)) {
                hasCompabibleDirection = true;
                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (2)");
                }
            }

            if (configuration.directions.values.contains(Direction.BIDIRECTIONAL) && otherConfiguration.directions.values.contains(Direction.INPUT)
                    || configuration.directions.values.contains(Direction.BIDIRECTIONAL) && otherConfiguration.directions.values.contains(Direction.OUTPUT)
                    || configuration.directions.values.contains(Direction.BIDIRECTIONAL) && otherConfiguration.directions.values.contains(Direction.BIDIRECTIONAL)) {
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
            if (configuration.voltages.values.contains(Voltage.TTL) && otherConfiguration.voltages.values.contains(Voltage.TTL)) {
                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (4)");
                }
                hasCompabibleVoltage = true;
            }

            if (configuration.voltages.values.contains(Voltage.CMOS) && otherConfiguration.voltages.values.contains(Voltage.CMOS)) {
                if (ENABLE_VERBOSE_OUTPUT) {
                    System.out.println("  > Match (5)");
                }
                hasCompabibleVoltage = true;
            }

            if (configuration.voltages.values.contains(Voltage.COMMON) && otherConfiguration.voltages.values.contains(Voltage.COMMON)) {
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

}
