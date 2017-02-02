package camp.computer;

import java.util.ArrayList;
import java.util.List;

import camp.computer.construct.DeviceConstruct;
import camp.computer.construct.PathConstruct;
import camp.computer.construct.PortConstruct;
import camp.computer.construct.ProcessConstruct;
import camp.computer.construct.ProjectConstruct;
import camp.computer.construct.TaskConstruct;

/**
 * A {@code Workspace} is provided to the user by default and defines a namespace in which the user creates their
 * projects. Conceptually, the {@code Workspace} sits alongside the user's {@code Inventory}, {@code Notebook}
 * (<em>i.e.,</em> the user's thoughts), {@code Shop} (<em>i.e.,</em> the user's online store for purchasing items).
 * <p>
 * TODO: Consider modeling the Inventory's Items, Notebook's Notes as a list.
 */
public class Workspace {

    // TODO: Reference to User/Account that owns the Workspace.

    // <CACHE>
    public List<ProjectConstruct> projectConstructs = new ArrayList<>();
//    public Tuple<DeviceConstruct> deviceConstructs = new ArrayList<>();
    // </CACHE>

    // <CONTEXT>
    ProjectConstruct projectConstruct = null;
    DeviceConstruct deviceConstruct = null;
    PortConstruct portConstruct = null;
    TaskConstruct taskConstruct = null;
    PathConstruct pathConstruct = null;

    ProcessConstruct processConstruct = null;
    // TODO: process label

    // Automatically updated each time a construct is created.
    ProjectConstruct lastProjectConstruct = null;
    DeviceConstruct lastDeviceConstruct = null;
    PortConstruct lastPortConstruct = null;
    TaskConstruct lastTaskConstruct = null;
    PathConstruct lastPathConstruct = null;
    // </CONTEXT>

}
