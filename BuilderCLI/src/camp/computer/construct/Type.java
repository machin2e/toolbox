package camp.computer.construct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import camp.computer.workspace.Manager;

public class Type extends Identifier {

    // TODO: Store Type identifiers in Manager_v1
    // TODO: Consider using static Type interface to wrap interface to Manager_v1 for Type-specific operations
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
            long uid = Manager.add(type);
            return type;
        }
    }

    public static boolean has(String identifier) {
        return Type.identifiers.containsKey(identifier);
    }

    public static List<Type> get() {
        return new ArrayList<>(Type.identifiers.values());
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
