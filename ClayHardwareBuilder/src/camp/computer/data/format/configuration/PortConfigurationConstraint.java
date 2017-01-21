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

        if (configuration.mode == otherConfiguration.mode) {

            // Check direction compatibility
            if (configuration.directions.values.contains(Direction.INPUT) && !otherConfiguration.directions.values.contains(Direction.OUTPUT)) {
                System.out.println("  > BREAK 1");
                return false;
            //} else if (!((configuration.directions.values.contains(Direction.OUTPUT) && (otherConfiguration.directions.values.contains(Direction.INPUT) || otherConfiguration.directions.values.contains(Direction.BIDIRECTIONAL))))) {
            }

            if (configuration.directions.values.contains(Direction.OUTPUT) && !otherConfiguration.directions.values.contains(Direction.INPUT)) {
                System.out.println("  > BREAK 2");
                return false;
            }

            if (configuration.directions.values.contains(Direction.BIDIRECTIONAL) && !otherConfiguration.directions.values.contains(Direction.INPUT)
                    || configuration.directions.values.contains(Direction.BIDIRECTIONAL) && !otherConfiguration.directions.values.contains(Direction.OUTPUT)
                    || configuration.directions.values.contains(Direction.BIDIRECTIONAL) && !otherConfiguration.directions.values.contains(Direction.BIDIRECTIONAL)) {
                System.out.println("  > BREAK 3");
                return false;
            }
            // TODO: null, NONE

            // Check voltage compatibility
            if (configuration.voltages.values.contains(Voltage.TTL) && !otherConfiguration.voltages.values.contains(Voltage.TTL)) {
                System.out.println("  > BREAK 4");
                return false;
            }

            if (configuration.voltages.values.contains(Voltage.CMOS) && !otherConfiguration.voltages.values.contains(Voltage.CMOS)) {
                System.out.println("  > BREAK 5");
                return false;
            }
            // TODO: null, NONE

            System.out.println(">>> COMPATIBLE: (" + configuration.mode + ", ...) --- (" + otherConfiguration.mode + ", ...)");

            return true;

        }
        // TODO: null, NONE

        // TODO: I2C, SPI, UART

        return false;
    }

}
