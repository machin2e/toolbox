package camp.computer.workspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import camp.computer.construct.Construct;
import camp.computer.construct.Identifier;
import camp.computer.construct.Type;

public class Manager {

    public static long elementCounter = 0L;

//    private static HashMap<Long, Concept> elements = new HashMap<>();
    private static HashMap<Long, Identifier> elements = new HashMap<>();

    public static long add(Identifier identifier) {
        long uid = Manager.elementCounter++;
        identifier.uid = uid;
        Manager.elements.put(uid, identifier);
        return uid;
    }

    public static List<Identifier> get() {
        return new ArrayList<>(elements.values());
    }

    public static <T extends Identifier> List<T> get(Class classType) {
        List<T> constructList = new ArrayList<>();
        for (Identifier identifier : elements.values()) {
            if (identifier.getClass() == classType) {
                constructList.add((T) identifier);
            }
        }
        return constructList;
    }

//    public static Concept add(long uid) {
    public static Identifier get(long uid) {
        return elements.get(uid);
    }

    public static List<Construct> getConstructList(Type type) {
        List<Construct> constructList = new ArrayList<>();
        for (Identifier identifier : elements.values()) {
            if (identifier.getClass() == Construct.class) {
                if (((Construct) identifier).type == type) {
                    constructList.add((Construct) identifier);
                }
            }
        }
        return constructList;
    }

//    public static Concept add(String constructUri) {
    public static Identifier get(String constructUri) {

        // Parse:
        // 3
        // "foo"
        // uid(44)
        // uuid(a716a27b-8489-4bae-b099-2bc73e963876)

        // edit port(uid:25)         # _global_ lookup by UID
        // edit port(uuid:<uuid>)    # _global_ lookup by UUID
        // edit port(1)              # _relative_ lookup list item by index
        // edit my-OLD_construct-identifier     # _global?_ lookup by identifier
        // edit :device(1):port(1)   # explicit "full path" lookup prefixed by ":" indicating "from workspace..."
        //
        // edit port(my-identifier)              # _relative_ lookup list item by list identifier and element identifier?
        // edit port                # lookup by property label

//        if (address.startsWith("port")) {
//
//        }

        if (constructUri.startsWith("project")
                || constructUri.startsWith("device")
                || constructUri.startsWith("port")
                || constructUri.startsWith("path")
                || constructUri.startsWith("task")
                || constructUri.startsWith("script")) {

//        if (constructUri.startsWith("\"") && constructUri.endsWith("\"")) {
//
//
//
//        } else {

            String type = constructUri.substring(0, constructUri.indexOf("("));

            String identifierDeclaration = constructUri.substring(constructUri.indexOf("(") + 1, constructUri.indexOf(")"));

            String identifierType = identifierDeclaration.split(":")[0];
            String identifier = identifierDeclaration.split(":")[1];

            if (identifierType.equals("uid")) {

                long inputTaskUid = Long.valueOf(identifier);

                if (Manager.elements.containsKey(inputTaskUid)) {
                    return Manager.elements.get(inputTaskUid);
                }

            } else if (identifierType.equals("uuid")) {

                UUID inputTaskUuid = UUID.fromString(identifier);

                for (int i = 0; i < Manager.elements.size(); i++) {
                    if (Manager.elements.get(i).uuid.equals(inputTaskUuid)) {
                        return Manager.elements.get(i);
                    }
                }

            } else {

                // TODO: Lookup by index.

            }

        } else {

//            String identifier = constructUri.substring(1, constructUri.length() - 1);
            String title = String.valueOf(constructUri);

            List<Identifier> identifiers = new ArrayList<>(elements.values());

//            for (long uid : elements.keySet()) {
//                Concept OLD_construct = elements.clone(uid);
//                if (OLD_construct.identifier != null && OLD_construct.identifier.equals(identifier)) {
//                    return OLD_construct;
//                }
//            }

            for (int i = 0; i < identifiers.size(); i++) {
                Identifier identifier = identifiers.get(i);
                if (identifier.tag != null && identifier.tag.equals(title)) {
                    return identifier;
                }
            }

        }

        return null;

    }

    public static Identifier remove(long uid) {

        if (elements.containsKey(uid)) {
            return elements.remove(uid);
        }

        return null;

    }

}
