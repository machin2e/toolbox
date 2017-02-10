package camp.computer.construct;

import java.util.HashMap;
import java.util.UUID;

import camp.computer.data.format.configuration.Variable;
import camp.computer.workspace.Manager;

public abstract class Construct {

    public static final String DEFAULT_CONSTRUCT_TYPE = "construct";

    public UUID uuid = UUID.randomUUID(); // universal identifier (unique among all in central repo)

    public long uid = Manager.elementCounter++; // manager/cache UID

    public String type = DEFAULT_CONSTRUCT_TYPE; // type identifier of construct

    public String title = null; // label/tag(s)

    public HashMap<String, Variable> variables = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?


    // + VARIABLES/FEATURES/PROPERTIES (list, string, link to construct, )
    //
    //   new port / dir port / def port / for port # Create construct in repository or find existing identical match and return UUID.
    //   let mode : string : "digital", "analog", ... # if only one value, auto-assign it upon use of construct
    //   let direction : string : "none", "input", "output", "bidirectional"
    //   let voltage : string : "cmos", "ttl"
    //   set mode:digital;direction;output;voltage:cmos,ttl # "set" in the context of "new" adds an allowed configuration
    //   but mode:digital;direction;output;voltage:cmos,ttl
    //     support mode:digital;direction;output;voltage:cmos,ttl
    //     for mode:digital;direction;output;voltage:cmos,ttl
    //     add setting : mode:digital;direction;output;voltage:cmos,ttl
    //     add setting : mode:analog;direction;input;voltage:ttl
    //   done / save / commit
    //
    //   use port (uuid:<uuid>) / edit port (...) # Creates INSTANCE based on the CONSTRUCT revision <uuid>
    //   set mode : "digital"
    //   set direction : "output"
    //   set voltage : "cmos"
    //   done / commit
    //
    //   add configuration mode:digital;direction;output;voltage:cmos
    //      put configuration mode:digital;direction;output;voltage:cmos
    //
    //   let source-port : port
    //   set source-port : port(uid:34)
    //
    //   let port : list
    //   add port : port(uid:44) # adds the port to the list "port"
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
