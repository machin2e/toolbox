package camp.computer.data.format.configuration;

public class PortConfiguration {

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
     * {@code directions} is set to {@code null} when it is not applicable to the {@code PortConfiguration}. This is
     * distinct from creating a {@code ValueSet} with no elements (i.e., an empty set).
     */
    public ValueSet<Direction> directions = null;


    /**
     * {@code voltages} is set to {@code null} when assigning the voltage is not applicable (like with
     * {@code directions}). This is distinct from creating a {@code ValueSet} with no elements (i.e., an empty set).
     */
    public ValueSet<Voltage> voltages = null;

    // TODO: Bus dependencies (device-device interface level)

    public PortConfiguration(Mode mode, ValueSet<Direction> directions, ValueSet<Voltage> voltages) {

        this.mode = mode;

        this.directions = directions;

        this.voltages = voltages;

    }

}
