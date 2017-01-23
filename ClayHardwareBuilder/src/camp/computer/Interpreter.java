package camp.computer;

import camp.computer.construct.DeviceConstruct;
import camp.computer.construct.PathConstruct;
import camp.computer.construct.PortConstruct;
import camp.computer.construct.ProjectConstruct;
import camp.computer.data.format.configuration.PathConfiguration;
import camp.computer.data.format.configuration.PortConfigurationConstraint;
import camp.computer.data.format.configuration.ValueSet;
import camp.computer.platform_infrastructure.LoadBuildFileTask;

import java.util.ArrayList;
import java.util.List;
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
            // TODO: set port supported portConfigurationConstraints
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

            addProjectTask();

        } else if (inputLine.equals("list projects")) {

            listProjectsTask();

        } else if (inputLine.startsWith("edit project")) {

            editProjectTask(inputLine);

        } else if (inputLine.startsWith("set project title")) {

            setProjectTitleTask(inputLine);

        } else if (inputLine.equals("add device")) { // create hardware

            addDeviceTask();

        } else if (inputLine.equals("list devices")) { // list hardware

            listDevicesTask();

        } else if (inputLine.startsWith("edit device")) { // select hardware

            editDeviceTask(inputLine);

        } else if (inputLine.startsWith("add port")) { // create port

            addPortTask();

        } else if (inputLine.startsWith("add constraint")) { // add constraint (to set of attributes in configuration)

            addConfigurationConstraintTask(inputLine);

        } else if (inputLine.startsWith("set attributes")) { // TODO: "set configuration" or "set attributes"

            setConfigurationAttributesTask(inputLine);

        } else if (inputLine.startsWith("list ports")) {

            listPortsTask(inputLine);

        } else if (inputLine.startsWith("edit port")) {

            editPortTask(inputLine);

        } else if (inputLine.startsWith("add path")) { // add path device 1 port 3 device 4 port 1

            addPathTask(inputLine);

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

    public void addProjectTask() {
        ProjectConstruct projectConstruct = new ProjectConstruct();
        System.out.println("✔ add project " + projectConstruct.uid);

        workspace.projectConstructs.add(projectConstruct);

        // Store reference to last-created project
        workspace.lastProjectConstruct = projectConstruct;
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

        if (inputLineWords.length == 2) {
            // edit project

            workspace.projectConstruct = workspace.lastProjectConstruct;

        } else if (inputLineWords.length > 2) {
            // edit project <uid>

            long inputProjectUid = Long.valueOf(inputLineWords[2]);

            for (int i = 0; i < workspace.projectConstructs.size(); i++) {
                if (workspace.projectConstructs.get(i).uid == inputProjectUid) {
                    workspace.projectConstruct = workspace.projectConstructs.get(i);
                    break;
                }
            }

        }

        System.out.println("✔ edit project " + workspace.projectConstruct.uid);

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

    public void addDeviceTask() {

        DeviceConstruct deviceConstruct = new DeviceConstruct();
        System.out.println("✔ add device " + deviceConstruct.uid);

        workspace.projectConstruct.deviceConstructs.add(deviceConstruct);

        // Store reference to last-created device
        workspace.lastDeviceConstruct= deviceConstruct;

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

        if (inputLineWords.length == 2) {

            workspace.deviceConstruct = workspace.lastDeviceConstruct;

        } else if (inputLineWords.length > 2) {
            long inputDeviceUid = Long.valueOf(inputLineWords[2]);

            for (int i = 0; i < workspace.projectConstruct.deviceConstructs.size(); i++) {
                if (workspace.projectConstruct.deviceConstructs.get(i).uid == inputDeviceUid) {
                    workspace.deviceConstruct = workspace.projectConstruct.deviceConstructs.get(i);
                    break;
                }
            }
        }

        System.out.println("✔ edit device " + workspace.deviceConstruct.uid);

    }

    public void addPortTask() {

        if (workspace.deviceConstruct != null) {

            PortConstruct portConstruct = new PortConstruct();
            System.out.println("✔ add port " + portConstruct.uid + " on device " + workspace.deviceConstruct.uid);

            workspace.deviceConstruct.portConstructs.add(portConstruct);

            // Store reference to last-created port
            workspace.lastPortConstruct = portConstruct;

        }

    }

    // e.g.,
    // add constraint uart(tx);output;ttl,cmos
    public void addConfigurationConstraintTask(String context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String inputLine = context;
        String[] inputLineWords = inputLine.split("[ ]+");

        String configurationOptionString = inputLineWords[2];

        String[] configurationOptionList = configurationOptionString.split(";");

        // Default Configuration
        // TODO: Replace this LUT to determine associated enums with flexible system using manager for Mode(String), Direction(String), Voltage(String).
        PortConfigurationConstraint.Mode mode = PortConfigurationConstraint.Mode.NONE;
        ValueSet<PortConfigurationConstraint.Direction> directions = null;
        ValueSet<PortConfigurationConstraint.Voltage> voltages = null;

        // TODO: Parse "bus(line)" value string pattern to create bus and lines.

        for (int i = 0; i < configurationOptionList.length; i++) {

            String[] configurationAttributeList = configurationOptionList[i].split(":");
            String attributeTitle = configurationAttributeList[0];
            String attributeValues = configurationAttributeList[1];

            if (attributeTitle.equals("mode")) {

                String configurationOptionMode = attributeValues;

                // Configuration Option Mode
                if (configurationOptionMode.equals("none")) {
                    mode = PortConfigurationConstraint.Mode.NONE;
                } else if (configurationOptionMode.equals("digital")) {
                    mode = PortConfigurationConstraint.Mode.DIGITAL;
                } else if (configurationOptionMode.equals("analog")) {
                    mode = PortConfigurationConstraint.Mode.ANALOG;
                } else if (configurationOptionMode.equals("pwm")) {
                    mode = PortConfigurationConstraint.Mode.PWM;
                } else if (configurationOptionMode.equals("resistive_touch")) {
                    mode = PortConfigurationConstraint.Mode.RESISTIVE_TOUCH;
                } else if (configurationOptionMode.equals("power")) {
                    mode = PortConfigurationConstraint.Mode.POWER;
                } else if (configurationOptionMode.equals("i2c(scl)")) {
                    mode = PortConfigurationConstraint.Mode.I2C_SCL;
                } else if (configurationOptionMode.equals("i2c(sda)")) {
                    mode = PortConfigurationConstraint.Mode.I2C_SDA;
                } else if (configurationOptionMode.equals("spi(sclk)")) {
                    mode = PortConfigurationConstraint.Mode.SPI_SCLK;
                } else if (configurationOptionMode.equals("spi(mosi)")) {
                    mode = PortConfigurationConstraint.Mode.SPI_MOSI;
                } else if (configurationOptionMode.equals("spi(miso)")) {
                    mode = PortConfigurationConstraint.Mode.SPI_MISO;
                } else if (configurationOptionMode.equals("spi(ss)")) {
                    mode = PortConfigurationConstraint.Mode.SPI_SS;
                } else if (configurationOptionMode.equals("uart(rx)")) {
                    mode = PortConfigurationConstraint.Mode.UART_RX;
                } else if (configurationOptionMode.equals("uart(tx)")) {
                    mode = PortConfigurationConstraint.Mode.UART_TX;
                }

            } else if (attributeTitle.equals("direction")) {

                String[] configurationOptionDirectionList = attributeValues.split(",");

                // Configuration Option Directions
                if (configurationOptionDirectionList.length == 1 && configurationOptionDirectionList[0].equals("null")) {

                    directions = null;

                } else {

                    directions = new ValueSet<>();

                    for (int j = 0; j < configurationOptionDirectionList.length; j++) {

                        if (configurationOptionDirectionList[j].equals("none")) {
                            directions.values.add(PortConfigurationConstraint.Direction.NONE);
                        } else if (configurationOptionDirectionList[j].equals("input")) {
                            directions.values.add(PortConfigurationConstraint.Direction.INPUT);
                        } else if (configurationOptionDirectionList[j].equals("output")) {
                            directions.values.add(PortConfigurationConstraint.Direction.OUTPUT);
                        } else if (configurationOptionDirectionList[j].equals("bidirectional")) {
                            directions.values.add(PortConfigurationConstraint.Direction.BIDIRECTIONAL);
                        }

                    }
                }

            } else if (attributeTitle.equals("voltage")) {

                String[] configurationOptionVoltageList = attributeValues.split(",");

                // Configuration Option Voltages
                if (configurationOptionVoltageList.length == 1 && configurationOptionVoltageList[0].equals("null")) {

                    voltages = null;

                } else {

                    voltages = new ValueSet<>();

                    for (int j = 0; j < configurationOptionVoltageList.length; j++) {

                        if (configurationOptionVoltageList[j].equals("none")) {
                            voltages.values.add(PortConfigurationConstraint.Voltage.NONE);
                        } else if (configurationOptionVoltageList[j].equals("ttl")) {
                            voltages.values.add(PortConfigurationConstraint.Voltage.TTL);
                        } else if (configurationOptionVoltageList[j].equals("cmos")) {
                            voltages.values.add(PortConfigurationConstraint.Voltage.CMOS);
                        } else if (configurationOptionVoltageList[j].equals("common")) {
                            voltages.values.add(PortConfigurationConstraint.Voltage.COMMON);
                        }

                    }
                }

            }

        }

        // Add Configuration Option/Constraint
        workspace.portConstruct.portConfigurationConstraints.add(new PortConfigurationConstraint(mode, directions, voltages));

    }

    // set attribute mode:digital;direction:output;voltage:ttl
    public void setConfigurationAttributesTask(String context) {

        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String inputLine = context;
        String[] inputLineWords = inputLine.split("[ ]+");

        String configurationOptionString = inputLineWords[2];

        PortConfigurationConstraint.Mode mode = PortConfigurationConstraint.Mode.NONE;
        PortConfigurationConstraint.Direction direction = null;
        PortConfigurationConstraint.Voltage voltage = null;

        // Separate configuration string into tokens separated by ";" substring, each an expression representing an
        // attribute state assignment. Separate each attribute assignment by ":", into the attribute title and
        // by ":" substring value.
        String[] configurationOptionList = configurationOptionString.split(";");
        for (int i = 0; i < configurationOptionList.length; i++) {

            String[] configurationAttributeList = configurationOptionList[i].split(":");
            String attributeTitle = configurationAttributeList[0];
            String attributeValues = configurationAttributeList[1];

            if (attributeTitle.equals("mode")) {

                // Parses and caches the mode assignment.
                if (attributeValues.equals("none")) {
                    mode = PortConfigurationConstraint.Mode.NONE;
                } else if (attributeValues.equals("digital")) {
                    mode = PortConfigurationConstraint.Mode.DIGITAL;
                } else if (attributeValues.equals("analog")) {
                    mode = PortConfigurationConstraint.Mode.ANALOG;
                } else if (attributeValues.equals("pwm")) {
                    mode = PortConfigurationConstraint.Mode.PWM;
                } else if (attributeValues.equals("resistive_touch")) {
                    mode = PortConfigurationConstraint.Mode.RESISTIVE_TOUCH;
                } else if (attributeValues.equals("power")) {
                    mode = PortConfigurationConstraint.Mode.POWER;
                } else if (attributeValues.equals("i2c(scl)")) {
                    mode = PortConfigurationConstraint.Mode.I2C_SCL;
                } else if (attributeValues.equals("i2c(sda)")) {
                    mode = PortConfigurationConstraint.Mode.I2C_SDA;
                } else if (attributeValues.equals("spi(sclk)")) {
                    mode = PortConfigurationConstraint.Mode.SPI_SCLK;
                } else if (attributeValues.equals("spi(mosi)")) {
                    mode = PortConfigurationConstraint.Mode.SPI_MOSI;
                } else if (attributeValues.equals("spi(miso)")) {
                    mode = PortConfigurationConstraint.Mode.SPI_MISO;
                } else if (attributeValues.equals("spi(ss)")) {
                    mode = PortConfigurationConstraint.Mode.SPI_SS;
                } else if (attributeValues.equals("uart(rx)")) {
                    mode = PortConfigurationConstraint.Mode.UART_RX;
                } else if (attributeValues.equals("uart(tx)")) {
                    mode = PortConfigurationConstraint.Mode.UART_TX;
                }

            } else if (attributeTitle.equals("direction")) {

                // Parses and caches the direction assignment.
                if (attributeValues.equals("none")) {
                    direction = PortConfigurationConstraint.Direction.NONE;
                } else if (attributeValues.equals("input")) {
                    direction = PortConfigurationConstraint.Direction.INPUT;
                } else if (attributeValues.equals("output")) {
                    direction = PortConfigurationConstraint.Direction.OUTPUT;
                } else if (attributeValues.equals("bidirectional")) {
                    direction = PortConfigurationConstraint.Direction.BIDIRECTIONAL;
                }

            } else if (attributeTitle.equals("voltage")) {

                // Parses and caches the voltage assignment.
                if (attributeValues.equals("none")) {
                    voltage = PortConfigurationConstraint.Voltage.NONE;
                } else if (attributeValues.equals("ttl")) {
                    voltage = PortConfigurationConstraint.Voltage.TTL;
                } else if (attributeValues.equals("cmos")) {
                    voltage = PortConfigurationConstraint.Voltage.CMOS;
                } else if (attributeValues.equals("common")) {
                    voltage = PortConfigurationConstraint.Voltage.COMMON;
                }

            }

        }

        // TODO: check if specified configuration is valid

        // Updates the port state.
        workspace.portConstruct.mode = mode;
        workspace.portConstruct.direction = direction;
        workspace.portConstruct.voltage = voltage;

        // TODO: Generalize so can set state of any construct/container. Don't assume port construct is only one with state.
        System.out.println("✔ set port attributes to " + workspace.portConstruct.mode + " " + workspace.portConstruct.direction + " " + workspace.portConstruct.voltage);

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

                    for (int j = 0; j < workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.size(); j++) {

                        // Mode/Family
                        System.out.print("\t" + workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).mode + "; ");

                        // Directions
                        if (workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).directions != null) {
                            for (int k = 0; k < workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).directions.values.size(); k++) {
                                System.out.print("" + workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).directions.values.get(k));

                                if ((k + 1) < workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).directions.values.size()) {
                                    System.out.print(", ");
                                }
                            }
                        }
                        System.out.print("; ");

                        // Voltages
                        if (workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).voltages != null) {
                            for (int k = 0; k < workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).voltages.values.size(); k++) {
                                System.out.print("" + workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).voltages.values.get(k));

                                if ((k + 1) < workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).voltages.values.size()) {
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

        if (inputLineWords.length == 2) {

            workspace.portConstruct = workspace.lastPortConstruct;

        } else if (inputLineWords.length > 2) {

            long inputPortUid = Long.valueOf(inputLineWords[2]);

            for (int i = 0; i < workspace.projectConstruct.deviceConstructs.size(); i++) {
                for (int j = 0; j < workspace.projectConstruct.deviceConstructs.get(i).portConstructs.size(); j++) {
                    if (workspace.projectConstruct.deviceConstructs.get(i).portConstructs.get(j).uid == inputPortUid) {
                        workspace.portConstruct = workspace.projectConstruct.deviceConstructs.get(i).portConstructs.get(j);
                        break;
                    }
                }
            }

        }

        System.out.println("✔ edit port " + workspace.portConstruct.uid);

    }

    // e.g., add path device 1 port 3 device 4 port 1
    public void addPathTask(String context) {

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

            System.out.println("✔ add path " + pathConstruct.uid + " from device " + sourceDeviceConstruct.uid + " port " + sourcePortConstruct.uid + " to device " + targetDeviceConstruct.uid + " port " + targetPortConstruct.uid);


            /**
             * "solve path [uid]"
             */

            // TODO: Resolve set of available configurations for path based on compatible configurations of ports in the path.

            // Iterate through configurations for of source port in path. For each source port configuration, check
            // the other ports' configurations for compatibility; then add each compatible configuration to a list of
            // compatible configurations.
            List<PathConfiguration> compatibleConfigurations = new ArrayList<>();
            for (int i = 0; i < pathConstruct.sourcePortConstruct.portConfigurationConstraints.size(); i++) {
                PortConfigurationConstraint portConstraint = pathConstruct.sourcePortConstruct.portConfigurationConstraints.get(i);

                for (int j = 0; j < pathConstruct.targetPortConstruct.portConfigurationConstraints.size(); j++) {
                    PortConfigurationConstraint otherPortConstraint = pathConstruct.targetPortConstruct.portConfigurationConstraints.get(j);

                    boolean isCompatible = PortConfigurationConstraint.isCompatible(portConstraint, otherPortConstraint);

                    // PATH SERIAL FORMAT:
                    // ~ mode;direction;voltage + mode;direction;voltage
                    //
                    // ? mode;ports:uid,uid;voltage
                    // ? source:uid;target:uid;mode;direction;voltage
                    // > ports:uid,uid;mode;direction;voltage
                    //
                    // ? mode;direction;voltage&mode;direction;voltage
                    // ? mode;direction;voltage+mode;direction;voltage
                    // ? mode;direction;voltage|mode;direction;voltage

                    if (isCompatible) {
                        // TODO: compatibleConfigurations.add(/* result from isCompatible */);
//                        PathConfiguration pathConfiguration = new PathConfiguration(portConstraint, otherPortConstraint);
//                        compatibleConfigurations.add(pathConfiguration);
//                        break;
                    }

                    ValueSet<PortConfigurationConstraint> compatibleConfigs = PortConfigurationConstraint.getCompatibleConfiguration(portConstraint, otherPortConstraint);

                    if (compatibleConfigs != null) {
                        // Source
                        System.out.print("" + compatibleConfigs.values.get(0).mode);
                        System.out.print(";");
                        for (int k = 0; k < compatibleConfigs.values.get(0).directions.values.size(); k++) {
                            System.out.print("" + compatibleConfigs.values.get(0).directions.values.get(k));
                            if ((k + 1) < compatibleConfigs.values.get(0).directions.values.size()) {
                                System.out.print(", ");
                            }
                        }
                        System.out.print(";");
                        for (int k = 0; k < compatibleConfigs.values.get(0).voltages.values.size(); k++) {
                            System.out.print("" + compatibleConfigs.values.get(0).voltages.values.get(k));
                            if ((k + 1) < compatibleConfigs.values.get(0).voltages.values.size()) {
                                System.out.print(", ");
                            }
                        }
                        System.out.print(" | ");

                        // Target
                        System.out.print("" + compatibleConfigs.values.get(1).mode);
                        System.out.print(";");
                        for (int k = 0; k < compatibleConfigs.values.get(1).directions.values.size(); k++) {
                            System.out.print("" + compatibleConfigs.values.get(1).directions.values.get(k));
                            if ((k + 1) < compatibleConfigs.values.get(1).directions.values.size()) {
                                System.out.print(", ");
                            }
                        }
                        System.out.print(";");
                        for (int k = 0; k < compatibleConfigs.values.get(1).voltages.values.size(); k++) {
                            System.out.print("" + compatibleConfigs.values.get(1).voltages.values.get(k));
                            if ((k + 1) < compatibleConfigs.values.get(1).voltages.values.size()) {
                                System.out.print(", ");
                            }
                        }

                        System.out.println();
                    }

                    // TODO: Pick up here. Constraint resolution isn't working, probably because of a logic bug in isCompatible(...)
                }
//                System.out.println();
            }

            // If there is only one path configuration in the compatible configurations list, automatically configure
            // the path with it, thereby updating the ports' configurations in the path.
            // TODO: ^
            if (compatibleConfigurations.size() == 1) {
                // Apply the corresponding configuration to ports.
                PathConfiguration pathConfiguration = compatibleConfigurations.get(0);
                System.out.println("✔ found compatible configuration (" + pathConstruct.sourcePortConstruct.uid + ", " + pathConstruct.targetPortConstruct.uid + "): (" + pathConfiguration.sourceConfiguration.mode + ", ...) --- (" + pathConfiguration.targetConfiguration.mode + ", ...)");

                // TODO: (QUESTION) Can I specify a path configuration and infer port configurations (for multi-port) or should it be a list of port configurations?
                // TODO: Apply states based on per-variable constraints?
                // TODO: Ensure there's only one compatible state for each of the variables.

                // list ports: "3 (12 configurations)" or "3 (null, null, null)"; "3 (SPI_MISO; INPUT; TTL)"

//                // Configure the ports with the single compatible configuration
//                sourcePortConstruct.mode = pathConfiguration.sourceConfiguration.mode;
//                System.out.println (">>> setting mode: " + sourcePortConstruct.mode);
//
//                if (pathConfiguration.sourceConfiguration.directions.values.size() == 1) {
//                    sourcePortConstruct.direction = pathConfiguration.sourceConfiguration.directions.values.get(0);
//                    System.out.println (">>> setting direction: " + sourcePortConstruct.direction);
//                }
//
//                if (pathConfiguration.sourceConfiguration.voltages.values.size() == 1) {
//                    sourcePortConstruct.voltage = pathConfiguration.sourceConfiguration.voltages.values.get(0);
//                    System.out.println (">>> setting voltages: " + sourcePortConstruct.voltage);
//                }

            }

            // Otherwise, list the available path configurations and prompt the user to set one of them manually.
            else if (compatibleConfigurations.size() > 1) {
                // Apply the corresponding configuration to ports.
                System.out.println("✔ found compatible configurations");
                for (int i = 0; i < compatibleConfigurations.size(); i++) {
                    PathConfiguration pathConfiguration = compatibleConfigurations.get(i);
                    System.out.println("\t[" + i + "] (" + pathConstruct.sourcePortConstruct.uid + ", " + pathConstruct.targetPortConstruct.uid + "): (" + pathConfiguration.sourceConfiguration.mode + ", ...) --- (" + pathConfiguration.targetConfiguration.mode + ", ...)");
                }
                System.out.println ("! set one of these configurations");
            }
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

        System.out.println("✔ set path configuration to \"" + inputPathConfiguration + "\"");

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
