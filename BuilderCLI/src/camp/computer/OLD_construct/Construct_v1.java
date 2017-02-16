package camp.computer.OLD_construct;

import java.util.HashMap;
import java.util.UUID;

import camp.computer.construct.Feature;
import camp.computer.workspace.Manager_v1;

public abstract class Construct_v1 {

    public static final String DEFAULT_CONSTRUCT_TYPE = "construct";

    public UUID uuid = UUID.randomUUID(); // universal identifier (unique among all in central repo)

    public long uid = Manager_v1.elementCounter++; // manager/cache UID

    public String type = DEFAULT_CONSTRUCT_TYPE; // type identifier of OLD_construct

    public String title = null; // label/tag(s)

    // string => String
    // list => ArrayList<?>
    // list of type Type => ArrayList<Type>
    public HashMap<String, Feature<?>> features = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?

    // HashMap<String, List<Identity>> constructs;

    /*
    public List<PortConstruct> portConstructs = new ArrayList<>();
    public ControllerConstruct controllerConstruct = new ControllerConstruct();

    public PortConstruct sourcePortConstruct = null;
    public PortConstruct targetPortConstruct = null;

    public HashMap<String, Variable> features = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?
    public List<Configuration> configurations = new ArrayList<>();

    public List<DeviceConstruct> deviceConstructs = new ArrayList<>();
    public List<PathConstruct> pathConstructs = new ArrayList<>();

    public List<TaskConstruct> taskConstructs = new ArrayList<>();

    public String text = null;

    public ScriptConstruct scriptConstruct = null;
    */

    // TODO: type (generic type identifier so can use single OLD_construct)
    // TODO: HashMap<?> features (can be Identity or other values)
    // Identity (Prototype) -> Container (must specify type or is anonymous) -> Container Revision

    // Structure/Links:
    // TODO: parent
    // TODO: siblings
    // TODO: connections (per-OLD_construct links)
    // TODO: children

    // TODO: previousVersion
    // TODO: nextVersions (maybe just use previousVersion)

    // TODO: features/features/properties/states
    // TODO: configuration(s) : assign state to multiple features <-- do this for _Container_ not Identity


    public Construct_v1() {
        Manager_v1.elements.put(uid, this);
    }

}
