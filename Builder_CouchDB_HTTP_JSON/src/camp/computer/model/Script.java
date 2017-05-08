package camp.computer.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

import camp.computer.util.CouchDB;
import camp.computer.util.Serialize;

public class Script extends Entity {

    public String id;

    public String rev;

    // <INSTANCE>
    public String instance_id = null; // UUID.randomUUID().toString();
    // </INSTANCE>

    public String type;

    public String code;

    private Script() {
        this.type = "script";
    }

    public static Script create() {
        Script script = new Script();
        return script;
    }

    public static ObjectNode serialize(Script script, Serialize.Policy serializePolicy) {

        ObjectNode scriptNode = JsonNodeFactory.instance.objectNode();

        scriptNode.put("_id", script.id);
        scriptNode.put("_rev", script.rev);

        scriptNode.put("type", script.type);

        if (serializePolicy == Serialize.Policy.INSTANCE) {
            scriptNode.put("instance_id", script.instance_id);
        }

        scriptNode.put("code", script.code);

        return scriptNode;

    }

    public static Script deserialize(String json) {

        try {

//            Interface iface = Interface.create();
            Script script = new Script();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json.toString());

            if (rootNode.has("_id")) {
                script.id = rootNode.path("_id").asText();
            }

            if (rootNode.has("type")) {
                script.type = rootNode.path("type").asText();
            }

            if (rootNode.has("instance_id")) {
                script.instance_id = rootNode.path("instance_id").asText();
            }

            if (rootNode.has("code")) {
                script.code = rootNode.path("code").asText();
            }

            return script;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static Script generateRandom() {

        Script script = new Script();
        script.id = CouchDB.generateUuid();

        script.code = "javascript:function(data) { /* TODO */ }";

        return script;

    }

}
