package camp.computer.construct;

import java.util.HashMap;
import java.util.List;

import camp.computer.workspace.Manager;

public class Concept extends Identifier {

    public Type type = null;

    // string => String
    // list => ArrayList<?>
    // list of type Type => ArrayList<Type>
    public HashMap<String, Feature> features = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?

    // TODO: configuration(s) : assign state to multiple features <-- do this for _Container_ not Concept

    private Concept() {
    }

    public static Concept add(Type type) {
        if (!has(type)) {
            Concept concept = new Concept();
            concept.type = type;
            long uid = Manager.add(concept);
            return concept;
        } else {
            List<Concept> conceptList = Manager.get(Concept.class);
            for (int i = 0; i < conceptList.size(); i++) {
                if (conceptList.get(i).type == type) {
                    return conceptList.get(i);
                }
            }
        }
        return null;
    }

    public static Concept get(Type type) {
        if (has(type)) {
            List<Concept> conceptList = Manager.get(Concept.class);
            for (int i = 0; i < conceptList.size(); i++) {
                if (conceptList.get(i).type == type) {
                    return conceptList.get(i);
                }
            }
        }
        return null;
    }

    /**
     * Returns true of a OLD_construct has been defined with the specified {@code identifier}.
     *
     * @param type
     * @return True
     */
    public static boolean has(Type type) {
        List<Concept> conceptList = Manager.get(Concept.class);
        for (int i = 0; i < conceptList.size(); i++) {
            if (conceptList.get(i).type == type) {
                return true;
            }
        }
        return false;
    }

//    public static Concept add(String identifier) {
//        if (!has(identifier)) {
//            Concept OLD_construct = new Concept();
//            OLD_construct.type = identifier;
//            long uid = Manager.add(OLD_construct);
//            return OLD_construct;
//        }
//        return null;
//    }
//
//    /**
//     * Returns true of a OLD_construct has been defined with the specified {@code identifier}.
//     *
//     * @param identifier
//     * @return True
//     */
//    public static boolean has(String identifier) {
//        List<Concept> constructList = Manager.add();
//        for (int i = 0; i < constructList.size(); i++) {
//            if (constructList.add(i).type.equals(identifier)) {
//                return true;
//            }
//        }
//        return false;
//    }

}
