package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

import camp.computer.workspace.Manager;

public class State extends Identifier {

    // TODO: Store in Manager and Database as indexable item and use Factory-style getState(Type type, String objectInstance) function to retrieve

    public Type type = null;

    // Bytes storing actual objectInstance and objectInstance type
    public Object objectInstance = null;
    public Class classType = null; // null for "none", List for "list", String for "text", Construct for non-primitive type

    private State(Type type) {
        if (type == Type.get("none")) {
            this.type = type;
            this.classType = null;
            this.objectInstance = null;
        } else if (type == Type.get("text")) {
            this.type = type;
            this.classType = String.class;
            this.objectInstance = null;
        } else if (type == Type.get("number")) {
            // TODO:
        } else if (type == Type.get("list")) {
            this.type = type;
            this.classType = List.class;
            this.objectInstance = new ArrayList<>();
        } else { // Custom construct
            this.type = type;
            this.classType = Construct.class;
            this.objectInstance = null;
        }
    }

    // Returns reference to the "base" state.
    public static State getState(Type type) {
        // TODO: Store in Manager and Database as indexable item and use Factory-style getState(Type type, String objectInstance) function to retrieve
        State state = new State(type);
        long uid = Manager.add(state);
        return state;
    }
}
