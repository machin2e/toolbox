package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

public class State {

    // TODO: Store in Manager and Database as indexable item and use Factory-style getState(...)

    public Type type = null;

    // Bytes storing actual content and content type
    public Object content = null;
    public Class contentType = null;

    public State(Type type) {
        if (type == Type.get("none")) {
            this.type = type;
            this.contentType = null;
            this.content = null;
        } else if (type == Type.get("list")) {
            this.type = type;
            this.contentType = List.class;
            this.content = new ArrayList<>();
//            for (int i = 0; i < feature.listTypes.size(); i++) {
//                this.listTypes.add(feature.listTypes.get(i));
//            }
        } else if (type == Type.get("text")) {
            this.type = type;
            this.contentType = String.class;
            this.content = null;
        } else { // Custom construct
            this.type = type;
            this.contentType = Construct.class;
            this.content = null;
        }
    }
}
