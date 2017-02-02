package camp.computer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import camp.computer.construct.Construct;
import camp.computer.construct.DeviceConstruct;
import camp.computer.construct.PathConstruct;
import camp.computer.construct.PortConstruct;
import camp.computer.construct.ProcessConstruct;
import camp.computer.construct.ProjectConstruct;
import camp.computer.construct.TaskConstruct;
import camp.computer.data.format.configuration.Configuration;
import camp.computer.data.format.configuration.Variable;
import camp.computer.platform_infrastructure.LoadBuildFileTask;
import camp.computer.util.Pair;
import camp.computer.util.Tuple;
import camp.computer.workspace.Manager;

public class Interpreter {

    private List<String> inputLines = new ArrayList<>();

    // TODO: Move list of Processes into Workspace
    private List<ProcessConstruct> processConstructs = new ArrayList<>(); // TODO: Define namespaces (or just use construct and set type from default "container" to "namespace", then add an interpreter for that type)!

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
            inputLine = scanner.nextLine();
            interpretLine(inputLine);
        }

    }

    public void interpretLine(String inputLine) {

        // <SANITIZE_INPUT>
        if (inputLine.contains("#")) {
            inputLine = inputLine.substring(0, inputLine.indexOf("#"));
        }

        inputLine = inputLine.trim();
        // </SANITIZE_INPUT>

        // <VALIDATE_INPUT>
        if (inputLine.length() == 0) {
            return;
        }
        // </VALIDATE_INPUT>

        if (workspace.processConstruct != null && !inputLine.startsWith("stop")) {
            workspace.processConstruct.operations.add(inputLine);
        } else {

            // Save line in history
            this.inputLines.add(inputLine);

            // Store context
            Context context = new Context();
            context.inputLine = inputLine;

            if (context.inputLine.startsWith("import file")) {
                importFileTask(context);
            } else if (context.inputLine.startsWith("start")) {
                startProcessTask(context);
            } else if (context.inputLine.startsWith("stop")) {
                stopProcessTask(context);
            } else if (context.inputLine.startsWith("do")) {
                doProcessTask(context);
            } else if (context.inputLine.equals("add project")) {
//                addProjectTask();
                addConstructTask(context);
            } else if (context.inputLine.equals("list projects")) {
                listProjectsTask();
            } else if (context.inputLine.startsWith("edit project")) {
                editProjectTask(context);
            } else if (context.inputLine.startsWith("set project title")) {
                setProjectTitleTask(context);
            } else if (context.inputLine.startsWith("remove project")) {
                removeProjectTask(context);
            } else if (context.inputLine.equals("add device")) {
//                addDeviceTask();
                addConstructTask(context);
            } else if (context.inputLine.equals("list devices")) {
                listDevicesTask();
            } else if (context.inputLine.startsWith("edit device")) {
                editDeviceTask(context);
            } else if (context.inputLine.startsWith("remove device")) {
                removeDeviceTask(context);
            } else if (context.inputLine.startsWith("add port")) {
//                addPortTask();
                addConstructTask(context);
            } else if (context.inputLine.startsWith("add configuration")) {
                addConfigurationTask(context);
            } else if (context.inputLine.startsWith("set configuration")) {
                setConfigurationTask(context);
            } else if (context.inputLine.startsWith("list ports")) {
                listPortsTask(context);
            } else if (context.inputLine.startsWith("edit port")) {
                editPortTask(context);
            } else if (context.inputLine.startsWith("remove port")) {
                removePortTask(context);
            } else if (context.inputLine.startsWith("add path")) { // add path device 1 port 3 device 4 port 1
//                addPathTask(context);
                addConstructTask(context);
            } else if (context.inputLine.startsWith("set")) {
                setConstructVariable(context);
            } else if (context.inputLine.startsWith("edit path")) {
                editPathTask(context);
            } else if (context.inputLine.startsWith("list paths")) {
                listPathsTask();
            } else if (context.inputLine.startsWith("remove path")) {
                removePathTask(context);
            } else if (context.inputLine.startsWith("set path configuration")) {
                setPathConfigurationTask(context);
            } else if (context.inputLine.startsWith("add task")) {
//                addTaskTask(context);
                addConstructTask(context);
            } else if (context.inputLine.startsWith("edit task")) {
                editTaskTask(context);
            } else if (context.inputLine.startsWith("remove task")) {
                removeTaskTask(context);
            } else if (context.inputLine.startsWith("solve")) {
                solvePathConfigurationTask(context);
            } else if (context.inputLine.startsWith("exit")) {
                exitTask();
            }

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

    public void startProcessTask(Context context) {
        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 1) {

            workspace.processConstruct = new ProcessConstruct();

        } else if (inputLineWords.length > 1) {

            String address = inputLineWords[1];
            if (address.startsWith("\"") && address.endsWith("\"")) {

                String title = address.substring(1, address.length() - 1);

                workspace.processConstruct = new ProcessConstruct();
                workspace.processConstruct.title = title;

            }

        }

//        System.out.println("✔ edit project " + workspace.projectConstruct.uid);
        System.out.println("> start " + workspace.processConstruct.uid);
    }

    public void stopProcessTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 1) {
            // stop

            processConstructs.add(workspace.processConstruct);

            //        System.out.println("✔ edit project " + workspace.projectConstruct.uid);
            System.out.println("✔ stop " + workspace.processConstruct.uid + " (" + workspace.processConstruct.operations.size() + " operations)");

            // Reset process construct
            workspace.processConstruct = null;

        }

    }

    public void doProcessTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 2) {
            // stop

            System.out.println("lookup: " + inputLineWords[1]);
            ProcessConstruct processConstructConstruct = (ProcessConstruct) Manager.getConstruct(inputLineWords[1]);

            System.out.println("" + processConstructConstruct.uid + ":");

            for (int i = 0; i < processConstructConstruct.operations.size(); i++) {
                // TODO: Add to "command buffer"
                interpretLine(processConstructConstruct.operations.get(i));
            }

        }

        System.out.println("-");

//        System.out.println("✔ edit project " + workspace.projectConstruct.uid);
//        System.out.println("✔ stop " + processConstruct.uid + " (" + processConstruct.operations.size() + " operations)");
    }

    // Format:
    // add <construct-type-tag> <construct-instance-tag>
    //
    // Examples:
    // - add project
    // - add project "my-project"
    public void addConstructTask(Context context) {

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 1) {

            // TODO: Add anonymous construct

        } else if (inputLineWords.length == 2) {

            String constructTypeString = inputLineWords[1];

            if (constructTypeString.equals("project")) {

                ProjectConstruct projectConstruct = new ProjectConstruct();
                workspace.projectConstructs.add(projectConstruct);
                workspace.lastProjectConstruct = projectConstruct; // Store reference to last-created project

            } else if (constructTypeString.equals("host")) {

                // TODO:

            } else if (constructTypeString.equals("device")) {

                // TODO: Ensure edit construct is a project!
                if (workspace.projectConstruct != null) {

                    DeviceConstruct deviceConstruct = new DeviceConstruct();
                    workspace.projectConstruct.deviceConstructs.add(deviceConstruct);
                    workspace.lastDeviceConstruct = deviceConstruct; // Store reference to last-created port

                    System.out.println("✔ add device " + deviceConstruct.uid);
                }

            } else if (constructTypeString.equals("port")) {

                // TODO: Ensure edit construct is a device!
                if (workspace.construct != null && workspace.construct.getClass() == DeviceConstruct.class) {

                    DeviceConstruct deviceConstruct = (DeviceConstruct) workspace.construct;

                    PortConstruct portConstruct = new PortConstruct();
                    deviceConstruct.portConstructs.add(portConstruct);
                    workspace.lastPortConstruct = portConstruct; // Store reference to last-created port

                    System.out.println("✔ add port " + portConstruct.uid + " on device " + deviceConstruct.uid);
                }

            } else if (constructTypeString.equals("path")) {

                // TODO: Ensure edit construct is a device!
                if (workspace.projectConstruct != null) {

                    PathConstruct pathConstruct = new PathConstruct();
//                    pathConstruct.sourcePortConstruct = sourcePortConstruct;
//                    pathConstruct.targetPortConstruct = targetPortConstruct;
                    workspace.projectConstruct.pathConstructs.add(pathConstruct);
                    workspace.lastPathConstruct = pathConstruct; // Store reference to last-created port

                    System.out.println("✔ add path " + pathConstruct.uid + " to project " + workspace.projectConstruct.uid);
                }

            } else if (constructTypeString.equals("task")) {

                if (workspace.construct != null && workspace.construct.getClass() == DeviceConstruct.class) {

                    DeviceConstruct deviceConstruct = (DeviceConstruct) workspace.construct;

                    TaskConstruct taskConstruct = new TaskConstruct();
                    deviceConstruct.scheduleConstruct.taskConstructs.add(taskConstruct);

                    // Store reference to last-created device
                    workspace.lastTaskConstruct = taskConstruct;

                    System.out.println("✔ add task " + taskConstruct.uid + " to device " + deviceConstruct.uid);

                }

            }

//            System.out.println("✔ add " + constructTypeString + " " + projectConstruct.uid);

        } else if (inputLineWords.length > 2) {

            String constructTypeString = inputLineWords[1];
            String constructTitleString = inputLineWords[2];

            if (constructTypeString.equals("project")) {

                ProjectConstruct projectConstruct = new ProjectConstruct();
                projectConstruct.title = constructTitleString;
                workspace.projectConstructs.add(projectConstruct);
                workspace.lastProjectConstruct = projectConstruct; // Store reference to last-created project

            } else if (constructTypeString.equals("host")) {

                // TODO:

            } else if (constructTypeString.equals("device")) {

                // TODO: Ensure edit construct is a project!
                if (workspace.projectConstruct != null) {

                    DeviceConstruct deviceConstruct = new DeviceConstruct();
                    deviceConstruct.title = constructTitleString;
                    workspace.projectConstruct.deviceConstructs.add(deviceConstruct);
                    workspace.lastDeviceConstruct = deviceConstruct; // Store reference to last-created port

                    System.out.println("✔ add device " + deviceConstruct.uid);
                }

            } else if (constructTypeString.equals("port")) {

                // TODO: Ensure edit construct is a device!
                if (workspace.construct != null && workspace.construct.getClass() == DeviceConstruct.class) {

                    DeviceConstruct deviceConstruct = (DeviceConstruct) workspace.construct;

                    PortConstruct portConstruct = new PortConstruct();
                    portConstruct.title = constructTitleString;
                    deviceConstruct.portConstructs.add(portConstruct);
                    workspace.lastPortConstruct = portConstruct; // Store reference to last-created port

                    System.out.println("✔ add port " + portConstruct.uid + " on device " + deviceConstruct.uid);
                }

            } else if (constructTypeString.equals("path")) {

                // TODO:

            } else if (constructTypeString.equals("task")) {

                if (workspace.construct != null && workspace.construct.getClass() == DeviceConstruct.class) {

                    DeviceConstruct deviceConstruct = (DeviceConstruct) workspace.construct;

                    TaskConstruct taskConstruct = new TaskConstruct();
                    taskConstruct.title = constructTitleString;
                    deviceConstruct.scheduleConstruct.taskConstructs.add(taskConstruct);

                    // Store reference to last-created device
                    workspace.lastTaskConstruct = taskConstruct;

                    System.out.println("✔ add task " + taskConstruct.uid + " to device " + deviceConstruct.uid);

                }

            }

//            System.out.println("✔ add " + constructTypeString + " " + projectConstruct.uid);

        }

    }

//    public void addProjectTask() {
//        ProjectConstruct projectConstruct = new ProjectConstruct();
//        System.out.println("✔ add project " + projectConstruct.uid);
//
//        workspace.projectConstructs.add(projectConstruct);
//
//        // Store reference to last-created project
//        workspace.lastProjectConstruct = projectConstruct;
//    }

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

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        Construct construct = null;

        if (inputLineWords.length == 2) {
            construct = workspace.lastProjectConstruct;
        } else if (inputLineWords.length > 2) {
            construct = Manager.getConstruct(inputLineWords[2]);
        }

        if (construct != null) {
            workspace.projectConstruct = (ProjectConstruct) construct;
            System.out.println("✔ edit project " + workspace.projectConstruct.uid);
        }

    }

    public void setProjectTitleTask(Context context) {

        // TODO: Lookup context.get("inputLine")
        if (workspace.projectConstruct != null) {

            String[] inputLineWords = context.inputLine.split("[ ]+");

            String inputProjectTitle = inputLineWords[3];

            workspace.projectConstruct.title = inputProjectTitle;

            System.out.println("project title changed to " + inputProjectTitle);
        }

    }

    public void removeProjectTask(Context context) {

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 2) {

            // Remove the project referenced in workspace.lastProjectConstruct

        } else if (inputLineWords.length > 2) {

            // Remove the project with the specified index, tag, UID, UUID.

        }
    }

//    public void addDeviceTask() {
//
//        DeviceConstruct deviceConstruct = new DeviceConstruct();
//        System.out.println("✔ add device " + deviceConstruct.uid);
//
//        workspace.projectConstruct.deviceConstructs.add(deviceConstruct);
//
//        // Store reference to last-created device
//        workspace.lastDeviceConstruct = deviceConstruct;
//
//    }

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

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        Construct deviceConstruct = null;

        if (inputLineWords.length == 2) {
            deviceConstruct = workspace.lastDeviceConstruct;
        } else if (inputLineWords.length > 2) {
            deviceConstruct = Manager.getConstruct(inputLineWords[2]);
        }

        if (deviceConstruct != null) {

            workspace.construct = deviceConstruct;
            System.out.println("✔ edit device " + deviceConstruct.uid);

        } else {

            // No port was found with the specified identifier (UID, UUID, tag, index)

        }

    }

    public void removeDeviceTask(Context context) {

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 2) {

            // Remove the device referenced in workspace.lastDeviceConstruct

        } else if (inputLineWords.length > 2) {

            // Remove the device with the specified index, tag, UID, UUID.

        }
    }

//    public void addPortTask() {
//
//        if (workspace.deviceConstruct != null) {
//
//            PortConstruct portConstruct = new PortConstruct();
//            System.out.println("✔ add port " + portConstruct.uid + " on device " + workspace.deviceConstruct.uid);
//
//            workspace.deviceConstruct.portConstructs.add(portConstruct);
//
//            // Store reference to last-created port
//            workspace.lastPortConstruct = portConstruct;
//
//        }
//
//    }

    // e.g., add configuration uart(tx);output;ttl,cmos
    public void addConfigurationTask(Context context) {

        // TODO: Parse "bus(line)" value string pattern to create bus and lines.

        String[] inputLineWords = context.inputLine.split("[ ]+");

        String configurationOptionString = inputLineWords[2];

        String[] configurationVariableList = configurationOptionString.split(";");

        List<Pair<String, Tuple<String>>> variableValueSets = new ArrayList<>();

        PortConstruct portConstruct = null;
        if (workspace.construct != null && workspace.construct.getClass() == PortConstruct.class) {
            portConstruct = (PortConstruct) workspace.construct;
        }

        for (int i = 0; i < configurationVariableList.length; i++) {

            String[] configurationAssignmentList = configurationVariableList[i].split(":");
            String variableTitle = configurationAssignmentList[0];
            String variableValues = configurationAssignmentList[1];

            // <HACK>
            if (!portConstruct.variables.containsKey(variableTitle)) {
                portConstruct.variables.put(variableTitle, new Variable(variableTitle));
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

        // Add VariableMap Option/Configuration
        portConstruct.configurations.add(new Configuration(variableValueSets));

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

            if (workspace.construct != null && workspace.construct.getClass() == DeviceConstruct.class) {

                DeviceConstruct deviceConstruct = (DeviceConstruct) workspace.construct;

                for (int i = 0; i < deviceConstruct.portConstructs.size(); i++) {

                    // Port UID
                    System.out.println("" + deviceConstruct.portConstructs.get(i).uid);

                }

            }

        } else if (inputLineWords.length > 2) {

            String modifiers = inputLineWords[2];

            if (!modifiers.equals("-configurations")) {
                return;
            }

            if (workspace.construct != null && workspace.construct.getClass() == DeviceConstruct.class) {

                DeviceConstruct deviceConstruct = (DeviceConstruct) workspace.construct;

                for (int i = 0; i < deviceConstruct.portConstructs.size(); i++) {

                    // Port UID
                    System.out.println("" + deviceConstruct.portConstructs.get(i).uid);

                    for (int j = 0; j < deviceConstruct.portConstructs.get(i).configurations.size(); j++) {

                        int k = 0;
                        for (String variableTitle : deviceConstruct.portConstructs.get(i).configurations.get(j).variables.keySet()) {

                            List<String> variableValueSet = deviceConstruct.portConstructs.get(i).configurations.get(j).variables.get(variableTitle).values;

                            for (int l = 0; l < variableValueSet.size(); l++) {
                                System.out.print("" + variableValueSet.get(l));

                                if ((l + 1) < variableValueSet.size()) {
                                    System.out.print(", ");
                                }
                            }

                            if ((k + 1) < deviceConstruct.portConstructs.get(i).configurations.get(j).variables.size()) {
                                System.out.print("; ");
                            }

                            k++;

                        }

                        System.out.println();

                    }

                }

            }

        }

    }

    public void editPortTask(Context context) {

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        Construct portConstruct = null;

        if (inputLineWords.length == 2) {
            portConstruct = workspace.lastPortConstruct;
        } else if (inputLineWords.length > 2) {
            portConstruct = Manager.getConstruct(inputLineWords[2]);
        }

        if (portConstruct != null) {

            workspace.construct = portConstruct;
            System.out.println("✔ edit port " + workspace.construct.uid);

        } else {

            // No port was found with the specified identifier (UID, UUID, tag, index)

        }

    }

    public void removePortTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)
        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 2) {

            // Remove the port referenced in workspace.lastPortConstruct

        } else if (inputLineWords.length > 2) {

            // Remove the device with the specified index, tag, UID, UUID.

        }
    }

    // e.g., add path device 1 port 3 device 4 port 1
    public void addPathTask(Context context) {

        // add path <title>
        // edit path
        // set source-port[construct-type] uid:34
        // set target-port[construct-type] uid:34

        if (workspace.construct != null && workspace.construct.getClass() == DeviceConstruct.class) {

            DeviceConstruct deviceConstruct = (DeviceConstruct) workspace.construct;

            String[] inputLineWords = context.inputLine.split("[ ]+");

            // TODO: Parse address token (for index, UID, UUID; title/key/tag)

            // <TODO>
            // TODO: Remove messages!
            DeviceConstruct sourceDeviceConstruct = (DeviceConstruct) Manager.getConstruct(inputLineWords[3]);
            PortConstruct sourcePortConstruct = (PortConstruct) Manager.getConstruct(inputLineWords[5]);
            DeviceConstruct targetDeviceConstruct = (DeviceConstruct) Manager.getConstruct(inputLineWords[7]);
            PortConstruct targetPortConstruct = (PortConstruct) Manager.getConstruct(inputLineWords[9]);
            // </TODO>

            PathConstruct pathConstruct = new PathConstruct();
            pathConstruct.sourcePortConstruct = sourcePortConstruct;
            pathConstruct.targetPortConstruct = targetPortConstruct;

            workspace.projectConstruct.pathConstructs.add(pathConstruct);

            System.out.println("✔ add path " + pathConstruct.uid + " from device " + sourceDeviceConstruct.uid + " port " + sourcePortConstruct.uid + " to device " + targetDeviceConstruct.uid + " port " + targetPortConstruct.uid);


            /**
             * "solve path [uid]"
             */

            // TODO: Resolve set of available configurations for path based on compatible configurations of ports in the path.

            // Iterate through configurations for of source port in path. For each source port configurations, check
            // the other ports' configurations for compatibility; then add each compatible configurations to a list of
            // compatible configurations.
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
                // TODO: print PORT ADDRESS
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
                // TODO: print PORT ADDRESS
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

    public void solvePathConfigurationTask(Context context) {

        // solve uid(34)
        // solve path <path-address>

        // add path <title>
        // edit path
        // set source-port[construct-type] uid:34
        // set target-port[construct-type] uid:34

//        if (workspace.construct != null && workspace.construct.getClass() == DeviceConstruct.class) {
//        if (workspace.construct != null && workspace.construct.getClass() == PathConstruct.class) {

//            DeviceConstruct deviceConstruct = (DeviceConstruct) workspace.construct;

        String[] inputLineWords = context.inputLine.split("[ ]+");

        // TODO: Parse address token (for index, UID, UUID; title/key/tag)

        // <TODO>
        // TODO: Remove messages!
//            DeviceConstruct sourceDeviceConstruct = (DeviceConstruct) Manager.getConstruct(inputLineWords[3]);
//            PortConstruct sourcePortConstruct = (PortConstruct) Manager.getConstruct(inputLineWords[5]);
//            DeviceConstruct targetDeviceConstruct = (DeviceConstruct) Manager.getConstruct(inputLineWords[7]);
//            PortConstruct targetPortConstruct = (PortConstruct) Manager.getConstruct(inputLineWords[9]);
        // </TODO>

//            PathConstruct pathConstruct = new PathConstruct();
//            pathConstruct.sourcePortConstruct = sourcePortConstruct;
//            pathConstruct.targetPortConstruct = targetPortConstruct;
//
//            workspace.projectConstruct.pathConstructs.add(pathConstruct);
//
//            System.out.println("✔ add path " + pathConstruct.uid + " from device " + sourceDeviceConstruct.uid + " port " + sourcePortConstruct.uid + " to device " + targetDeviceConstruct.uid + " port " + targetPortConstruct.uid);

        PathConstruct pathC = (PathConstruct) Manager.getConstruct(inputLineWords[1]);


        /**
         * "solve path [uid]"
         */

        // TODO: Resolve set of available configurations for path based on compatible configurations of ports in the path.

        // Iterate through configurations for of source port in path. For each source port configurations, check
        // the other ports' configurations for compatibility; then add each compatible configurations to a list of
        // compatible configurations.
        List<HashMap<String, Configuration>> pathConfigurations = new ArrayList<>();
//            for (int i = 0; i < pathConstruct.sourcePortConstruct.configurations.size(); i++) {
        for (int i = 0; i < pathC.sourcePortConstruct.configurations.size(); i++) {
//                Configuration sourcePortConfiguration = pathConstruct.sourcePortConstruct.configurations.get(i);
            Configuration sourcePortConfiguration = pathC.sourcePortConstruct.configurations.get(i);

//                for (int j = 0; j < pathConstruct.targetPortConstruct.configurations.size(); j++) {
            for (int j = 0; j < pathC.targetPortConstruct.configurations.size(); j++) {
//                    Configuration targetPortConfiguration = pathConstruct.targetPortConstruct.configurations.get(j);
                Configuration targetPortConfiguration = pathC.targetPortConstruct.configurations.get(j);

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
            // TODO: print PORT ADDRESS
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
            // TODO: print PORT ADDRESS
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
//                    sourcePortConstruct.variables.get("mode").value = pathConfiguration.get("source-port").variables.get("mode").values.get(0);
//                    System.out.println("  ✔ setting mode: " + sourcePortConstruct.variables.get("mode").value);
                pathC.sourcePortConstruct.variables.get("mode").value = pathConfiguration.get("source-port").variables.get("mode").values.get(0);
                System.out.println("  ✔ setting mode: " + pathC.sourcePortConstruct.variables.get("mode").value);
            }

            if (pathConfiguration.get("source-port").variables.get("direction").values.size() == 1) {
//                    sourcePortConstruct.variables.get("direction").value = pathConfiguration.get("source-port").variables.get("direction").values.get(0);
//                    System.out.println("  ✔ setting direction: " + sourcePortConstruct.variables.get("direction").value);
                pathC.sourcePortConstruct.variables.get("direction").value = pathConfiguration.get("source-port").variables.get("direction").values.get(0);
                System.out.println("  ✔ setting direction: " + pathC.sourcePortConstruct.variables.get("direction").value);
            }

            if (pathConfiguration.get("source-port").variables.get("voltage").values.size() == 1) {
//                    sourcePortConstruct.variables.get("voltage").value = pathConfiguration.get("source-port").variables.get("voltage").values.get(0);
//                    System.out.println("  ✔ setting voltages: " + sourcePortConstruct.variables.get("voltage").value);
                pathC.sourcePortConstruct.variables.get("voltage").value = pathConfiguration.get("source-port").variables.get("voltage").values.get(0);
                System.out.println("  ✔ setting voltages: " + pathC.sourcePortConstruct.variables.get("voltage").value);
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
//        }

    }

    public void setConstructVariable(Context context) {

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 1) {

            // TODO: Add anonymous construct

        } else if (inputLineWords.length == 2) {

            String assignmentString = inputLineWords[1];

            String[] assignmentTokens = assignmentString.split(":");

            String variableTitle = assignmentTokens[0];
            String variableValue = assignmentTokens[1];

            if (workspace.construct.getClass() == PathConstruct.class) {
//            if (constructTypeString.equals("path")) {

                // set path source-port uid:4

                PathConstruct pathConstruct = (PathConstruct) workspace.construct;

                if (variableTitle.equals("source-port")) {

                    PortConstruct sourcePort = (PortConstruct) Manager.getConstruct(variableValue);
                    pathConstruct.sourcePortConstruct = sourcePort;

                    System.out.println(">>> set source-port " + variableValue);

                } else if (variableTitle.equals("target-port")) {

                    PortConstruct targetPort = (PortConstruct) Manager.getConstruct(variableValue);
                    pathConstruct.targetPortConstruct = targetPort;

                    System.out.println(">>> set target-port " + variableValue);

                }

            }
//            else if (assignmentString.equals("host")) {
//
//                // TODO:
//
//            } else if (assignmentString.equals("device")) {
//
//            }

        }

    }

    public void editPathTask(Context context) {
        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        Construct pathConstruct = null;

        if (inputLineWords.length == 2) {
            pathConstruct = workspace.lastPathConstruct;
        } else if (inputLineWords.length > 2) {
            pathConstruct = Manager.getConstruct(inputLineWords[2]);
        }

        if (pathConstruct != null) {

            workspace.construct = pathConstruct;
            System.out.println("✔ edit path " + workspace.construct.uid);

        } else {

            // No port was found with the specified identifier (UID, UUID, tag, index)

        }
    }

    public void listPathsTask() {

        if (workspace.projectConstruct != null) {

            for (int i = 0; i < workspace.projectConstruct.pathConstructs.size(); i++) {
                System.out.println("" + workspace.projectConstruct.pathConstructs.get(i).uid + " (port " + workspace.projectConstruct.pathConstructs.get(i).sourcePortConstruct.uid + ", port " + workspace.projectConstruct.pathConstructs.get(i).targetPortConstruct.uid + ")");
            }

        }

    }

    public void removePathTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)
        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 2) {

            // Remove the device referenced in workspace.lastPathConstruct

        } else if (inputLineWords.length > 2) {

            // Remove the path with the specified index, tag, UID, UUID.

        }
    }

    public void setPathConfigurationTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        String inputPathConfiguration = inputLineWords[3];

        System.out.println("✔ set path configuration to \"" + inputPathConfiguration + "\"");

        // protocols:
        // - electronic, rf, none

        // electronic:
        // - voltage
    }

//    public void addTaskTask(Context context) {
//
//        if (workspace.deviceConstruct != null) {
//
//            TaskConstruct taskConstruct = new TaskConstruct();
//            workspace.deviceConstruct.scheduleConstruct.taskConstructs.add(taskConstruct);
//
//            // Store reference to last-created device
//            workspace.lastTaskConstruct = taskConstruct;
//
//            System.out.println("✔ add task " + taskConstruct.uid + " to device " + workspace.deviceConstruct.uid);
//
//        }
//
//    }

    public void editTaskTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)

        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 2) {

            Workspace.setConstruct(workspace, workspace.lastTaskConstruct);

        } else if (inputLineWords.length > 2) {

//            String inputTaskIdentifier = inputLineWords[2];
//            if (inputTaskIdentifier.startsWith("uid:")) {
//
//                long inputTaskUid = Long.valueOf(inputTaskIdentifier.split(":")[1]);
//
//                for (int i = 0; i < workspace.projectConstruct.deviceConstructs.size(); i++) {
//                    for (int j = 0; j < workspace.projectConstruct.deviceConstructs.get(i).scheduleConstruct.taskConstructs.size(); j++) {
//                        if (workspace.projectConstruct.deviceConstructs.get(i).scheduleConstruct.taskConstructs.get(j).uid == inputTaskUid) {
//                            workspace.taskConstruct = workspace.projectConstruct.deviceConstructs.get(i).scheduleConstruct.taskConstructs.get(j);
//                            break;
//                        }
//                    }
//                }
//
//            } else if (inputTaskIdentifier.startsWith("uuid:")) {
//
//                UUID inputTaskUuid = UUID.fromString(inputTaskIdentifier.split(":")[1]);
//
//                for (int i = 0; i < workspace.projectConstruct.deviceConstructs.size(); i++) {
//                    for (int j = 0; j < workspace.projectConstruct.deviceConstructs.get(i).scheduleConstruct.taskConstructs.size(); j++) {
//                        if (workspace.projectConstruct.deviceConstructs.get(i).scheduleConstruct.taskConstructs.get(j).uuid.equals(inputTaskUuid)) {
//                            workspace.taskConstruct = workspace.projectConstruct.deviceConstructs.get(i).scheduleConstruct.taskConstructs.get(j);
//                            break;
//                        }
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

        System.out.println("✔ edit task " + workspace.construct.uid);
    }

    public void removeTaskTask(Context context) {
        // TODO: Change argument to "Context context" (temporary cache/manager)
        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 2) {

            // Remove the path referenced in workspace.lastPathConstruct

        } else if (inputLineWords.length > 2) {

            // Remove the path with the specified index, tag, UID, UUID.

        }
    }

    public void exitTask() {
        System.exit(0);
    }
    // </REFACTOR>
}


// TODO: PORTS CAN BE "WIRELESS" AND SPREAD OUT ACROSS BOARDS JUST LIKE CLAY BOARDS CAN BE SPREAD OUT IN SPACE.
