package camp.computer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

import camp.computer.util.Serialize;

public class Task {

    public String id;

    public String rev;

    // <INSTANCE>
    public String instance_id = null; // UUID.randomUUID().toString();
    // </INSTANCE>

    public String type;

    public Script script;

    public Task() {
        this.type = "task";
    }

    public static ObjectNode serialize(Task task, Serialize.Policy serializePolicy) {

        ObjectNode taskNode = JsonNodeFactory.instance.objectNode();

        taskNode.put("_id", task.id);
        taskNode.put("_rev", task.rev);

        taskNode.put("type", task.type);

        if (serializePolicy == Serialize.Policy.INSTANCE) {
            taskNode.put("instance_id", task.instance_id);
        }

//        if (serializePolicy == Serialize.Policy.TEMPLATE
//                || serializePolicy == Serialize.Policy.INSTANCE) {
//        }

        taskNode.set("script", Script.serialize(task.script, serializePolicy));

//        if (serializePolicy == Serialize.Policy.STATE) {
//            ArrayNode tasksNode = taskNode.putArray("tasks");
//            for (int i = 0; i < task.tasks.size(); i++) {
//                tasksNode.add(Task.serialize(task.tasks.get(i), serializePolicy));
//            }
//        }

        return taskNode;

    }

}

