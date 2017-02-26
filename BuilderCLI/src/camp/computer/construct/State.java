package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

import camp.computer.Application;
import camp.computer.workspace.Manager;

public class State extends Identifier {

    // TODO: Store in Manager and Database as indexable item and use Factory-style getPersistentState(Type types, String object) function to retrieve

    public Type type = null;

    // Bytes storing actual object and object types
    public Class objectType = null; // null for "none", List for "list", String for "text", Construct for non-primitive types
    public Object object = null;

    private State(Type type) {
        if (type == Type.get("none")) {
            this.type = type;
            this.objectType = null;
            this.object = null;
        } else if (type == Type.get("text")) {
            this.type = type;
            this.objectType = String.class;
            this.object = null;
        } else if (type == Type.get("number")) {
            // TODO:
        } else if (type == Type.get("list")) {
            this.type = type;
            this.objectType = List.class;
            this.object = new ArrayList<>();
        } else { // Custom construct
            this.type = type;
            this.objectType = Construct.class;
            this.object = null;
        }
    }

    // Returns reference to the "base" state.
    public static State getState(Type type) {
        // TODO: Store in Manager and Database as indexable item and use Factory-style getPersistentState(Type types, String object) function to retrieve


        // TODO: Check if Manager contains the State already and if so, retrieve it and return a reference to it
        State state = new State(type);
        long uid = Manager.add(state);
        return state;
    }

    // Encapsulate state
    // TODO: Query database for the state corresponding to stateExpression.
    // e.g.,
    // none
    // 3 --or-- 3.4 --or-- 341
    // text(id:23) --or-- 'foo' --or-- text('foo')
    // list(id:44) --or-- port, path, project --or-- 'foo', text('bar'), port(uid:3) --or-- port, path, project
    // port(uid:3)
    public static State getState(String expression) {
        Type stateType = Type.get(expression);
        if (stateType != null) {

            if (stateType == Type.get("none")) {
                State state = Manager.getPersistentState(expression);
                if (state == null) {
                    // State wasn't found, so create a new one and return it
                    // TODO: Store in the database
                    state = State.getState(stateType);
                }
                return state;
            } else if (stateType == Type.get("text")) {

                State state = Manager.getPersistentState(expression);
                if (state == null) {
                    // State wasn't found, so create a new one and return it
                    // TODO: Store in the database
                    state = State.getState(stateType);
                    state.object = expression.substring(1, expression.length() - 1);
                }
                return state;

            } else if (stateType == Type.get("list")) {

            } else {

                State state = Manager.getPersistentState(expression);
                if (state == null) {

                    // Create new State
                    // TODO: Add new state to persistent store

                    String typeIdentifierToken = expression.substring(0, expression.indexOf("(")).trim(); // text before '('
                    String addressTypeToken = expression.substring(expression.indexOf("(") + 1, expression.indexOf(":")).trim(); // text between '(' and ':'
                    String addressToken = expression.substring(expression.indexOf(":") + 1, expression.indexOf(")")).trim(); // text between ':' and ')'

                    long uid = Long.parseLong(addressToken.trim());
                    Identifier identifier = Manager.get(uid);
                    if (identifier != null) {
                        state = State.getState(stateType);
                        state.object = identifier;
                        return state;
                    } else {
                        System.out.println(Error.get("Error: " + expression + " does not exist."));
                    }
                }
                return state;

            }

        }
        return null;
    }

    @Override
    public String toString() {
        if (type != null) {
            if (type == Type.get("none")) {
                // TODO: Print from "none" construct
                return "none (id: " + this.uid + ")";
            } else if (type == Type.get("text")) {
                // TODO: Print from "text" construct
//                return "'" + (String) this.object + "' (id: " + this.uid + ")";
                return Application.ANSI_YELLOW + "'" + this.object + "'" + Application.ANSI_RESET + " (id: " + this.uid + ")";
                // return types + "('" + (String) this.object + "')";
            } else if (type == Type.get("number")) {
                // TODO:
                // TODO: Print from "number" construct
                // return types + "('" + (String) this.object + "')";
            } else if (type == Type.get("list")) {
//                return "<TODO:LIST>" + "' (id: " + this.uid + ")";
                List list = (List) this.object;
                String listString = "list (id: " + this.uid + ")";
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        listString += " : ";
                    }
                    listString += ((State) list.get(i));
                    if ((i + 1) < list.size()) {
                        listString += ", ";
                    }
                }
                return listString;
            } else {
                return type + "(id:" + ((Construct) object).uid + ")";
            }
        } else {
            System.out.println(Error.get("Error: State is in corrupt (types is null)."));
        }
        return null;
    }
}
