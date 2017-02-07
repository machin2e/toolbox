package camp.computer.workspace;

import java.util.HashMap;
import java.util.UUID;

import camp.computer.construct.Construct;

public class Manager {

    public static long elementCounter = 0L;

    public static HashMap<Long, Construct> elements = new HashMap<>();

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

        if (constructUri.startsWith("\"") && constructUri.endsWith("\"")) {

            String title = constructUri.substring(1, constructUri.length() - 1);

            for (long uid : elements.keySet()) {
                if (elements.get(uid).title != null && elements.get(uid).title.equals(title)) {
                    return elements.get(uid);
                }
            }

        } else {

            String type = constructUri.substring(0, constructUri.indexOf("("));

            String identifierDeclaration = constructUri.substring(constructUri.indexOf("(") + 1, constructUri.indexOf(")"));

            String identifierType = identifierDeclaration.split(":")[0];
            String identifier = identifierDeclaration.split(":")[1];

            if (identifierType.equals("uid")) {

//                long inputTaskUid = Long.valueOf(address.substring(address.indexOf("(") + 1, address.indexOf(")")));
                long inputTaskUid = Long.valueOf(identifier);

                if (Manager.elements.containsKey(inputTaskUid)) {
                    return Manager.elements.get(inputTaskUid);
                }

            } else if (identifierType.equals("uuid")) {

//                UUID inputTaskUuid = UUID.fromString(address.substring(address.indexOf("(") + 1, address.indexOf(")")));
                UUID inputTaskUuid = UUID.fromString(identifier);

                for (int i = 0; i < Manager.elements.size(); i++) {
                    if (Manager.elements.get(i).uuid.equals(inputTaskUuid)) {
                        return Manager.elements.get(i);
                    }
                }

            } else {

                // TODO: Lookup by index.

                /*
                long inputDeviceUid = Long.valueOf(inputDeviceIdentifier.split(":")[1]);

                for (int i = 0; i < workspace.projectConstruct.deviceConstructs.size(); i++) {
                    if (workspace.projectConstruct.deviceConstructs.get(i).uid == inputDeviceUid) {
                        workspace.deviceConstruct = workspace.projectConstruct.deviceConstructs.get(i);
                        break;
                    }
                }
                */

            }

//            if (address.startsWith("uid")) {
//
//                long inputTaskUid = Long.valueOf(address.substring(address.indexOf("(") + 1, address.indexOf(")")));
//
//                if (Manager.elements.containsKey(inputTaskUid)) {
//                    return Manager.elements.get(inputTaskUid);
//                }
//
//            } else if (address.startsWith("uuid")) {
//
//                UUID inputTaskUuid = UUID.fromString(address.substring(address.indexOf("(") + 1, address.indexOf(")")));
//
//                for (int i = 0; i < Manager.elements.size(); i++) {
//                    if (Manager.elements.get(i).uuid.equals(inputTaskUuid)) {
//                        return Manager.elements.get(i);
//                    }
//                }
//
//            } else {
//
//                // TODO: Lookup by index.
//
//                /*
//                long inputDeviceUid = Long.valueOf(inputDeviceIdentifier.split(":")[1]);
//
//                for (int i = 0; i < workspace.projectConstruct.deviceConstructs.size(); i++) {
//                    if (workspace.projectConstruct.deviceConstructs.get(i).uid == inputDeviceUid) {
//                        workspace.deviceConstruct = workspace.projectConstruct.deviceConstructs.get(i);
//                        break;
//                    }
//                }
//                */
//
//            }

        }

        return null;

    }

}
