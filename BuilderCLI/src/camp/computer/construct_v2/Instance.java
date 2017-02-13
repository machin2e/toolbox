package camp.computer.construct_v2;

import java.util.HashMap;
import java.util.UUID;

import camp.computer.workspace.Manager;

public class Instance {

    public static final String DEFAULT_CONSTRUCT_TYPE = "container";

    public UUID uuid = UUID.randomUUID(); // universal identifier (unique among all in central repo)

    public long uid = Manager.elementCounter++; // manager/cache UID

    public String type = DEFAULT_CONSTRUCT_TYPE; // type identifier of construct

    public String tag = null; // label/tag(s)

    // string => String
    // list => ArrayList<?>
    // list of type Type => ArrayList<Type>
    public HashMap<String, Feature<?>> features = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?

    // TODO: configuration(s) : assign state to multiple features <-- do this for _Container_ not Construct


    public Instance() {
//        Manager.elements.put(uid, this);
    }

}
