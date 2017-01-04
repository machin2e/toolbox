package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

public class DeviceConstruct {

    public static long constructCounter = 0L;

    public long uid = constructCounter++;

    // HostConstruct
    // DeviceDescription, HardwarePilot/HardwareBuilder

    // TODO: Ports, PortConstruct

    // TODO: Specs

    public List<PortConstruct> portConstructs = new ArrayList<>();

}
