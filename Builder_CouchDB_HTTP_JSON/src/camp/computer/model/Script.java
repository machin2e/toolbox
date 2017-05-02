package camp.computer.model;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import camp.computer.util.CouchDB;
import camp.computer.util.Serialize;

public class Script {

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

    public static Script generateRandom() {

        Script script = new Script();
        script.id = CouchDB.generateUuid();

        script.code = "javascript:function(data) { /* TODO */ }";

        return script;

    }

}
