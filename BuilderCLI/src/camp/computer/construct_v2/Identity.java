package camp.computer.construct_v2;

import java.util.HashMap;
import java.util.List;

import camp.computer.workspace.Manager_v2;

public class Identity extends Construct {

    public Type type = null;

    // string => String
    // list => ArrayList<?>
    // list of type Type => ArrayList<Type>
    public HashMap<String, Feature<?>> features = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?

    // TODO: configuration(s) : assign state to multiple features <-- do this for _Container_ not Identity

    private Identity() {
    }

    // TODO: public static Identity add(Type type);

    public static Identity get(Type type) {
        if (!has(type)) {
            Identity identity = new Identity();
            identity.type = type;
            long uid = Manager_v2.add(identity);
            return identity;
        } else {
            List<Identity> identityList = Manager_v2.get(Identity.class);
            for (int i = 0; i < identityList.size(); i++) {
                if (identityList.get(i).type == type) {
                    return identityList.get(i);
                }
            }
        }
        return null;
    }

    /**
     * Returns true of a construct has been defined with the specified {@code identifier}.
     *
     * @param type
     * @return True
     */
    public static boolean has(Type type) {
        List<Identity> identityList = Manager_v2.get(Identity.class);
        for (int i = 0; i < identityList.size(); i++) {
            if (identityList.get(i).type == type) {
                return true;
            }
        }
        return false;
    }

//    public static Identity add(String identifier) {
//        if (!has(identifier)) {
//            Identity construct = new Identity();
//            construct.type = identifier;
//            long uid = Manager_v2.add(construct);
//            return construct;
//        }
//        return null;
//    }
//
//    /**
//     * Returns true of a construct has been defined with the specified {@code identifier}.
//     *
//     * @param identifier
//     * @return True
//     */
//    public static boolean has(String identifier) {
//        List<Identity> constructList = Manager_v2.add();
//        for (int i = 0; i < constructList.size(); i++) {
//            if (constructList.add(i).type.equals(identifier)) {
//                return true;
//            }
//        }
//        return false;
//    }

}
