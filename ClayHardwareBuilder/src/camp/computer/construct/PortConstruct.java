package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

public class PortConstruct {

    public long uid = DeviceConstruct.constructCounter++;

    // PortDescription, PortPilot/PortBuilder

    public List<SignalConstruct.Channel> channels = new ArrayList<>();
    public List<SignalConstruct.Mode> modes = new ArrayList<>();
    public List<SignalConstruct.Direction> directions = new ArrayList<>();

    public SignalConstruct.Channel channel = SignalConstruct.Channel.NONE;
    public SignalConstruct.Mode mode = SignalConstruct.Mode.NONE;
    public SignalConstruct.Direction direction = SignalConstruct.Direction.NONE;

    public PortConstruct() {

        // Initialize supported channels
        channels.add(SignalConstruct.Channel.ELECTRONIC);

        // Initialize default channel
        channel = SignalConstruct.Channel.ELECTRONIC;

        // Initialize supported types
        modes.add(SignalConstruct.Mode.NONE);
        modes.add(SignalConstruct.Mode.DISCRETE);
        modes.add(SignalConstruct.Mode.CONTINUOUS);

        // Initialize default type
        mode = SignalConstruct.Mode.NONE;

        // Initialize supported directions
        directions.add(SignalConstruct.Direction.NONE);
        directions.add(SignalConstruct.Direction.INPUT);
        directions.add(SignalConstruct.Direction.OUTPUT);
        directions.add(SignalConstruct.Direction.BOTH);

        // Initialize default direction
        direction = SignalConstruct.Direction.NONE;

    }

}
