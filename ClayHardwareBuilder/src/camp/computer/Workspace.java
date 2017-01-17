package camp.computer;

import camp.computer.construct.DeviceConstruct;
import camp.computer.construct.PortConstruct;
import camp.computer.construct.ProjectConstruct;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@code Workspace} is provided to the user by default and defines a namespace in which the user creates their
 * projects. Conceptually, the {@code Workspace} sits alongside the user's {@code Inventory}, {@code Notebook}
 * (<em>i.e.,</em> the user's thoughts), {@code Shop} (<em>i.e.,</em> the user's online store for purchasing items).
 *
 * TODO: Consider modeling the Inventory's Items, Notebook's Notes as a list.
 */
public class Workspace {

    // TODO: Reference to User/Account that owns the Workspace.

    // <CACHE>
    public List<ProjectConstruct> projectConstructs = new ArrayList<>();
//    public List<DeviceConstruct> deviceConstructs = new ArrayList<>();
    // </CACHE>

    // <CONTEXT>
    ProjectConstruct projectConstruct = null;
    DeviceConstruct deviceConstruct = null;
    PortConstruct portConstruct = null;
    // </CONTEXT>

}
