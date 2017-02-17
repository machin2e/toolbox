package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

public class Content {

    // Content Type
//    public Type type = null;
    public List<String> type = new ArrayList<>(); // if size == 0, then unconstrained!

    /**
     * {@code tag} is a {@code String} that uniquely identifies the {@code Variable} in the
     * containing {@code VariableMap}. <em>in the namespace</em>.
     */
    public String tag = null; // e.g., mode; direction; voltage

    // Content Domain
    public List<String> domain = new ArrayList<>(); // if size == 0, then unconstrained!

    // Only used for listType
    public Type listType = null;

    public Object content = null;
    public Class contentType = null;

    public Content(Feature feature) {

        this.tag = feature.tag;

        this.type = feature.type;

        this.domain.addAll(feature.domain);

        // none
        // any
        // number
        if (this.type == Type.get("list")) {
            this.content = new ArrayList<>();
            this.contentType = List.class;

            this.listType = feature.listType;
        } else if (this.type == Type.get("text")) {
            this.content = null;
            this.contentType = String.class;
        } else { // Custom construct
            this.content = null;
            this.contentType = Instance.class;
        }

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
