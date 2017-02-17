package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@code VariableValueSet} stores a list of tag values for a specified tag identified by its unique label.
 */
public class Feature {

    // tag/key: string
    // type: string, list, OLD_construct-name
    // domain: list of accepted tokens; (or) for lists, stores list of values that can be stored in the list

    /**
     * {@code tag} is a {@code String} that uniquely identifies the {@code Variable} in the
     * containing {@code VariableMap}. <em>in the namespace</em>.
     */
    public String tag = null; // e.g., mode; direction; voltage

    // Content Type (e.g., none, any, text, list, etc.)
    public Type type = null;
//    public List<String> type = new ArrayList<>(); // if size == 0, then unconstrained!

    // Content Domain (contains Construct Types and Construct Content)
    public List<String> domain = new ArrayList<>(); // if size == 0, then unconstrained!

    // Only used for listType
    public Type listType = null;

    public Feature(String tag) {
        this.tag = tag;
    }

}
