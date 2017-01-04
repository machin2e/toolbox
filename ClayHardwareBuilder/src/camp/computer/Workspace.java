package camp.computer;

import camp.computer.construct.DeviceConstruct;
import camp.computer.construct.ProjectConstruct;

import java.util.ArrayList;
import java.util.List;

public class Workspace {

    // TODO: Reference to User/Account that owns the Workspace.

    // <CACHE>
    public List<ProjectConstruct> projectConstructs = new ArrayList<>();
//    public List<DeviceConstruct> deviceConstructs = new ArrayList<>();
    // </CACHE>

    // <CONTEXT>
    ProjectConstruct projectConstruct = null;
    DeviceConstruct deviceConstruct = null;
    // </CONTEXT>

}
