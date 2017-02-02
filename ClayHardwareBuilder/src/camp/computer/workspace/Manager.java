package camp.computer.workspace;

import java.util.HashMap;
import java.util.UUID;

import camp.computer.construct.Construct;

public class Manager {

    public static long elementCounter = 0L;

    public static HashMap<Long, Construct> elements = new HashMap<>();

    public static Construct getConstruct(String address) {

        // Parse:
        // 3
        // "foo"
        // uid(44)
        // uuid(a716a27b-8489-4bae-b099-2bc73e963876)

        if (address.startsWith("\"") && address.endsWith("\"")) {

            String title = address.substring(1, address.length() - 1);

            System.out.println("title: " + title);

            for (long uid : elements.keySet()) {
                if (elements.get(uid).title != null && elements.get(uid).title.equals(title)) {
                    System.out.println("found: " + elements.get(uid).uid);
                    return elements.get(uid);
                }
            }

        } else {

            if (address.startsWith("uid")) {

//                long inputTaskUid = Long.valueOf(address.split(":")[1]);
                long inputTaskUid = Long.valueOf(address.substring(address.indexOf("(") + 1, address.indexOf(")")));

                if (Manager.elements.containsKey(inputTaskUid)) {
                    return Manager.elements.get(inputTaskUid);
                }

//            for (int i = 0; i < workspace.projectConstruct.deviceConstructs.size(); i++) {
//                for (int j = 0; j < workspace.projectConstruct.deviceConstructs.get(i).scheduleConstruct.taskConstructs.size(); j++) {
//                    if (workspace.projectConstruct.deviceConstructs.get(i).scheduleConstruct.taskConstructs.get(j).uid == inputTaskUid) {
//                        workspace.taskConstruct = workspace.projectConstruct.deviceConstructs.get(i).scheduleConstruct.taskConstructs.get(j);
//                        break;
//                    }
//                }
//            }

            } else if (address.startsWith("uuid")) {

//                UUID inputTaskUuid = UUID.fromString(address.split(":")[1]);
                UUID inputTaskUuid = UUID.fromString(address.substring(address.indexOf("(") + 1, address.indexOf(")")));

                for (int i = 0; i < Manager.elements.size(); i++) {
                    if (Manager.elements.get(i).uuid.equals(inputTaskUuid)) {
                        return Manager.elements.get(i);
                    }
                }

//            for (int i = 0; i < workspace.projectConstruct.deviceConstructs.size(); i++) {
//                for (int j = 0; j < workspace.projectConstruct.deviceConstructs.get(i).scheduleConstruct.taskConstructs.size(); j++) {
//                    if (workspace.projectConstruct.deviceConstructs.get(i).scheduleConstruct.taskConstructs.get(j).uuid.equals(inputTaskUuid)) {
//                        workspace.taskConstruct = workspace.projectConstruct.deviceConstructs.get(i).scheduleConstruct.taskConstructs.get(j);
//                        break;
//                    }
//                }
//            }

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

        }

        return null;

    }

}
