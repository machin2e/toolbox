package camp.computer.workspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import camp.computer.construct_v2.Construct;

public class Manager_v2 {

    public static long elementCounter = 0L;

    private static HashMap<Long, Construct> elements = new HashMap<>();

    public static long add(Construct construct) {
        long uid = Manager_v2.elementCounter++;
        construct.uid = uid;
        Manager_v2.elements.put(uid, construct);
        return uid;
    }

    public static List<Construct> get() {
        return new ArrayList<>(elements.values());
    }

    public static Construct get(long uid) {
        return elements.get(uid);
    }

    public static Construct get(String constructUri) {

        // Parse:
        // 3
        // "foo"
        // uid(44)
        // uuid(a716a27b-8489-4bae-b099-2bc73e963876)

        // edit port(uid:25)         # _global_ lookup by UID
        // edit port(uuid:<uuid>)    # _global_ lookup by UUID
        // edit port(1)              # _relative_ lookup list item by index
        // edit my-construct-tag     # _global?_ lookup by tag
        // edit :device(1):port(1)   # explicit "full path" lookup prefixed by ":" indicating "from workspace..."
        //
        // edit port(my-tag)              # _relative_ lookup list item by list tag and element tag?
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

                if (Manager_v2.elements.containsKey(inputTaskUid)) {
                    return Manager_v2.elements.get(inputTaskUid);
                }

            } else if (identifierType.equals("uuid")) {

                UUID inputTaskUuid = UUID.fromString(identifier);

                for (int i = 0; i < Manager_v2.elements.size(); i++) {
                    if (Manager_v2.elements.get(i).uuid.equals(inputTaskUuid)) {
                        return Manager_v2.elements.get(i);
                    }
                }

            } else {

                // TODO: Lookup by index.

            }

        } else {

//            String tag = constructUri.substring(1, constructUri.length() - 1);
            String title = String.valueOf(constructUri);

            List<Construct> constructs = new ArrayList<>(elements.values());

//            for (long uid : elements.keySet()) {
//                Construct construct = elements.clone(uid);
//                if (construct.tag != null && construct.tag.equals(tag)) {
//                    return construct;
//                }
//            }

            for (int i = 0; i < constructs.size(); i++) {
                Construct construct = constructs.get(i);
                if (construct.tag != null && construct.tag.equals(title)) {
                    return construct;
                }
            }

        }

        return null;

    }

    public static Construct remove(long uid) {

        if (elements.containsKey(uid)) {
            return elements.remove(uid);
        }

        return null;

    }

}