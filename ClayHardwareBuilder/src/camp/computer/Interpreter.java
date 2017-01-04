package camp.computer;

import camp.computer.construct.DeviceConstruct;
import camp.computer.construct.PathConstruct;
import camp.computer.construct.PortConstruct;
import camp.computer.construct.ProjectConstruct;

import java.util.Scanner;

public class Interpreter {

    Workspace workspace = new Workspace();

    public void start() {

        Scanner scanner = new Scanner(System.in);
        String inputLine = null;

        while(true) {

            // TODO: add path <sourcePortUid>, <targetPortUid>
            // TODO: set port supported modes
            // TODO: save project (for user account)
            // TODO: load project (for user account)
            // TODO: remove project, device, port
            // TODO: show workspace, project, device, port
            // TODO: clone (add from description by UUID, or at uri/path) project, device
            // TODO: Explore/Search devices

            // TODO: Redis
            // TODO: Timeline (of commands, so can reconstruct)
            // TODO: IASM: Generate instructions, track completion status of each step!
            // TODO: Users: Workspace, Projects, Portfolio (not separate object, just filter shared projects)

            // TODO: Create "host/device server" that announces its presence and allows retrieval of its HDL, specs.

            inputLine = scanner.nextLine().trim();

            if (inputLine.equals("add project")) {

                createProjectTask();

            } else if (inputLine.equals("list projects")) {

                listProjectsTask();

            } else if (inputLine.startsWith("choose project")) {

                chooseProjectTask(inputLine);

            } else if (inputLine.startsWith("change project title")) {

                changeProjectTitleTask(inputLine);

            } else if (inputLine.equals("add device")) { // create hardware

                createDeviceTask();

            } else if (inputLine.equals("list devices")) { // list hardware

                listDevicesTask();

            } else if (inputLine.startsWith("choose device")) { // select hardware

                chooseDeviceTask(inputLine);

            } else if (inputLine.startsWith("add port")) { // create port

                createPortTask();

            } else if (inputLine.startsWith("list ports")) {

                listPortsTask();

            } else if (inputLine.startsWith("add path")) { // add path device 1 port 3 device 4 port 1

                createPathTask(inputLine);

            } else if (inputLine.startsWith("list paths")) {

                listPathsTask();

            } else if (inputLine.startsWith("close")) {

                closeTask();

            }

        }

    }

    // <REFACTOR>
    // TODO: Create "Command" class with command (1) keywords and (2) task to handle command.

    public void createProjectTask() {
        ProjectConstruct projectConstruct = new ProjectConstruct();
        System.out.println("> created project " + projectConstruct.uid);

        workspace.projectConstructs.add(projectConstruct);
    }

    public void listProjectsTask() {

        if (workspace.projectConstructs.size() == 0) {
            System.out.println("none");
        } else {
            for (int i = 0; i < workspace.projectConstructs.size(); i++) {
                System.out.print("" + workspace.projectConstructs.get(i).uid);

                if (workspace.projectConstructs.get(i).deviceConstructs.size() > 0) {
                    System.out.print(" (" + workspace.projectConstructs.get(i).deviceConstructs.size() + " devices, " + workspace.projectConstructs.get(i).pathConstructs.size() + " paths)");
                }

                System.out.println();
            }
        }

    }

    public void chooseProjectTask(String context){
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String inputLine = context;
        String[] inputLineWords = inputLine.split("[ ]+");

        long inputProjectUid = Long.valueOf(inputLineWords[2]);

        for (int i = 0; i < workspace.projectConstructs.size(); i++) {
            if (workspace.projectConstructs.get(i).uid == inputProjectUid) {
                workspace.projectConstruct = workspace.projectConstructs.get(i);
                break;
            }
        }

        System.out.println("> chose project " + inputProjectUid);

    }

    public void changeProjectTitleTask(String context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        if (workspace.projectConstruct != null) {

            String inputLine = context;
            String[] inputLineWords = inputLine.split("[ ]+");

            String inputProjectTitle = inputLineWords[3];

            workspace.projectConstruct.title = inputProjectTitle;

            System.out.println("> project title changed to " + inputProjectTitle);
        }

    }

    public void createDeviceTask() {

        DeviceConstruct deviceConstruct = new DeviceConstruct();
        System.out.println("> created device " + deviceConstruct.uid);

        workspace.projectConstruct.deviceConstructs.add(deviceConstruct);

    }

    public void listDevicesTask() {

        if (workspace.projectConstruct.deviceConstructs.size() == 0) {
            System.out.println("none");
        } else {
            for (int i = 0; i < workspace.projectConstruct.deviceConstructs.size(); i++) {
                System.out.print("" + workspace.projectConstruct.deviceConstructs.get(i).uid);

                if (workspace.projectConstruct.deviceConstructs.get(i).portConstructs.size() > 0) {
                    System.out.print(" (" + workspace.projectConstruct.deviceConstructs.get(i).portConstructs.size() + " ports)");
                }

                System.out.println();
            }
        }

    }

    public void chooseDeviceTask(String context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String inputLine = context;
        String[] inputLineWords = inputLine.split("[ ]+");

        long inputDeviceUid = Long.valueOf(inputLineWords[2]);

        for (int i = 0; i < workspace.projectConstruct.deviceConstructs.size(); i++) {
            if (workspace.projectConstruct.deviceConstructs.get(i).uid == inputDeviceUid) {
                workspace.deviceConstruct = workspace.projectConstruct.deviceConstructs.get(i);
                break;
            }
        }

        System.out.println("> chose device " + inputDeviceUid);

    }

    public void createPortTask() {

        if (workspace.deviceConstruct != null) {

            PortConstruct portConstruct = new PortConstruct();
            System.out.println("> created port " + portConstruct.uid + " on device " + workspace.deviceConstruct.uid);

            workspace.deviceConstruct.portConstructs.add(portConstruct);

        }

    }

    public void listPortsTask() {

        if (workspace.deviceConstruct != null) {

            for (int i = 0; i < workspace.deviceConstruct.portConstructs.size(); i++) {
                System.out.println("" + workspace.deviceConstruct.portConstructs.get(i).uid);
            }

        }

    }

    // e.g., add path device 1 port 3 device 4 port 1
    public void createPathTask(String context) {

        if (workspace.deviceConstruct != null) {

            String inputLine = context;
            String[] inputLineWords = inputLine.split("[ ]+");

            long inputSourceDeviceUid = Long.valueOf(inputLineWords[3]);
            long sourcePortUid = Long.valueOf(inputLineWords[5]);
            long targetDeviceUid = Long.valueOf(inputLineWords[7]);
            long targetPortUid = Long.valueOf(inputLineWords[9]);

            DeviceConstruct sourceDeviceConstruct = null;
            for (int i = 0; i < workspace.projectConstruct.deviceConstructs.size(); i++) {
                if (workspace.projectConstruct.deviceConstructs.get(i).uid == inputSourceDeviceUid) {
                    sourceDeviceConstruct = workspace.projectConstruct.deviceConstructs.get(i);
                    break;
                }
            }


            PortConstruct sourcePortConstruct = null;
            for (int i = 0; i < sourceDeviceConstruct.portConstructs.size(); i++) {
                if (sourceDeviceConstruct.portConstructs.get(i).uid == sourcePortUid) {
                    sourcePortConstruct = sourceDeviceConstruct.portConstructs.get(i);
                    break;
                }
            }

            DeviceConstruct targetDeviceConstruct = null;
            for (int i = 0; i < workspace.projectConstruct.deviceConstructs.size(); i++) {
                if (workspace.projectConstruct.deviceConstructs.get(i).uid == targetDeviceUid) {
                    targetDeviceConstruct = workspace.projectConstruct.deviceConstructs.get(i);
                    break;
                }
            }


            PortConstruct targetPortConstruct = null;
            for (int i = 0; i < targetDeviceConstruct.portConstructs.size(); i++) {
                if (targetDeviceConstruct.portConstructs.get(i).uid == targetPortUid) {
                    targetPortConstruct = targetDeviceConstruct.portConstructs.get(i);
                    break;
                }
            }

            PathConstruct pathConstruct = new PathConstruct();
            pathConstruct.sourcePort = sourcePortConstruct;
            pathConstruct.targetPort = targetPortConstruct;

            workspace.projectConstruct.pathConstructs.add(pathConstruct);

            System.out.println("> created path " + pathConstruct.uid + " from device " + sourceDeviceConstruct.uid + " port " + sourcePortConstruct.uid + " to device " + targetDeviceConstruct.uid + " port " + targetPortConstruct.uid);
        }

    }

    public void listPathsTask() {

        if (workspace.projectConstruct != null) {

            for (int i = 0; i < workspace.projectConstruct.pathConstructs.size(); i++) {
                System.out.println("" + workspace.projectConstruct.pathConstructs.get(i).uid + " (port " + workspace.projectConstruct.pathConstructs.get(i).sourcePort.uid + ", port " + workspace.projectConstruct.pathConstructs.get(i).targetPort.uid + ")");
            }

        }

    }

    public void closeTask() {
        System.exit(0);
    }
    // </REFACTOR>
}


// TODO: PORTS CAN BE "WIRELESS" AND SPREAD OUT ACROSS BOARDS JUST LIKE CLAY BOARDS CAN BE SPREAD OUT IN SPACE.
