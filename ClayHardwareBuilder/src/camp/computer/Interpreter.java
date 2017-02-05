package camp.computer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import camp.computer.construct.Construct;
import camp.computer.construct.DeviceConstruct;
import camp.computer.construct.HostConstruct;
import camp.computer.construct.PathConstruct;
import camp.computer.construct.PortConstruct;
import camp.computer.construct.ProcessConstruct;
import camp.computer.construct.ProjectConstruct;
import camp.computer.construct.ScheduleConstruct;
import camp.computer.construct.ScriptConstruct;
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
            } else if (context.inputLine.startsWith("save")) {
                saveConstructVersion(context);
            } else if (context.inputLine.startsWith("restore")) {
                restoreConstructVersion(context);
            }
            // <REFACTOR>
            else if (context.inputLine.startsWith("add configuration")) {
                addConfigurationTask(context);
            }
            // </REFACTOR>
            else if (context.inputLine.startsWith("add")) {
                addConstructTask(context);
            } else if (context.inputLine.startsWith("list")) {
                listConstructsTask(context);
//                listProjectsTask();
//                listDevicesTask();
//                listPortsTask(context);
//                listPathsTask();
            } else if (context.inputLine.startsWith("edit project")) {
//                editProjectTask(context);
                editConstructTask(context);
            } else if (context.inputLine.startsWith("edit device")) {
//                editDeviceTask(context);
                editConstructTask(context);
            } else if (context.inputLine.startsWith("edit port")) {
//                editPortTask(context);
                editConstructTask(context);
            } else if (context.inputLine.startsWith("edit path")) {
//                editPathTask(context);
                editConstructTask(context);
            } else if (context.inputLine.startsWith("edit task")) {
//                editTaskTask(context);
                editConstructTask(context);
            } else if (context.inputLine.startsWith("set project title")) {
//                setProjectTitleTask(context);
            } else if (context.inputLine.startsWith("remove")) {
                removeConstructTask(context);
            }
            // <REFACTOR>
            else if (context.inputLine.startsWith("set configuration")) {
                setConfigurationTask(context);
            } else if (context.inputLine.startsWith("set path configuration")) {
                setPathConfigurationTask(context);
            }
            // </REFACTOR>
            else if (context.inputLine.startsWith("set")) {
                setConstructVariable(context);
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

        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 1) {

            processConstructs.add(workspace.processConstruct);

            System.out.println("✔ stop " + workspace.processConstruct.uid + " (" + workspace.processConstruct.operations.size() + " operations)");

            // Reset process construct
            workspace.processConstruct = null;

        }

    }

    public void doProcessTask(Context context) {

        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 2) {

            ProcessConstruct processConstructConstruct = (ProcessConstruct) Manager.get(inputLineWords[1]);

            System.out.println("> do " + processConstructConstruct.uid);

            for (int i = 0; i < processConstructConstruct.operations.size(); i++) {
                // TODO: Add to "command buffer"
                interpretLine(processConstructConstruct.operations.get(i));
            }

        }

//        System.out.println("✔ stop " + workspace.processConstruct.uid + " (" + workspace.processConstruct.operations.size() + " operations)");

    }

    // push
    public void saveConstructVersion(Context context) {

        // TODO: Save new snapshot as a child/successor of the current construct version

        System.out.println("✔ save (revision XXX)");

    }

    // checkout
    public void restoreConstructVersion(Context context) {

        System.out.println("✔ restore (revision XXX)");

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

                // TODO: Ensure edit construct is a device!
                if (workspace.construct != null && workspace.construct.getClass() == ProjectConstruct.class) {

                    ProjectConstruct projectConstruct = (ProjectConstruct) workspace.construct;

                    DeviceConstruct deviceConstruct = new DeviceConstruct();
                    projectConstruct.deviceConstructs.add(deviceConstruct);
                    workspace.lastDeviceConstruct = deviceConstruct; // Store reference to last-created device

                    System.out.println("✔ add device " + deviceConstruct.uid + " to project " + projectConstruct.uid);
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
                if (workspace.construct != null && workspace.construct.getClass() == ProjectConstruct.class) {

                    ProjectConstruct projectConstruct = (ProjectConstruct) workspace.construct;

                    PathConstruct pathConstruct = new PathConstruct();
                    projectConstruct.pathConstructs.add(pathConstruct);
                    workspace.lastPathConstruct = pathConstruct; // Store reference to last-created port

                    System.out.println("✔ add path " + pathConstruct.uid + " to project " + projectConstruct.uid);
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

//                // TODO: Ensure edit construct is a project!
//                if (workspace.projectConstruct != null) {
//
//                    DeviceConstruct deviceConstruct = new DeviceConstruct();
//                    deviceConstruct.title = constructTitleString;
//                    workspace.projectConstruct.deviceConstructs.add(deviceConstruct);
//                    workspace.lastDeviceConstruct = deviceConstruct; // Store reference to last-created port
//
//                    System.out.println("✔ add device " + deviceConstruct.uid);
//                }

                // TODO: Ensure edit construct is a device!
                if (workspace.construct != null && workspace.construct.getClass() == ProjectConstruct.class) {

                    ProjectConstruct projectConstruct = (ProjectConstruct) workspace.construct;

                    DeviceConstruct deviceConstruct = new DeviceConstruct();
                    projectConstruct.deviceConstructs.add(deviceConstruct);
                    workspace.lastDeviceConstruct = deviceConstruct; // Store reference to last-created device

                    deviceConstruct.title = constructTitleString;

                    System.out.println("✔ add device " + deviceConstruct.uid + " to project " + projectConstruct.uid);
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

    /**
     * <strong>Examples</strong>
     * {@code list <construct-type>}
     *
     * @param context
     */
    public void listConstructsTask(Context context) {

        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 1) {

            // TODO: List all constructs!

        } else if (inputLineWords.length == 2) {

            String constructTypeString = inputLineWords[1];

            Class constructType = null;
            if (constructTypeString.equals("projects")) {
                constructType = ProjectConstruct.class;
            } else if (constructTypeString.equals("hosts")) {
                constructType = HostConstruct.class;
            } else if (constructTypeString.equals("devices")) {
                constructType = DeviceConstruct.class;
            } else if (constructTypeString.equals("ports")) {
                constructType = PortConstruct.class;
            } else if (constructTypeString.equals("paths")) {
                constructType = PathConstruct.class;
            } else if (constructTypeString.equals("schedules")) {
                constructType = ScheduleConstruct.class;
            } else if (constructTypeString.equals("tasks")) {
                constructType = TaskConstruct.class;
            }

            for (Construct construct : Manager.elements.values()) {
                if (construct.getClass() == constructType) {
                    // System.out.println("" + construct.uid + "\t" + construct.uuid.toString());
                    System.out.println("" + construct.uid);
                }

                if (construct.getClass() == DeviceConstruct.class) {
                    List<PortConstruct> unassignedPorts = DeviceConstruct.getUnassignedPorts((DeviceConstruct) construct);
                    System.out.print("Unassigned: ");
                    for (int j = 0; j < unassignedPorts.size(); j++) {
                        System.out.print("" + unassignedPorts.get(j).uid + " ");
                    }
                    System.out.println();
                }
            }

        }
    }

    public void editConstructTask(Context context) {
        // TODO: Lookup context.get("inputLine")
        String[] inputLineWords = context.inputLine.split("[ ]+");

        Construct construct = null;

        String constructTypeString = inputLineWords[1];

        if (inputLineWords.length == 2) {

            if (constructTypeString.equals("project")) {
                construct = workspace.lastProjectConstruct;
            } else if (constructTypeString.equals("host")) {
                construct = workspace.lastHostConstruct;
            } else if (constructTypeString.equals("device")) {
                construct = workspace.lastDeviceConstruct;
            } else if (constructTypeString.equals("port")) {
                construct = workspace.lastPortConstruct;
            } else if (constructTypeString.equals("path")) {
                construct = workspace.lastPathConstruct;
            } else if (constructTypeString.equals("schedule")) {
                construct = workspace.lastScheduleConstruct;
            } else if (constructTypeString.equals("task")) {
                construct = workspace.lastTaskConstruct;
            }

        } else if (inputLineWords.length > 2) {

            construct = Manager.get(inputLineWords[2]);

        }

        if (construct != null) {

            workspace.construct = construct;
            System.out.println("✔ edit " + constructTypeString + " " + workspace.construct.uid);

        } else {

            // No port was found with the specified identifier (UID, UUID, tag, index)

        }
    }

    public void removeConstructTask(Context context) {

        String[] inputLineWords = context.inputLine.split("[ ]+");

        if (inputLineWords.length == 1) {

            // TODO: List all constructs!

        } else if (inputLineWords.length == 2) {

            String addressString = inputLineWords[1];

            Construct construct = Manager.get(addressString);

            if (construct != null) {
                // TODO: Remove construct
            }

        }
    }

//    public void editProjectTask(Context context) {
//
//        // TODO: Lookup context.get("inputLine")
//        String[] inputLineWords = context.inputLine.split("[ ]+");
//
//        Construct construct = null;
//
//        if (inputLineWords.length == 2) {
//            construct = workspace.lastProjectConstruct;
//        } else if (inputLineWords.length > 2) {
//            construct = Manager.get(inputLineWords[2]);
//        }
//
//        if (construct != null) {
//            workspace.projectConstruct = (ProjectConstruct) construct;
//            System.out.println("✔ edit project " + workspace.projectConstruct.uid);
//        }
//
//    }
//
//    public void editDeviceTask(Context context) {
//
//        // TODO: Lookup context.get("inputLine")
//        String[] inputLineWords = context.inputLine.split("[ ]+");
//
//        Construct deviceConstruct = null;
//
//        if (inputLineWords.length == 2) {
//            deviceConstruct = workspace.lastDeviceConstruct;
//        } else if (inputLineWords.length > 2) {
//            deviceConstruct = Manager.get(inputLineWords[2]);
//        }
//
//        if (deviceConstruct != null) {
//
//            workspace.construct = deviceConstruct;
//            System.out.println("✔ edit device " + deviceConstruct.uid);
//
//        } else {
//
//            // No port was found with the specified identifier (UID, UUID, tag, index)
//
//        }
//
//    }
//
//    public void editPortTask(Context context) {
//
//        // TODO: Lookup context.get("inputLine")
//        String[] inputLineWords = context.inputLine.split("[ ]+");
//
//        Construct portConstruct = null;
//
//        if (inputLineWords.length == 2) {
//            portConstruct = workspace.lastPortConstruct;
//        } else if (inputLineWords.length > 2) {
//            portConstruct = Manager.get(inputLineWords[2]);
//        }
//
//        if (portConstruct != null) {
//
//            workspace.construct = portConstruct;
//            System.out.println("✔ edit port " + workspace.construct.uid);
//
//        } else {
//
//            // No port was found with the specified identifier (UID, UUID, tag, index)
//
//        }
//
//    }
//
//    public void editPathTask(Context context) {
//        // TODO: Lookup context.get("inputLine")
//        String[] inputLineWords = context.inputLine.split("[ ]+");
//
//        Construct pathConstruct = null;
//
//        if (inputLineWords.length == 2) {
//            pathConstruct = workspace.lastPathConstruct;
//        } else if (inputLineWords.length > 2) {
//            pathConstruct = Manager.get(inputLineWords[2]);
//        }
//
//        if (pathConstruct != null) {
//
//            workspace.construct = pathConstruct;
//            System.out.println("✔ edit path " + workspace.construct.uid);
//
//        } else {
//
//            // No port was found with the specified identifier (UID, UUID, tag, index)
//
//        }
//    }
//
//    public void editTaskTask(Context context) {
//        // TODO: Change argument to "Context context" (temporary cache/manager)
//
//        // TODO: Lookup context.get("inputLine")
//        String[] inputLineWords = context.inputLine.split("[ ]+");
//
//        if (inputLineWords.length == 2) {
//
//            Workspace.setConstruct(workspace, workspace.lastTaskConstruct);
//
//        } else if (inputLineWords.length > 2) {
//
//        }
//
//        System.out.println("✔ edit task " + workspace.construct.uid);
//    }
//
//    public void setProjectTitleTask(Context context) {
//
//        // TODO: Lookup context.get("inputLine")
//        if (workspace.projectConstruct != null) {
//
//            String[] inputLineWords = context.inputLine.split("[ ]+");
//
//            String inputProjectTitle = inputLineWords[3];
//
//            workspace.projectConstruct.title = inputProjectTitle;
//
//            System.out.println("project title changed to " + inputProjectTitle);
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
//                portConstruct.variables.put(variableTitle, new Variable(variableTitle));
                portConstruct.variables.put(variableTitle, null);
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

    /**
     * Given a project specification and a workspace, search unassigned ports on discovered and
     * virtual hosts for the port configuration dependencies of the project's extension device
     * requirements.
     */
    public void autoAssembleProjectWithWorkspace() {

    }

    /**
     * Given a device and a workspace, ...
     */
    public void autoAssembleDeviceWithWorkspace() {

    }

    /**
     * Given a device and a host, ...
     */
    public void autoAssembleDeviceWithHost() {

    }

    public void autoAssemblePortWithWorkspace() {

    }

    public void autoAssemblePortWithHost() {

    }

    public void autoAssemblePortWithPort() {

    }

    /**
     * Selects devices (and ports?) with unassigned ports that are compatible with the specified
     * path configuration.
     *
     * @param context
     */
    public void solveDeviceConfigurationTask(Context context) {

        // 1. Given two devices and a path, selects ports on respective paths that are compatible,
        //    if any.
        // 2. Given a path, selects the devices and then searches for a compatible port pairing
        //    (as in 1), that satisfied the path's dependencies.
        // 3. Same as 2, but for a set of paths.
        // 4. Same as 1, but for a set of paths.

    }

    /**
     * Given a path with containing two ports, determines compatible configurations (if any).
     * <p>
     * "solve <path-construct>"
     * e.g., solve uid(34)
     *
     * @param context
     */
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

        PathConstruct pathConstruct = (PathConstruct) Manager.get(inputLineWords[1]);

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
                if (pathConstruct.sourcePortConstruct.variables.containsKey("mode")
                        && pathConstruct.sourcePortConstruct.variables.get("mode") == null) {
                    pathConstruct.sourcePortConstruct.variables.put("mode", new Variable("mode"));
                }
                pathConstruct.sourcePortConstruct.variables.get("mode").value = pathConfiguration.get("source-port").variables.get("mode").values.get(0);
                System.out.println("  ✔ setting mode: " + pathConstruct.sourcePortConstruct.variables.get("mode").value);
            }

            if (pathConfiguration.get("source-port").variables.get("direction").values.size() == 1) {
                if (pathConstruct.sourcePortConstruct.variables.containsKey("direction")
                        && pathConstruct.sourcePortConstruct.variables.get("direction") == null) {
                    pathConstruct.sourcePortConstruct.variables.put("direction", new Variable("direction"));
                }
                pathConstruct.sourcePortConstruct.variables.get("direction").value = pathConfiguration.get("source-port").variables.get("direction").values.get(0);
                System.out.println("  ✔ setting direction: " + pathConstruct.sourcePortConstruct.variables.get("direction").value);
            }

            if (pathConfiguration.get("source-port").variables.get("voltage").values.size() == 1) {
                if (pathConstruct.sourcePortConstruct.variables.containsKey("voltage")
                        && pathConstruct.sourcePortConstruct.variables.get("voltage") == null) {
                    pathConstruct.sourcePortConstruct.variables.put("voltage", new Variable("voltage"));
                }
                pathConstruct.sourcePortConstruct.variables.get("voltage").value = pathConfiguration.get("source-port").variables.get("voltage").values.get(0);
                System.out.println("  ✔ setting voltages: " + pathConstruct.sourcePortConstruct.variables.get("voltage").value);
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

                    PortConstruct sourcePort = (PortConstruct) Manager.get(variableValue);
                    pathConstruct.sourcePortConstruct = sourcePort;

//                    System.out.println(">>> set source-port " + variableValue);

                } else if (variableTitle.equals("target-port")) {

                    PortConstruct targetPort = (PortConstruct) Manager.get(variableValue);
                    pathConstruct.targetPortConstruct = targetPort;

//                    System.out.println(">>> set target-port " + variableValue);

                }

            } else if (workspace.construct.getClass() == TaskConstruct.class) {

                TaskConstruct taskConstruct = (TaskConstruct) workspace.construct;

                if (variableTitle.equals("script")) {

                    ScriptConstruct scriptConstruct = new ScriptConstruct();
                    scriptConstruct.text = variableValue;

                    taskConstruct.scriptConstruct = scriptConstruct;

//                    System.out.println(">>> set script " + variableValue);

                }

            }

//            else if (assignmentString.equals("host")) {
//
//                // TODO:
//
//            } else if (assignmentString.equals("device")) {
//
//            }

            System.out.println("✔ set script " + variableTitle + ":" + variableValue);

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

    public void exitTask() {
        System.exit(0);
    }
    // </REFACTOR>

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

    /*
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

    public void listPathsTask() {

        if (workspace.projectConstruct != null) {

            for (int i = 0; i < workspace.projectConstruct.pathConstructs.size(); i++) {
                System.out.println("" + workspace.projectConstruct.pathConstructs.get(i).uid + " (port " + workspace.projectConstruct.pathConstructs.get(i).sourcePortConstruct.uid + ", port " + workspace.projectConstruct.pathConstructs.get(i).targetPortConstruct.uid + ")");
            }

        }

    }
    */

}


// TODO: PORTS CAN BE "WIRELESS" AND SPREAD OUT ACROSS BOARDS JUST LIKE CLAY BOARDS CAN BE SPREAD OUT IN SPACE.
