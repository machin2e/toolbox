package camp.computer.construct_v2;

import java.util.HashMap;

public class Type {

    // TODO: Store Type identifiers in Manager
    // TODO: Consider using static Type interface to wrap interface to Manager for Type-specific operations
    private static HashMap<String, Type> identifiers = new HashMap<>();

    public String identifier = null; // type identifier of construct

    private Type(String identifier) {
        this.identifier = identifier;
    }

    public static Type add(String identifier) {
        if (Type.identifiers.containsKey(identifier)) {
            return Type.identifiers.get(identifier);
        } else {
            Type type = new Type(identifier);
            Type.identifiers.put(identifier, type);
            return type;
        }
    }

    public static boolean has(String identifier) {
        return Type.identifiers.containsKey(identifier);
    }

    public static Type get(String identifier) {
        if (Type.identifiers.containsKey(identifier)) {
            return Type.identifiers.get(identifier);
        }
        return null;
    }

    @Override
    public String toString() {
        return identifier;
    }

}
