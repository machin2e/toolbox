package camp.computer.construct;

import java.util.UUID;

import camp.computer.workspace.Manager;

public abstract class Construct {

    public long uid = Manager.elementCounter++;

    // Identity:
    public UUID uuid = UUID.randomUUID();

    // <COMPONENT>
    public String title = null; // title/tag(s)
    // </COMPONENT>

    // HashMap<String, List<Construct>> constructs;

    /*
    public List<PortConstruct> portConstructs = new ArrayList<>();
    public ControllerConstruct controllerConstruct = new ControllerConstruct();

    public PortConstruct sourcePortConstruct = null;
    public PortConstruct targetPortConstruct = null;

    public HashMap<String, Variable> variables = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?
    public List<Configuration> configurations = new ArrayList<>();

    public List<DeviceConstruct> deviceConstructs = new ArrayList<>();
    public List<PathConstruct> pathConstructs = new ArrayList<>();

    public List<TaskConstruct> taskConstructs = new ArrayList<>();

    public String text = null;

    public ScriptConstruct scriptConstruct = null;
    */

    // TODO: type (generic type identifier so can use single construct)
    // TODO: HashMap<?> variables (can be Construct or other values)
    // Construct (Prototype) -> Container (must specify type or is anonymous) -> Container Revision

    // Structure/Links:
    // TODO: parent
    // TODO: siblings
    // TODO: connections (per-construct links)
    // TODO: children

    // TODO: previousVersion
    // TODO: nextVersions (maybe just use previousVersion)

    // TODO: variables/features/properties/states
    // TODO: configuration(s) : assign state to multiple variables <-- do this for _Container_ not Construct


    public Construct() {
        Manager.elements.put(uid, this);
    }

}
