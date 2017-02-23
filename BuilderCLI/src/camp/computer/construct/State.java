package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

import camp.computer.workspace.Manager;

public class State extends Identifier {

    // TODO: Store in Manager and Database as indexable item and use Factory-style getPersistentState(Type type, String objectInstance) function to retrieve

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
        // TODO: Store in Manager and Database as indexable item and use Factory-style getPersistentState(Type type, String objectInstance) function to retrieve


        // TODO: Check if Manager contains the State already and if so, retrieve it and return a reference to it
        State state = new State(type);
        long uid = Manager.add(state);
        return state;
    }

    // Encapsulate state
    // TODO: Query database for the state corresponding to stateExpression.
    public static State getState(String expression) {
        Type stateType = Type.get(expression);
        if (stateType != null) {

            if (stateType == Type.get("none")) {
                State state = State.getState(stateType);
                return state;
            } else if (stateType == Type.get("text")) {

                State state = Manager.getPersistentState(expression);
                if (state == null) {
                    // State wasn't found, so create a new one and return it
                    // TODO: Store in the database
                    state = State.getState(stateType);
                    state.objectInstance = expression.substring(1, expression.length() - 1);
                }
                return state;

            } else if (stateType == Type.get("list")) {

            } else {

//                if (State.isConstructExpression(expression)) {
//
//                    String typeIdentifierToken = expression.substring(0, expression.indexOf("(")).trim(); // text before '('
//                    String addressTypeToken = expression.substring(expression.indexOf("(") + 1, expression.indexOf(":")).trim(); // text between '(' and ':'
//                    String addressToken = expression.substring(expression.indexOf(":") + 1, expression.indexOf(")")).trim(); // text between ':' and ')'
//
//                    long uid = Long.parseLong(addressToken.trim());
//
//                    Identifier identifier = Manager.get(uid);
//                    if (identifier != null) {
//                        if (identifier.getClass() == Construct.class) {
//                            State state = State.getState(stateType);
//                            state.objectInstance = identifier;
//                            return state;
//                        }
//                    } else {
//                        System.out.println(Error.get("Error: " + expression + " does not exist."));
//                    }
//
//                }

                State state = Manager.getPersistentState(expression);
                if (state != null) {
                    return state;
                } else {

                    // Create new State
                    // TODO: Add new state to persistent store

                    String typeIdentifierToken = expression.substring(0, expression.indexOf("(")).trim(); // text before '('
                    String addressTypeToken = expression.substring(expression.indexOf("(") + 1, expression.indexOf(":")).trim(); // text between '(' and ':'
                    String addressToken = expression.substring(expression.indexOf(":") + 1, expression.indexOf(")")).trim(); // text between ':' and ')'

                    long uid = Long.parseLong(addressToken.trim());
                    Identifier identifier = Manager.get(uid);
                    if (identifier != null) {
                        state = State.getState(stateType);
                        state.objectInstance = identifier;
                        return state;
                    } else {
                        System.out.println(Error.get("Error: " + expression + " does not exist."));
                    }
                }

            }

        }
        return null;
    }

    public static boolean isConstructExpression(String expression) {
        return expression.matches("([a-z]+)[ ]*\\([ ]*(id|uid|uuid)[ ]*:[ ]*[0-9]+[ ]*\\)");
    }

    @Override
    public String toString() {
        if (type != null) {
            if (type == Type.get("none")) {
                // TODO: Print from "none" construct
                return "none";
            } else if (type == Type.get("text")) {
                // TODO: Print from "text" construct
                return "'" + (String) this.objectInstance + "' (id: " + this.uid + ")";
                // return type + "('" + (String) this.objectInstance + "')";
            } else if (type == Type.get("number")) {
                // TODO:
                // TODO: Print from "number" construct
                // return type + "('" + (String) this.objectInstance + "')";
            } else if (type == Type.get("list")) {
                return "<TODO:LIST>";
            } else {
                return type + "(id:" + ((Construct) objectInstance).uid + ")";
            }
        } else {
            System.out.println(Error.get("Error: State is in corrupt (type is null)."));
        }
        return null;
    }
}
