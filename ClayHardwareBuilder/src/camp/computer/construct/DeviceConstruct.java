package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

public class DeviceConstruct extends Construct {

    public List<PortConstruct> portConstructs = new ArrayList<>();

    // <REFACTOR>
    // TODO: Consider replacing with List<TaskConstruct>
    public ScheduleConstruct scheduleConstruct = new ScheduleConstruct();
    // <REFACTOR>

    public DeviceConstruct() {
        super();
    }

}
