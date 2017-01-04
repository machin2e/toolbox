package camp.computer.data.format;

public class PortTypeDescriptor {

    // data <--> construct ==> project [~~> project instance]

    public enum Medium {
        NONE,
        ELECTRONIC,
        BLUETOOTH_LE,
        THREAD,
        INTERNET
    }

    public enum Mode {
        NONE,
        DIGITAL,
        PWM,
        ANALOG,
        COMMON,
        POWER
    }

    public enum Direction {
        NONE,
        INPUT,
        OUTPUT,
        BOTH
    }

    public Medium medium;

    public Mode mode;

    public Direction direction;

    public PortTypeDescriptor(Medium medium, Mode mode, Direction direction) {
        this.medium = medium;
        this.mode = mode;
        this.direction = direction;
    }

}
