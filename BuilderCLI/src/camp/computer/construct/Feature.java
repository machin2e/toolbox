package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@code VariableValueSet} stores a list of identifier values for a specified identifier identified by its unique label.
 */
public class Feature extends Identifier {

    // identifier/key: string
    // type: string, list, construct-name
    // domain: list of accepted tokens; (or) for lists, stores list of values that can be stored in the list

    /**
     * {@code identifier} is a {@code String} that uniquely identifies the {@code Variable} in the
     * containing {@code VariableMap}. <em>in the namespace</em>.
     */
    public String identifier = null; // e.g., mode; direction; voltage

    // Content Type (e.g., none, any, text, list, etc.)
    public List<Type> type = new ArrayList<>(); // if size == 0, then unconstrained!

    // Content Domain (contains Identifier Types and Identifier Content)
    // NOTE: This only ever contains "text objectInstance" or references to specific constructs
    public List<String> domain; // if size == 0, then 'none'! if null, then 'any'!
    // TODO: Create a separate feature domain for each type in featureType

    /**
     * Only used for <em>list</em> {@code Type}.
     *
     * {@code listTypes} is {@code null} when <em>any</em> type is acceptable. {@code listTypes} is
     * an empty list when the list's type is <em>none</em>, meaning nothing can be added to the
     * list.
     */
    public List<Type> listTypes = null; // if size == 0, then unconstrained!

    public Feature(String identifier) {
        this.identifier = identifier;
    }

    // 'text'
    // text('text')
    // port(uid:<uid>)
    // port(uuid:<uid>)
    // device(uid:<uid>)
    // device(uuid:<uid>)
    public static Type getType(String content) {

        if (content.startsWith("'") && content.endsWith("'")) {
            return Type.get("text");
        } else if (content.contains("(") && content.contains(")")) {
            String typeTag = content.substring(0, content.indexOf("("));
            if (Type.has(typeTag)) {
                // TODO: Check if specified construct exists
                return Type.get(typeTag);
            }
        }

        return null;
    }

    public static boolean isText(String featureContent) {
        if (!featureContent.startsWith("'") || !featureContent.endsWith("'")) {
            return false;
        }
        return true;
    }

    public static boolean isConstruct(String featureContent) {
        // TODO:
        return false;
    }

}
