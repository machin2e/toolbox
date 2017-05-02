package camp.computer.model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Random;

import camp.computer.util.CouchDB;
import camp.computer.util.List;
import camp.computer.util.Serialize;

public class Project {

    public String id;

    public String rev;

    public String type;

    public List<Device> devices;

    public List<Interface> interfaces;

    public Project() {
        type = "project";
        devices = new List<>();
        interfaces = new List<>();
    }

    public Interface getInterface(Device source, Device target) {
        for (int i = 0; i < interfaces.size(); i++) {
            if (interfaces.get(i).source == source && interfaces.get(i).target == target) {
                return interfaces.get(i);
            }
        }
        return null;
    }

    public Interface addInterface(Device source, Device target) {
        if (source == target) {
            return null;
        }

        if (!this.devices.contains(source)) {
            this.devices.add(source);
        }
        if (!this.devices.contains(target)) {
            this.devices.add(target);
        }

        Interface iface = getInterface(source, target);
        if (iface == null) {
            iface = new Interface(source, target);
            interfaces.add(iface);
        }
        return iface;
    }

//    // TODO: Delete!
//    public static Project deserialize(String json) {
//
//        ObjectMapper mapper = new ObjectMapper();
//        SimpleModule module = new SimpleModule();
//        module.addDeserializer(Project.class, new ProjectDeserializer());
//        mapper.registerModule(module);
//
//        Project project = null;
//        try {
//            project = mapper.readValue(json, Project.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return project;
//
//    }

    public static ObjectNode serialize(Project project, Serialize.Policy serializationPolicy) {

        if (serializationPolicy == Serialize.Policy.TEMPLATE) {


        } else if (serializationPolicy == Serialize.Policy.INSTANCE) {

            // TODO:

        } else if (serializationPolicy == Serialize.Policy.STATE) {

            ObjectNode projectNode = JsonNodeFactory.instance.objectNode();

            projectNode.put("_id", project.id);

//            if (projectNode != null) {
//                projectNode.put("_rev", project.rev);
//            }

            projectNode.put("type", project.type);

            ObjectNode assetsNode = projectNode.putObject("assets");

            ArrayNode devicesNode = assetsNode.putArray("devices");
            for (int i = 0; i < project.devices.size(); i++) {
                devicesNode.add(Device.serialize(project.devices.get(i), Serialize.Policy.INSTANCE));
            }

            ArrayNode interfacesNode = projectNode.putArray("interfaces");
            for (int i = 0; i < project.interfaces.size(); i++) {
                interfacesNode.add(Interface.serialize(project.interfaces.get(i), Serialize.Policy.STATE));
            }

            return projectNode;

        }

        return null;

    }

    public static Project generateRandom() {

        Port port = null;

        Project project = new Project();
        project.id = CouchDB.generateUuid();

        Task task = new Task();
        task.script = new Script();
//            iface.controller.tasks.add(task);
//            project.interfaces.add(iface);
//        Device irRangefinderDevice = Device.create();
//        port = Port.create(new List("power"), new List("input"), new List("cmos"));
//        port.set("power", "input", "cmos");
//        irRangefinderDevice.ports.add(port);
//        port = Port.create(new List("digital", "analog"), new List("input", "output"), new List("cmos", "ttl"));
//        port.set("digital", "output", "cmos");
//        irRangefinderDevice.ports.add(port);
//        port = Port.create(new List("digital", "analog"), new List("input", "output"), new List("cmos", "ttl"));
//        port.set("analog", "input", "cmos");
//        irRangefinderDevice.ports.add(port);
//        Device servoDevice = Device.create();
//        servoDevice.ports.add(Port.create());
//        servoDevice.ports.add(Port.create());
//        servoDevice.ports.add(Port.create());
////            project.devices.add(device1);

        Random random = new Random();

        int deviceCount = 2 + random.nextInt(4);
        for (int i = 0; i < deviceCount; i++) {
            project.devices.add(Device.generateRandom());
        }

        int interfaceCount = 1;
        for (int i = 0; i < interfaceCount; i++) {

            int sourceDeviceIndex = 0;
            int targetDeviceIndex = 1;
            Device sourceDevice = project.devices.get(sourceDeviceIndex);
            Device targetDevice = project.devices.get(targetDeviceIndex);
            Interface iface = project.addInterface(sourceDevice, targetDevice);

            int channelCount = random.nextInt(Math.min(sourceDevice.ports.size(), targetDevice.ports.size()));
            if (channelCount == 0) {
                channelCount = 1;
            }
            for (int j = 0; j < channelCount; j++) {
                int sourcePortIndex = random.nextInt(sourceDevice.ports.size());
                int targetPortIndex = random.nextInt(targetDevice.ports.size());
                iface.addChannel(sourceDevice.ports.get(sourcePortIndex), targetDevice.ports.get(targetPortIndex));
            }

            iface.controller = new Controller();
        }

//        Interface iface = project.addInterface(irRangefinderDevice, servoDevice);
//        iface.addChannel(irRangefinderDevice.ports.get(0), servoDevice.ports.get(1));
//        iface.addChannel(irRangefinderDevice.ports.get(1), servoDevice.ports.get(2));
//        iface.addChannel(irRangefinderDevice.ports.get(2), servoDevice.ports.get(0));
//            project.addInterface(servoDevice, irRangefinderDevice);
//            iface.addChannel(irRangefinderDevice.ports.get(0), servoDevice.ports.get(1));
//            iface.addChannel(irRangefinderDevice.ports.get(1), servoDevice.ports.get(2));
//            iface.addChannel(irRangefinderDevice.ports.get(2), servoDevice.ports.get(0));
//            System.out.println("interface #: " + project.interfaces.size());
//            System.out.println("channel #: " + iface.channels.size());

//            iface.controller

        return project;

    }
}

