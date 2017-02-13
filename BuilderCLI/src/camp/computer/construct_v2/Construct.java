package camp.computer.construct_v2;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import camp.computer.workspace.Manager_v2;

public class Construct {

    private static HashMap<String, Type> constructs = new HashMap<>();

    public static final String DEFAULT_CONSTRUCT_TYPE = "container";

    public UUID uuid = UUID.randomUUID(); // universal identifier (unique among all in central repo)

    public long uid = -1L; // Manager.elementCounter++; // manager/cache UID

    public String type = DEFAULT_CONSTRUCT_TYPE; // type identifier of construct

    public String tag = null; // label/tag(s)

    // string => String
    // list => ArrayList<?>
    // list of type Type => ArrayList<Type>
    public HashMap<String, Feature<?>> features = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?

    // TODO: configuration(s) : assign state to multiple features <-- do this for _Container_ not Construct

    private Construct() {
    }

    public static Construct getConstruct(String identifier) {
        if (!hasConstruct(identifier)) {
            Construct construct = new Construct();
            construct.type = identifier;
            long uid = Manager_v2.add(construct);
            return construct;
        }
        return null;
    }

    /**
     * Returns true of a construct has been defined with the specified {@code identifier}.
     *
     * @param identifier
     * @return True
     */
    public static boolean hasConstruct(String identifier) {
        List<Construct> constructList = Manager_v2.get();
        for (int i = 0; i < constructList.size(); i++) {
            if (constructList.get(i).type.equals(identifier)) {
                return true;
            }
        }
        return false;
    }

}
