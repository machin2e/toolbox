package camp.computer.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import camp.computer.util.CouchDB;
import camp.computer.util.List;
import camp.computer.util.Serialize;

public class Project extends Entity {

    // <COUCHDB>
    public String id;
    public String rev;
    // </COUCHDB>

    public String type;

    public List<Device> devices;

    public List<Interface> interfaces;

    private Project() {
        type = "project";
        devices = new List<>();
        interfaces = new List<>();
    }

    public static Project create() {
        Project project = new Project();
        return project;
    }

    public Interface getInterface(Device source, Device target) {
        for (int i = 0; i < interfaces.size(); i++) {
            if ((interfaces.get(i).source == source && interfaces.get(i).target == target)
                    || (interfaces.get(i).source == target && interfaces.get(i).target == source)) {
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

    public static Project deserialize(String json) {

        try {

            Project project = Project.create();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json.toString());

            if (rootNode.has("_id")) {
                project.id = rootNode.path("_id").asText();
            }

            if (rootNode.has("type")) {
                project.type = rootNode.path("type").asText();
            }

//            if (rootNode.has("instance_id")) {
//                project.instance_id = rootNode.path("instance_id").asText();
//            }

            if (rootNode.has("assets")) {
                JsonNode assetsNode = rootNode.path("assets");
                if (assetsNode.has("devices")) {
                    JsonNode devicesNode = assetsNode.path("devices");
                    for (Iterator<JsonNode> it = devicesNode.elements(); it.hasNext(); ) {
                        JsonNode deviceNode = it.next();
                        Device device = Device.deserialize(deviceNode.toString());
                        project.devices.add(device);
                    }
                }
            }

            if (rootNode.has("interfaces")) {
                JsonNode interfacesNode = rootNode.path("interfaces");
                for (Iterator<JsonNode> it = interfacesNode.elements(); it.hasNext(); ) {
                    JsonNode interfaceNode = it.next();
                    Interface iface = Interface.deserialize(interfaceNode.toString());
                    project.interfaces.add(iface);
                }
            }

            return project;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static Project generateRandom() {

        Random random = new Random();

        Project project = Project.create();
        project.id = CouchDB.generateUuid();

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


            Task task = Task.create();
            task.script = Script.create();

            iface.controller = Controller.generateRandom();
        }

        return project;

    }
}

