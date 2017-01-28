package camp.computer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import camp.computer.construct.DeviceConstruct;
import camp.computer.construct.PathConstruct;
import camp.computer.construct.PortConstruct;
import camp.computer.construct.ProjectConstruct;
import camp.computer.data.format.configuration.Configuration;
import camp.computer.data.format.configuration.Variable;
import camp.computer.platform_infrastructure.LoadBuildFileTask;
import camp.computer.util.Pair;
import camp.computer.util.Tuple;

public class Interpreter {

    // <SETTINGS>
    public static boolean ENABLE_VERBOSE_OUTPUT = false;
    // </SETTINGS>

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

        while (true) {

            System.out.print("~ ");

            inputLine = scanner.nextLine().trim();

            interpretLine(inputLine);

        }

    }

    public void interpretLine(String inputLine) {

        // Store context
        Context context = new Context();
        context.inputLine = inputLine;

        if (context.inputLine.startsWith("#")) {

            // Nothing!

        } else if (context.inputLine.startsWith("import file")) {

            importFileTask(context);

        } else if (context.inputLine.equals("add project")) {

            addProjectTask();

        } else if (context.inputLine.equals("list projects")) {

            listProjectsTask();

        } else if (context.inputLine.startsWith("edit project")) {

            editProjectTask(context);

        } else if (context.inputLine.startsWith("set project title")) {

            setProjectTitleTask(context);

        } else if (context.inputLine.equals("add device")) { // create hardware

            addDeviceTask();

        } else if (context.inputLine.equals("list devices")) { // list hardware

            listDevicesTask();

        } else if (context.inputLine.startsWith("edit device")) { // select hardware

            editDeviceTask(context);

        } else if (context.inputLine.startsWith("add port")) { // create port

            addPortTask();

        } else if (context.inputLine.startsWith("add configuration")) { // add configuration (to set of attributes in configurations)

            addConfigurationTask(context);

        } else if (context.inputLine.startsWith("set configuration")) { // TODO: "set configurations" or "set configuration"

            setConfigurationTask(context);

        } else if (context.inputLine.startsWith("list ports")) {

            listPortsTask(context);

        } else if (context.inputLine.startsWith("edit port")) {

            editPortTask(context);

        } else if (context.inputLine.startsWith("add path")) { // add path device 1 port 3 device 4 port 1

            addPathTask(context);

        } else if (context.inputLine.startsWith("list paths")) {

            listPathsTask();

        } else if (context.inputLine.startsWith("set path configurations")) {

            setPathConfigurationTask(context);

        } else if (context.inputLine.startsWith("exit")) {

            exitTask();

        }

    }

    // <REFACTOR>
    // TODO: Create "Command" class with command (1) keywords and (2) task to handle command.

    public void importFileTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

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

    public void editProjectTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

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

    public void setProjectTitleTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        if (workspace.projectConstruct != null) {

            String[] inputLineWords = context.inputLine.split("[ ]+");

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
        workspace.lastDeviceConstruct = deviceConstruct;

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

    public void editDeviceTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

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

    // e.g., add configuration uart(tx);output;ttl,cmos
    public void addConfigurationTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        String configurationOptionString = inputLineWords[2];

        String[] configurationVariableList = configurationOptionString.split(";");

        // Default VariableMap
        // TODO: Replace this LUT to determine associated enums with flexible system using manager for Mode(String), Direction(String), Voltage(String).
//        PortConfigurationConstraint.Mode mode = PortConfigurationConstraint.Mode.NONE;
//        ValueSet<PortConfigurationConstraint.Direction> directions = null;
//        ValueSet<PortConfigurationConstraint.Voltage> voltages = null;

        // TODO: Parse "bus(line)" value string pattern to create bus and lines.

        List<Pair<String, Tuple<String>>> variableValueSets = new ArrayList<>();

        for (int i = 0; i < configurationVariableList.length; i++) {

            String[] configurationAssignmentList = configurationVariableList[i].split(":");
            String variableTitle = configurationAssignmentList[0];
            String variableValues = configurationAssignmentList[1];

            // <HACK>
            if (!workspace.portConstruct.variables.containsKey(variableTitle)) {
                workspace.portConstruct.variables.put(variableTitle, new Variable(variableTitle));
            }
            // </HACK>

            String[] variableValueList = variableValues.split(",");

            // Save variable's value set for the configuration constraint
            Tuple<String> variableValueSet = new Tuple<>();
            for (int j = 0; j < variableValueList.length; j++) {
                variableValueSet.values.add(variableValueList[j]);
            }
            variableValueSets.add(new Pair<>(variableTitle, variableValueSet));

        }

        // TODO TODO

        // Add VariableMap Option/Configuration
        workspace.portConstruct.configurations.add(new Configuration(variableValueSets));

    }

    // set configuration mode:digital;direction:output;voltage:ttl
    public void setConfigurationTask(Context context) {

//        // TODO: Change argument to "Context context" (temporary cache/manager)
//
//        // TODO: Lookup context.get("inputLine")
//        String[] inputLineWords = context.inputLine.split("[ ]+");
//
//        String configurationOptionString = inputLineWords[2];
//
//        PortConfigurationConstraint.Mode mode = PortConfigurationConstraint.Mode.NONE;
//        PortConfigurationConstraint.Direction direction = null;
//        PortConfigurationConstraint.Voltage voltage = null;
//
//        // Separate configurations string into tokens separated by ";" substring, each an expression representing an
//        // attribute state assignment. Separate each attribute assignment by ":", into the attribute title and
//        // by ":" substring value.
//        String[] configurationOptionList = configurationOptionString.split(";");
//        for (int i = 0; i < configurationOptionList.length; i++) {
//
//            String[] configurationAttributeList = configurationOptionList[i].split(":");
//            String attributeTitle = configurationAttributeList[0];
//            String attributeValues = configurationAttributeList[1];
//
//            if (attributeTitle.equals("mode")) {
//
//                // Parses and caches the mode assignment.
//                if (attributeValues.equals("none")) {
//                    mode = PortConfigurationConstraint.Mode.NONE;
//                } else if (attributeValues.equals("digital")) {
//                    mode = PortConfigurationConstraint.Mode.DIGITAL;
//                } else if (attributeValues.equals("analog")) {
//                    mode = PortConfigurationConstraint.Mode.ANALOG;
//                } else if (attributeValues.equals("pwm")) {
//                    mode = PortConfigurationConstraint.Mode.PWM;
//                } else if (attributeValues.equals("resistive_touch")) {
//                    mode = PortConfigurationConstraint.Mode.RESISTIVE_TOUCH;
//                } else if (attributeValues.equals("power")) {
//                    mode = PortConfigurationConstraint.Mode.POWER;
//                } else if (attributeValues.equals("i2c(scl)")) {
//                    mode = PortConfigurationConstraint.Mode.I2C_SCL;
//                } else if (attributeValues.equals("i2c(sda)")) {
//                    mode = PortConfigurationConstraint.Mode.I2C_SDA;
//                } else if (attributeValues.equals("spi(sclk)")) {
//                    mode = PortConfigurationConstraint.Mode.SPI_SCLK;
//                } else if (attributeValues.equals("spi(mosi)")) {
//                    mode = PortConfigurationConstraint.Mode.SPI_MOSI;
//                } else if (attributeValues.equals("spi(miso)")) {
//                    mode = PortConfigurationConstraint.Mode.SPI_MISO;
//                } else if (attributeValues.equals("spi(ss)")) {
//                    mode = PortConfigurationConstraint.Mode.SPI_SS;
//                } else if (attributeValues.equals("uart(rx)")) {
//                    mode = PortConfigurationConstraint.Mode.UART_RX;
//                } else if (attributeValues.equals("uart(tx)")) {
//                    mode = PortConfigurationConstraint.Mode.UART_TX;
//                }
//
//            } else if (attributeTitle.equals("direction")) {
//
//                // Parses and caches the direction assignment.
//                if (attributeValues.equals("none")) {
//                    direction = PortConfigurationConstraint.Direction.NONE;
//                } else if (attributeValues.equals("input")) {
//                    direction = PortConfigurationConstraint.Direction.INPUT;
//                } else if (attributeValues.equals("output")) {
//                    direction = PortConfigurationConstraint.Direction.OUTPUT;
//                } else if (attributeValues.equals("bidirectional")) {
//                    direction = PortConfigurationConstraint.Direction.BIDIRECTIONAL;
//                }
//
//            } else if (attributeTitle.equals("voltage")) {
//
//                // Parses and caches the voltage assignment.
//                if (attributeValues.equals("none")) {
//                    voltage = PortConfigurationConstraint.Voltage.NONE;
//                } else if (attributeValues.equals("ttl")) {
//                    voltage = PortConfigurationConstraint.Voltage.TTL;
//                } else if (attributeValues.equals("cmos")) {
//                    voltage = PortConfigurationConstraint.Voltage.CMOS;
//                } else if (attributeValues.equals("common")) {
//                    voltage = PortConfigurationConstraint.Voltage.COMMON;
//                }
//
//            }
//
//        }
//
//        // TODO: check if specified configurations is valid
//
//        // Updates the port state.
//        workspace.portConstruct.mode = mode;
//        workspace.portConstruct.direction = direction;
//        workspace.portConstruct.voltage = voltage;
//
//        // TODO: Generalize so can set state of any construct/container. Don't assume port construct is only one with state.
//        System.out.println("✔ set port attributes to " + workspace.portConstruct.mode + " " + workspace.portConstruct.direction + " " + workspace.portConstruct.voltage);

    }

    // list ports -configurations
    public void listPortsTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 2) {

            if (workspace.deviceConstruct != null) {

                for (int i = 0; i < workspace.deviceConstruct.portConstructs.size(); i++) {

                    // Port UID
                    System.out.println("" + workspace.deviceConstruct.portConstructs.get(i).uid);

                }

            }

        } else if (inputLineWords.length > 2) {

            String modifiers = inputLineWords[2];

            if (!modifiers.equals("-configurations")) {
                return;
            }

            if (workspace.deviceConstruct != null) {

                for (int i = 0; i < workspace.deviceConstruct.portConstructs.size(); i++) {

                    // Port UID
                    System.out.println("" + workspace.deviceConstruct.portConstructs.get(i).uid);

//                    for (int j = 0; j < workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.size(); j++) {
                    for (int j = 0; j < workspace.deviceConstruct.portConstructs.get(i).configurations.size(); j++) {

//                        for (int k = 0; k < workspace.deviceConstruct.portConstructs.get(i).configurations.get(j).variables.size(); k++) {
//
//                            VariableValueSet variableValueSet = workspace.deviceConstruct.portConstructs.get(i).configurations.get(j).variableValueSets.get(k);
//
////                            System.out.print("\t" + variableValueSet.title + "; ");
//
//                            for (int l = 0; l < variableValueSet.values.values.size(); l++) {
//                                System.out.print("" + variableValueSet.values.values.get(l));
//
//                                if ((l + 1) < variableValueSet.values.values.size()) {
//                                    System.out.print(", ");
//                                }
//                            }
//
//                            if ((k + 1) < workspace.deviceConstruct.portConstructs.get(i).configurations.get(j).variableValueSets.size()) {
//                                System.out.print("; ");
//                            }
//
//                        }

                        int k = 0;
                        for (String variableTitle : workspace.deviceConstruct.portConstructs.get(i).configurations.get(j).variables.keySet()) {

                            List<String> variableValueSet = workspace.deviceConstruct.portConstructs.get(i).configurations.get(j).variables.get(variableTitle).values;

                            for (int l = 0; l < variableValueSet.size(); l++) {
                                System.out.print("" + variableValueSet.get(l));

                                if ((l + 1) < variableValueSet.size()) {
                                    System.out.print(", ");
                                }
                            }

                            if ((k + 1) < workspace.deviceConstruct.portConstructs.get(i).configurations.get(j).variables.size()) {
                                System.out.print("; ");
                            }

                            k++;

                        }

//                        // Mode/Family
//                        System.out.print("\t" + workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).mode + "; ");
//
//                        // Directions
//                        if (workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).directions != null) {
//                            for (int l = 0; l < workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).directions.values.size(); l++) {
//                                System.out.print("" + workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).directions.values.get(l));
//
//                                if ((l + 1) < workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).directions.values.size()) {
//                                    System.out.print(", ");
//                                }
//                            }
//                        }
//                        System.out.print("; ");
//
//                        // Voltages
//                        if (workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).voltages != null) {
//                            for (int k = 0; k < workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).voltages.values.size(); k++) {
//                                System.out.print("" + workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).voltages.values.get(k));
//
//                                if ((k + 1) < workspace.deviceConstruct.portConstructs.get(i).portConfigurationConstraints.get(j).voltages.values.size()) {
//                                    System.out.print(", ");
//                                }
//                            }
//                        }

                        System.out.println();

                    }

                }

            }

        }

    }

    public void editPortTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

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
    public void addPathTask(Context context) {

        if (workspace.deviceConstruct != null) {

            String[] inputLineWords = context.inputLine.split("[ ]+");

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

            // TODO TODO
            // Iterate through configurations for of source port in path. For each source port configurations, check
            // the other ports' configurations for compatibility; then add each compatible configurations to a list of
            // compatible configurations.
//            Tuple<PathConfiguration> consistentPathConfigurations = new ArrayList<>();
            List<HashMap<String, Configuration>> pathConfigurations = new ArrayList<>();
            for (int i = 0; i < pathConstruct.sourcePortConstruct.configurations.size(); i++) {
                Configuration sourcePortConfiguration = pathConstruct.sourcePortConstruct.configurations.get(i);

                for (int j = 0; j < pathConstruct.targetPortConstruct.configurations.size(); j++) {
                    Configuration targetPortConfiguration = pathConstruct.targetPortConstruct.configurations.get(j);

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

                    List<Configuration> compatiblePortConfigurations = Configuration.computeCompatibleConfigurations(sourcePortConfiguration, targetPortConfiguration);

                    if (compatiblePortConfigurations != null) {
                        HashMap<String, Configuration> pathConfiguration = new HashMap<>();
                        pathConfiguration.put("source-port", compatiblePortConfigurations.get(0));
                        pathConfiguration.put("target-port", compatiblePortConfigurations.get(1));
                        pathConfigurations.add(pathConfiguration);
                    }

                    // TODO: Pick up here. Configuration resolution isn't working, probably because of a logic bug in isCompatible(...)
                }
//                System.out.println();
            }

            // If there is only one path configurations in the compatible configurations list, automatically configure
            // the path with it, thereby updating the ports' configurations in the path.
            // TODO: ^
            if (pathConfigurations.size() == 1) {
                // Apply the corresponding configurations to ports.
                HashMap<String, Configuration> pathConfiguration = pathConfigurations.get(0);
                System.out.println("✔ found compatible configurations");

                // TODO: (QUESTION) Can I specify a path configurations and infer port configurations (for multi-port) or should it be a list of port configurations?
                // TODO: Apply values based on per-variable configurations?
                // TODO: Ensure there's only one compatible state for each of the configurations.

                // Source
                System.out.print("  1. mode:" + pathConfiguration.get("source-port").variables.get("mode").values.get(0));
                System.out.print(";direction:");
                for (int k = 0; k < pathConfiguration.get("source-port").variables.get("direction").values.size(); k++) {
                    System.out.print("" + pathConfiguration.get("source-port").variables.get("direction").values.get(k));
                    if ((k + 1) < pathConfiguration.get("source-port").variables.get("direction").values.size()) {
                        System.out.print(", ");
                    }
                }
                System.out.print(";voltage:");
                for (int k = 0; k < pathConfiguration.get("source-port").variables.get("voltage").values.size(); k++) {
                    System.out.print("" + pathConfiguration.get("source-port").variables.get("voltage").values.get(k));
                    if ((k + 1) < pathConfiguration.get("source-port").variables.get("voltage").values.size()) {
                        System.out.print(", ");
                    }
                }
                System.out.print(" | ");

                // Target
                System.out.print("mode:" + pathConfiguration.get("target-port").variables.get("mode").values.get(0));
                System.out.print(";direction:");
                for (int k = 0; k < pathConfiguration.get("target-port").variables.get("direction").values.size(); k++) {
                    System.out.print("" + pathConfiguration.get("target-port").variables.get("direction").values.get(k));
                    if ((k + 1) < pathConfiguration.get("target-port").variables.get("direction").values.size()) {
                        System.out.print(", ");
                    }
                }
                System.out.print(";voltage:");
                for (int k = 0; k < pathConfiguration.get("target-port").variables.get("voltage").values.size(); k++) {
                    System.out.print("" + pathConfiguration.get("target-port").variables.get("voltage").values.get(k));
                    if ((k + 1) < pathConfiguration.get("target-port").variables.get("voltage").values.size()) {
                        System.out.print(", ");
                    }
                }

                System.out.println();

                // Configure the ports with the single compatible configurations
                if (pathConfiguration.get("source-port").variables.get("mode").values.size() == 1) {
                    sourcePortConstruct.variables.get("mode").value = pathConfiguration.get("source-port").variables.get("mode").values.get(0);
                    System.out.println("  ✔ setting mode: " + sourcePortConstruct.variables.get("mode").value);
                }

                if (pathConfiguration.get("source-port").variables.get("direction").values.size() == 1) {
                    sourcePortConstruct.variables.get("direction").value = pathConfiguration.get("source-port").variables.get("direction").values.get(0);
                    System.out.println("  ✔ setting direction: " + sourcePortConstruct.variables.get("direction").value);
                }

                if (pathConfiguration.get("source-port").variables.get("voltage").values.size() == 1) {
                    sourcePortConstruct.variables.get("voltage").value = pathConfiguration.get("source-port").variables.get("voltage").values.get(0);
                    System.out.println("  ✔ setting voltages: " + sourcePortConstruct.variables.get("voltage").value);
                }

            }

            // Otherwise, list the available path configurations and prompt the user to set one of them manually.
            else if (pathConfigurations.size() > 1) {
                // Apply the corresponding configurations to ports.
                System.out.println("✔ found compatible configurations");
                for (int i = 0; i < pathConfigurations.size(); i++) {
//                    PathConfiguration pathConfiguration = consistentPathConfigurations.get(i);
//                    System.out.println("\t[" + i + "] (" + pathConstruct.sourcePortConstruct.uid + ", " + pathConstruct.targetPortConstruct.uid + "): (" + pathConfiguration.configurations.get("source-port").mode + ", ...) --- (" + pathConfiguration.configurations.get("target-port").mode + ", ...)");
                }
                System.out.println("! set one of these configurations");
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

    public void setPathConfigurationTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        String inputPathConfiguration = inputLineWords[3];

        System.out.println("✔ set path configurations to \"" + inputPathConfiguration + "\"");

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
