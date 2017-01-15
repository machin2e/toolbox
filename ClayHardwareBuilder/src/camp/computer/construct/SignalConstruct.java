package camp.computer.construct;

public class SignalConstruct {

    // data <--> construct ==> project [~~> project instance]

    // Signal Configurations/Combinations:
    // Electronic; Digital, ; Input, Output, Both

    // TODO: Measure the current and store it in the PortConstruct/PortStatus. If can't measure the current, store the expected/rated current.

//    public enum Channel { // formerly "Medium"
//        NONE,
//        ELECTRONIC,
//        BLUETOOTH_LE, // RADIO
//        THREAD, // RADIO
//        INTERNET // RADIO/WIFI
//    }

    public enum Mode {
        NONE,
        DIGITAL, // DISCRETE
        ANALOG, // CONTINUOUS
        POWER,
        COMMON,

        PWM,
        RESISTIVE_TOUCH,

        // I2C
        I2C_SDA,
        I2C_SCL,

        // SPI
        SPI_SCLK,
        SPI_MOSI,
        SPI_MISO,
        SPI_SS,

        // UART
        UART_RX,
        UART_TX


    }

    public enum Protocol {
        // Electronic
        PWM,
        I2C,
        SPI,
        UART,
        // Internet/Thread
        TCP,
        UDP
    }

    public enum Direction {
        NONE,
        INPUT,
        OUTPUT,
        BOTH
    }

//    public Channel channel;

    public Mode mode;

    public Direction direction;

//    public SignalConstruct(Channel channel, Mode mode, Direction direction) {
//        this.channel = channel;
//        this.mode = mode;
//        this.direction = direction;
//    }

    public SignalConstruct(Mode mode, Direction direction) {
        this.mode = mode;
        this.direction = direction;
    }

}
