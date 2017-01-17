package camp.computer;

import camp.computer.construct.DeviceConstruct;
import camp.computer.construct.PathConstruct;
import camp.computer.construct.PortConstruct;
import camp.computer.construct.ProjectConstruct;
import camp.computer.data.format.configuration.PortConfiguration;
import camp.computer.data.format.configuration.ValueSet;
import camp.computer.platform_infrastructure.LoadBuildFileTask;

import javax.sound.sampled.Port;
import java.util.Scanner;

public class Interpreter {

    private static Interpreter instance = null;

    Workspace workspace;

    private Interpreter() {
        Interpreter.instance = this;
        workspace = new Workspace();
    }

    public static Interpreter getInstance() {
        if (Interpreter.instance == null) {
            Interpreter interpreter = new Interpreter();
            return interpreter;
        } else {
            return Interpreter.instance;
        }
    }

    public void start() {

        Scanner scanner = new Scanner(System.in);
        String inputLine = null;

        while(true) {

            // TODO: start file
            // TODO: export file <filename>

            // SATURDAY GOAL:
            // TODO: ✓ add User object
            // TODO: set port supported portConfigurations
            // TODO: remove project, device, port
            // TODO: export/save project (for user account)
            // TODO: import/load project (for user account)

            // TODO: add path <sourcePortUid>, <targetPortUid>
            // TODO: show workspace, project, device, port
            // TODO: clone (add from description by UUID, or at uri/path) project, device
            // TODO: Explore/Search devices

            // TODO: simulate vertical integration of virtual host shared by (or distributed across) connected devices
            // TODO: list hosts

            // TODO: TaskConstruct that is composable, sequencable (in live-editing loop), live-loadable (simulated/placeholder for C?).

            // TODO: Select saved project, auto-select from hosts/verify, propagate viral installation/verify, IASM for vertical integration of hosts/device(s), IASM per-host for extensions.

            // TODO: Redis
            // TODO: Timeline (of commands, so can reconstruct)
            // TODO: IASM: Generate instructions, track completion status/state of each step (for host/device(s), across hosts)!
            // TODO: Users: Workspace, Projects, Portfolio (not separate object, just filter shared projects)

            // TODO: Create "host/device server" that announces its presence and allows retrieval of its HDL, specs.

            System.out.print("~ ");

            inputLine = scanner.nextLine().trim();

            interpretLine(inputLine);

        }

    }

    public void interpretLine(String inputLine) {

        if (inputLine.startsWith("#")) {

            // Nothing!

        } else if (inputLine.startsWith("import file")) {

            importFileTask(inputLine);

        } else if (inputLine.equals("add project")) {

            createProjectTask();

        } else if (inputLine.equals("list projects")) {

            listProjectsTask();

        } else if (inputLine.startsWith("edit project")) {

            editProjectTask(inputLine);

        } else if (inputLine.startsWith("set project title")) {

            setProjectTitleTask(inputLine);

        } else if (inputLine.equals("add device")) { // create hardware

            createDeviceTask();

        } else if (inputLine.equals("list devices")) { // list hardware

            listDevicesTask();

        } else if (inputLine.startsWith("edit device")) { // select hardware

            editDeviceTask(inputLine);

        } else if (inputLine.startsWith("add port")) { // create port

            createPortTask();

        } else if (inputLine.startsWith("add option")) {

            createOptionTask(inputLine);

        } else if (inputLine.startsWith("list ports")) {

            listPortsTask(inputLine);

        } else if (inputLine.startsWith("edit port")) {

            editPortTask(inputLine);

        } else if (inputLine.startsWith("add path")) { // add path device 1 port 3 device 4 port 1

            createPathTask(inputLine);

        } else if (inputLine.startsWith("list paths")) {

            listPathsTask();

        } else if (inputLine.startsWith("set path configuration")) {

            setPathConfigurationTask(inputLine);

        } else if (inputLine.startsWith("exit")) {

            exitTask();

        }

    }

    // <REFACTOR>
    // TODO: Create "Command" class with command (1) keywords and (2) task to handle command.

    public void importFileTask(String context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String inputLine = context;
        String[] inputLineWords = inputLine.split("[ ]+");

        String inputFilePath = inputLineWords[2];

        new LoadBuildFileTask().execute(inputFilePath);

    }

    public void createProjectTask() {
        ProjectConstruct projectConstruct = new ProjectConstruct();
        System.out.println("added project " + projectConstruct.uid);

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

    public void editProjectTask(String context){
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

        System.out.println("editing project " + inputProjectUid);

    }

    public void setProjectTitleTask(String context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        if (workspace.projectConstruct != null) {

            String inputLine = context;
            String[] inputLineWords = inputLine.split("[ ]+");

            String inputProjectTitle = inputLineWords[3];

            workspace.projectConstruct.title = inputProjectTitle;

            System.out.println("project title changed to " + inputProjectTitle);
        }

    }

    public void createDeviceTask() {

        DeviceConstruct deviceConstruct = new DeviceConstruct();
        System.out.println("added device " + deviceConstruct.uid);

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

    public void editDeviceTask(String context) {
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

        System.out.println("editing device " + inputDeviceUid);

    }

    public void createPortTask() {

        if (workspace.deviceConstruct != null) {

            PortConstruct portConstruct = new PortConstruct();
            System.out.println("added port " + portConstruct.uid + " on device " + workspace.deviceConstruct.uid);

            workspace.deviceConstruct.portConstructs.add(portConstruct);

        }

    }

    // e.g.,
    // add option uart(tx);output;ttl,cmos
    public void createOptionTask(String context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String inputLine = context;
        String[] inputLineWords = inputLine.split("[ ]+");

        String configurationOptionString = inputLineWords[2];

        String[] configurationOptionList = configurationOptionString.split(";");

        String configurationOptionMode = configurationOptionList[0];
        String[] configurationOptionDirectionList = configurationOptionList[1].split(",");
        String[] configurationOptionVoltageList = configurationOptionList[2].split(",");

        // TODO: Parse "bus(line)" mode string pattern to create bus and lines.

        PortConfiguration.Mode mode = PortConfiguration.Mode.NONE;
        ValueSet<PortConfiguration.Direction> directions = null;
        ValueSet<PortConfiguration.Voltage> voltages = null;

        // <REPLACE>
        // TODO: Replace this LUT to determine associated enums with flexible system using manager for Mode(String), Direction(String), Voltage(String).

        // Configuration Option Mode
        if (configurationOptionMode.equals("none")) {
            mode = PortConfiguration.Mode.NONE;
        } else if (configurationOptionMode.equals("digital")) {
            mode = PortConfiguration.Mode.DIGITAL;
        } else if (configurationOptionMode.equals("analog")) {
            mode = PortConfiguration.Mode.ANALOG;
        } else if (configurationOptionMode.equals("pwm")) {
            mode = PortConfiguration.Mode.PWM;
        } else if (configurationOptionMode.equals("resistive_touch")) {
            mode = PortConfiguration.Mode.RESISTIVE_TOUCH;
        } else if (configurationOptionMode.equals("power")) {
            mode = PortConfiguration.Mode.POWER;
        } else if (configurationOptionMode.equals("i2c(scl)")) {
            mode = PortConfiguration.Mode.I2C_SCL;
        } else if (configurationOptionMode.equals("i2c(sda)")) {
            mode = PortConfiguration.Mode.I2C_SDA;
        } else if (configurationOptionMode.equals("spi(sclk)")) {
            mode = PortConfiguration.Mode.SPI_SCLK;
        } else if (configurationOptionMode.equals("spi(mosi)")) {
            mode = PortConfiguration.Mode.SPI_MOSI;
        } else if (configurationOptionMode.equals("spi(miso)")) {
            mode = PortConfiguration.Mode.SPI_MISO;
        } else if (configurationOptionMode.equals("spi(ss)")) {
            mode = PortConfiguration.Mode.SPI_SS;
        } else if (configurationOptionMode.equals("uart(rx)")) {
            mode = PortConfiguration.Mode.UART_RX;
        } else if (configurationOptionMode.equals("uart(tx)")) {
            mode = PortConfiguration.Mode.UART_TX;
        }

        // Configuration Option Directions
        if (configurationOptionDirectionList.length == 1 && configurationOptionDirectionList[0].equals("null")) {

            directions = null;

        } else {

            directions = new ValueSet<>();

            for (int i = 0; i < configurationOptionDirectionList.length; i++) {

                if (configurationOptionDirectionList[i].equals("none")) {
                    directions.values.add(PortConfiguration.Direction.NONE);
                } else if (configurationOptionDirectionList[i].equals("input")) {
                    directions.values.add(PortConfiguration.Direction.INPUT);
                } else if (configurationOptionDirectionList[i].equals("output")) {
                    directions.values.add(PortConfiguration.Direction.OUTPUT);
                } else if (configurationOptionDirectionList[i].equals("bidirectional")) {
                    directions.values.add(PortConfiguration.Direction.BIDIRECTIONAL);
                }

            }
        }

        // Configuration Option Voltages
        if (configurationOptionVoltageList.length == 1 && configurationOptionVoltageList[0].equals("null")) {

            voltages = null;

        } else {

            voltages = new ValueSet<>();

            for (int i = 0; i < configurationOptionVoltageList.length; i++) {

                if (configurationOptionVoltageList[i].equals("none")) {
                    voltages.values.add(PortConfiguration.Voltage.NONE);
                } else if (configurationOptionVoltageList[i].equals("ttl")) {
                    voltages.values.add(PortConfiguration.Voltage.TTL);
                } else if (configurationOptionVoltageList[i].equals("cmos")) {
                    voltages.values.add(PortConfiguration.Voltage.CMOS);
                } else if (configurationOptionVoltageList[i].equals("common")) {
                    voltages.values.add(PortConfiguration.Voltage.COMMON);
                }

            }
        }

        // Add configuration option
        workspace.portConstruct.portConfigurations.add(new PortConfiguration(mode, directions, voltages));
        // </REPLACE>

    }

    // list ports -configuration
    public void listPortsTask(String context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String inputLine = context;
        String[] inputLineWords = inputLine.split("[ ]+");

        if (inputLineWords.length == 2) {

            if (workspace.deviceConstruct != null) {

                for (int i = 0; i < workspace.deviceConstruct.portConstructs.size(); i++) {

                    // Port UID
                    System.out.println("" + workspace.deviceConstruct.portConstructs.get(i).uid);

                }

            }

        } else if (inputLineWords.length > 2) {

            String modifiers = inputLineWords[2];

            if (!modifiers.equals("-configuration")) {
                return;
            }

            if (workspace.deviceConstruct != null) {

                for (int i = 0; i < workspace.deviceConstruct.portConstructs.size(); i++) {

                    // Port UID
                    System.out.println("" + workspace.deviceConstruct.portConstructs.get(i).uid);

                    for (int j = 0; j < workspace.deviceConstruct.portConstructs.get(i).portConfigurations.size(); j++) {

                        // Mode/Family
                        System.out.print("\t" + workspace.deviceConstruct.portConstructs.get(i).portConfigurations.get(j).mode + "; ");

                        // Directions
                        if (workspace.deviceConstruct.portConstructs.get(i).portConfigurations.get(j).directions != null) {
                            for (int k = 0; k < workspace.deviceConstruct.portConstructs.get(i).portConfigurations.get(j).directions.values.size(); k++) {
                                System.out.print("" + workspace.deviceConstruct.portConstructs.get(i).portConfigurations.get(j).directions.values.get(k));

                                if ((k + 1) < workspace.deviceConstruct.portConstructs.get(i).portConfigurations.get(j).directions.values.size()) {
                                    System.out.print(", ");
                                }
                            }
                        }
                        System.out.print("; ");

                        // Voltages
                        if (workspace.deviceConstruct.portConstructs.get(i).portConfigurations.get(j).voltages != null) {
                            for (int k = 0; k < workspace.deviceConstruct.portConstructs.get(i).portConfigurations.get(j).voltages.values.size(); k++) {
                                System.out.print("" + workspace.deviceConstruct.portConstructs.get(i).portConfigurations.get(j).voltages.values.get(k));

                                if ((k + 1) < workspace.deviceConstruct.portConstructs.get(i).portConfigurations.get(j).voltages.values.size()) {
                                    System.out.print(", ");
                                }
                            }
                        }

                        System.out.println();

                    }

                }

            }

        }

    }

    public void editPortTask(String context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String inputLine = context;
        String[] inputLineWords = inputLine.split("[ ]+");

        long inputPortUid = Long.valueOf(inputLineWords[2]);

        for (int i = 0; i < workspace.projectConstruct.deviceConstructs.size(); i++) {
            for (int j = 0; j < workspace.projectConstruct.deviceConstructs.get(i).portConstructs.size(); j++) {
                if (workspace.projectConstruct.deviceConstructs.get(i).portConstructs.get(j).uid == inputPortUid) {
                    workspace.portConstruct = workspace.projectConstruct.deviceConstructs.get(i).portConstructs.get(j);
                    break;
                }
            }
        }

        System.out.println("editing port " + inputPortUid);

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
            pathConstruct.sourcePortConstruct = sourcePortConstruct;
            pathConstruct.targetPortConstruct = targetPortConstruct;

            workspace.projectConstruct.pathConstructs.add(pathConstruct);

            System.out.println("added path " + pathConstruct.uid + " from device " + sourceDeviceConstruct.uid + " port " + sourcePortConstruct.uid + " to device " + targetDeviceConstruct.uid + " port " + targetPortConstruct.uid);
        }

    }

    public void listPathsTask() {

        if (workspace.projectConstruct != null) {

            for (int i = 0; i < workspace.projectConstruct.pathConstructs.size(); i++) {
                System.out.println("" + workspace.projectConstruct.pathConstructs.get(i).uid + " (port " + workspace.projectConstruct.pathConstructs.get(i).sourcePortConstruct.uid + ", port " + workspace.projectConstruct.pathConstructs.get(i).targetPortConstruct.uid + ")");
            }

        }

    }

    public void setPathConfigurationTask(String context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String inputLine = context;
        String[] inputLineWords = inputLine.split("[ ]+");

        String inputPathConfiguration = inputLineWords[3];

        System.out.println("set path configuration to \"" + inputPathConfiguration + "\"");

        // protocols:
        // - electronic, rf, none

        // electronic:
        // - voltage
    }

    public void exitTask() {
        System.exit(0);
    }
    // </REFACTOR>
}


// TODO: PORTS CAN BE "WIRELESS" AND SPREAD OUT ACROSS BOARDS JUST LIKE CLAY BOARDS CAN BE SPREAD OUT IN SPACE.
