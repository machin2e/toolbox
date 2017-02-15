package camp.computer.construct_v2;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import camp.computer.workspace.Manager;
import camp.computer.workspace.Manager_v2;

public class Instance extends Construct {

    // Identity -> Feature : Instance -> Content

//    public UUID uuid = UUID.randomUUID(); // universal identifier (unique among all in central repo)

//    public long uid = Manager.elementCounter++; // manager/cache UID

    public Type type = null;

//    public String tag = null; // label/tag(s)

    // string => String
    // list => ArrayList<?>
    // list of type Type => ArrayList<Type>
    public HashMap<String, Content> contents = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?

    // TODO: configuration(s) : assign state to multiple features <-- do this for _Container_ not Identity


    private Instance(Identity identity) {

        this.type = identity.type;

        // TODO: Create actual feature data structures!
        for (Feature feature : identity.features.values()) {
//        for (int i = 0; i < identity.features.size(); i++) {
//            Feature feature = identity.features.get(i);
            Content content = new Content(feature);
            contents.put(content.tag, content);

//            Class classType = null;
//            if (feature.type == Type.add("none")) {
//                classType = null;
//            } else if (feature.type == Type.add("text")) {
//                classType = String.class;
//                Content<String>
//            } else if (feature.type == Type.add("list")) {
//                classType = List.class;
//            }

        }

    }

    public static Instance add(Type type) {
        if (Identity.has(type)) {
            Identity identity = Identity.get(type);
            Instance instance = new Instance(identity);
            long uid = Manager_v2.add(instance);
            return instance;
        }
        return null;
    }

//    /**
//     * Returns true of a construct has been defined with the specified {@code identifier}.
//     *
//     * @param type
//     * @return True
//     */
//    public static boolean has(Type type) {
//        List<Identity> identityList = Manager_v2.add();
//        for (int i = 0; i < identityList.size(); i++) {
//            if (identityList.add(i).type == type) {
//                return true;
//            }
//        }
//        return false;
//    }

    public static <T> void set(T content) {

    }

    public static void add(String featureTag) {

    }

}
