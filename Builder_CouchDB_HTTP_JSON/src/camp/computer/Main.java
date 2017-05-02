package camp.computer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

import camp.computer.model.Device;
import camp.computer.model.Interface;
import camp.computer.model.Port;
import camp.computer.model.Project;
import camp.computer.model.Script;
import camp.computer.model.Task;
import camp.computer.util.JSON;
import camp.computer.util.List;
import camp.computer.util.Serialize;

public class Main {

    public static void main(String[] args) {

//        try {
//
//            Port port = null;
//
//            Project project = new Project();
////            Interface iface = new Interface();
////            iface.controller = new Controller();
//            Task task = new Task();
//            task.script = new Script();
////            iface.controller.tasks.add(task);
////            project.interfaces.add(iface);
//            Device irRangefinderDevice = Device.create();
//            port = Port.create(new List("power"), new List("input"), new List("cmos"));
//            port.set("power", "input", "cmos");
//            irRangefinderDevice.ports.add(port);
//            port = Port.create(new List("digital", "analog"), new List("input", "output"), new List("cmos", "ttl"));
//            port.set("digital", "output", "cmos");
//            irRangefinderDevice.ports.add(port);
//            port = Port.create(new List("digital", "analog"), new List("input", "output"), new List("cmos", "ttl"));
//            port.set("analog", "input", "cmos");
//            irRangefinderDevice.ports.add(port);
//            Device servoDevice = Device.create();
//            servoDevice.ports.add(Port.create());
//            servoDevice.ports.add(Port.create());
//            servoDevice.ports.add(Port.create());
////            project.devices.add(device1);
//
//            Interface iface = project.addInterface(irRangefinderDevice, servoDevice);
//            iface.addChannel(irRangefinderDevice.ports.get(0), servoDevice.ports.get(1));
//            iface.addChannel(irRangefinderDevice.ports.get(1), servoDevice.ports.get(2));
//            iface.addChannel(irRangefinderDevice.ports.get(2), servoDevice.ports.get(0));
////            project.addInterface(servoDevice, irRangefinderDevice);
////            iface.addChannel(irRangefinderDevice.ports.get(0), servoDevice.ports.get(1));
////            iface.addChannel(irRangefinderDevice.ports.get(1), servoDevice.ports.get(2));
////            iface.addChannel(irRangefinderDevice.ports.get(2), servoDevice.ports.get(0));
////            System.out.println("interface #: " + project.interfaces.size());
////            System.out.println("channel #: " + iface.channels.size());
//
////            ObjectMapper mapper = new ObjectMapper();
//            //By default all fields without explicit view definition are included, disable this
////            mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);
////            mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
////            String json = mapper
////                    .writerWithView(Views.StateOnly.class)
////                    .writeValueAsString(project);
//
////            String json = new ObjectMapper().writeValueAsString(device);
//            String json = new ObjectMapper().writeValueAsString(project);
//            Object jsonObject = new ObjectMapper().readValue(json, Object.class);
//            String prettyString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
////            System.out.println(prettyString);
////            System.out.println(json);
//
//
////            Project p = Project.deserialize(json);
//
//            //---------------------
//            // MANUAL SERIALIZATION
//            //---------------------
//
//            /*
//            Device testDevice = Device.create();
//            Port testPort = Port.create(new List("power"), new List("input"), new List("cmos"));
//            testPort.set("power", "input", "cmos");
//            testDevice.ports.add(testPort);
//
//            ObjectNode testPortJsonObject = Port.serialize(testPort, Serialize.Policy.TEMPLATE);
//            if (testPortJsonObject != null) {
//                System.out.println(JSON.toPrettyString(testPortJsonObject.toString()));
//            }
//
//            ObjectNode testDeviceJsonObject = Device.serialize(testDevice, Serialize.Policy.TEMPLATE);
//            if (testDeviceJsonObject != null) {
//                System.out.println(JSON.toPrettyString(testDeviceJsonObject.toString()));
//            }
//
//            testPortJsonObject = Port.serialize(testPort, Serialize.Policy.STATE);
//            if (testPortJsonObject != null) {
//                System.out.println(JSON.toPrettyString(testPortJsonObject.toString()));
//            }
//
//            testDeviceJsonObject = Device.serialize(testDevice, Serialize.Policy.STATE);
//            if (testDeviceJsonObject != null) {
//                System.out.println(JSON.toPrettyString(testDeviceJsonObject.toString()));
//            }
//            */
//
//
//
//            /*
//            ObjectNode innerNode = JsonNodeFactory.instance.objectNode();
//            innerNode.put("type", "innerFoo");
//
//            ObjectNode node = JsonNodeFactory.instance.objectNode();
//            node.put("type", "foo");
//            node.set("inner", innerNode);
//
//            System.out.println(node.toString());
//
//            System.out.println(innerNode.toString());
//            */
//
////            json = new ObjectMapper().writeValueAsString(node);
////            jsonObject = new ObjectMapper().readValue(json, Object.class);
////            prettyString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
////            System.out.println(prettyString);
//
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Application application = new Application();
        application.start();
//        application.blah();

    }
}
