package camp.computer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

import camp.computer.util.Serialize;

public class Controller {

    public String id;

    public String rev;

    // <INSTANCE>
    public String instance_id = null; // UUID.randomUUID().toString();
    // </INSTANCE>

    public String type;

    public List<Task> tasks;

    public Controller() {
        tasks = new ArrayList<>();
    }

    public static ObjectNode serialize(Controller controller, Serialize.Policy serializationPolicy) {

        ObjectNode controllerNode = JsonNodeFactory.instance.objectNode();

        controllerNode.put("_id", controller.id);
        controllerNode.put("_rev", controller.rev);

        controllerNode.put("type", controller.type);

        if (serializationPolicy == Serialize.Policy.INSTANCE) {
            controllerNode.put("instance_id", controller.instance_id);
        }

//        if (serializationPolicy == Serialize.Policy.TEMPLATE
//                || serializationPolicy == Serialize.Policy.INSTANCE) {
//
//            ArrayNode portsNode = controllerNode.putArray("ports");
//            for (int i = 0; i < controller.ports.size(); i++) {
//                portsNode.add(Port.serialize(controller.ports.get(i), serializationPolicy));
//            }
//
//        }

//        if (serializationPolicy == Serialize.Policy.STATE) {

        ArrayNode tasksNode = controllerNode.putArray("tasks");
        for (int i = 0; i < controller.tasks.size(); i++) {
            tasksNode.add(Task.serialize(controller.tasks.get(i), serializationPolicy));
        }

//        }

        return controllerNode;

    }
}

