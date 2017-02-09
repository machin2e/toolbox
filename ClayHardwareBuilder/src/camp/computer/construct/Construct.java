package camp.computer.construct;

import java.util.UUID;

import camp.computer.workspace.Manager;

public abstract class Construct {

    public static final String DEFAULT_CONSTRUCT_TYPE = "construct";

    public UUID uuid = UUID.randomUUID(); // universal identifier (unique among all in central repo)

    public long uid = Manager.elementCounter++; // manager/cache UID

    public String type = DEFAULT_CONSTRUCT_TYPE; // type identifier of construct

    public String title = null; // label/tag(s)


    // + VARIABLES/FEATURES/PROPERTIES (list, string, link to construct, )
    //
    //   set mode : string
    //   set direction : string
    //   set voltage : string
    //
    //   set ports : list(type:port)
    //      set type ports (port)
    //      configure ports (type:port)
    //      bind ports type:port
    //      limit ports type:port
    //      filter ports type:port
    //      control ports type:port
    //      restrict ports type:port
    //   set controller : link(type:controller)
    //      restrict link type:port
    //
    //   set source-port : link(type:port)
    //   set target-port : link(type:port)
    //
    //   set tasks : list(type:task)
    //
    //
    //
    // + CONFIGURATIONS/ASSIGNMENTS


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
