package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

import static camp.computer.construct.PortConstruct.Type.CONTINUOUS;
import static camp.computer.construct.PortConstruct.Type.DISCRETE;
import static camp.computer.construct.PortConstruct.Type.NONE;

public class PortConstruct {

    public long uid = DeviceConstruct.constructCounter++;

    // PortDescription, PortPilot/PortBuilder

    public enum Type {
        NONE,
        DISCRETE,
        CONTINUOUS,
        MODULATING
    }

    public List<Type> typeSet = new ArrayList<>();

    public Type type = NONE;

    public PortConstruct() {

        // Initialize supported types
        typeSet.add(NONE);
        typeSet.add(DISCRETE);
        typeSet.add(CONTINUOUS);

        // Initialize default type
        type = DISCRETE;

    }

}
