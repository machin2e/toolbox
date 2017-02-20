package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

public class Content {

    // Content Type
    public List<Type> type = new ArrayList<>(); // if size == 0, then unconstrained!

    /**
     * {@code identifier} is a {@code String} that uniquely identifies the {@code Variable} in the
     * containing {@code VariableMap}. <em>in the namespace</em>.
     */
    public String identifier = null; // e.g., mode; direction; voltage

    // Content Domain
    public List<String> domain = null; // if size == 0, then unconstrained!
    // TODO: List<State>?

    // Only used for listType
    public List<Type> listTypes = new ArrayList<>(); // if size == 0, then unconstrained!

    // Bytes storing actual content and content type
    public State state = null;

    public Content(Feature feature) {

        this.identifier = feature.identifier;

        this.type.addAll(feature.type);

        if (feature.domain != null) {
            this.domain = new ArrayList<>();
            this.domain.addAll(feature.domain);
        }

        if (feature.type.contains(Type.get("list"))) {
            this.listTypes.addAll(feature.listTypes);
        }

        // Allocate storage for content if there's only one available type for the feature
        if (this.type.size() == 1) {
            // Set content type and, if list, the list content type.
            // none
            // any
            // number
            this.state = new State(this.type.get(0));
//            if (this.type.get(0) == Type.get("none")) {
////                this.contentType = null;
////                this.content = null;
//                this.state = new State(Type.get("none"));
//            } else if (this.type.get(0) == Type.get("list")) {
////                this.contentType = List.class;
////                this.content = new ArrayList<>();
//                this.state = new State(Type.get("list"));
//                for (int i = 0; i < feature.listTypes.size(); i++) {
//                    this.listTypes.add(feature.listTypes.get(i));
//                }
//            } else if (this.type.get(0) == Type.get("text")) {
////                this.contentType = String.class;
////                this.content = null;
//                this.state = new State(Type.get("text"));
//            } else { // Custom construct
////                this.contentType = Construct.class;
////                this.content = null;
//                this.state = new State(Construct.class, null);
//            }
        }

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
