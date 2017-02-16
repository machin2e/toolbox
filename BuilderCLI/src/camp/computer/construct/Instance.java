package camp.computer.construct;

import java.util.HashMap;
import java.util.List;

import camp.computer.workspace.Manager;

public class Instance extends Construct {

    // Identity -> Feature : Instance -> Content

//    public UUID uuid = UUID.randomUUID(); // universal identifier (unique among all in central repo)

//    public long uid = Manager_v1.elementCounter++; // manager/cache UID

    public Type type = null;

//    public String tag = null; // label/tag(s)

    // string => String
    // list => ArrayList<?>
    // list of type Type => ArrayList<Type>
    public HashMap<String, Content> contents = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?

    // TODO: configuration(s) : assign state to multiple features <-- do this for _Container_ not Identity


    private Instance(Identity identity) {

        this.type = identity.type;

        // Create Content for each Feature
        for (Feature feature : identity.features.values()) {
            Content content = new Content(feature);
            contents.put(content.tag, content);
        }

    }

    public static Instance add(Type type) {
        if (Identity.has(type)) {
            Identity identity = Identity.get(type);
            Instance instance = new Instance(identity);
            long uid = Manager.add(instance);
            return instance;
        }
        return null;
    }

    public void set(String tag, Object content) {
        if (contents.containsKey(tag)) {
            if (contents.get(tag).type == Type.get("none")) {
                // TODO: Can't assign anything to the feature content
            } else if (contents.get(tag).type == Type.get("any")) {
                // TODO: Verify that this is correct!
                contents.get(tag).content = content;
            } else if (contents.get(tag).type == Type.get("list")) {
                List contentList = (List) contents.get(tag).content;
                // TODO: Check type of list contents and restrict to the type (or any if "any")

                // TODO: If specific text tokens are allowed AS WELL AS text construct, text construct subsumes the tokens and the tokens are not included in the domain

                // If listType is "any", allow anything to go in the list
                // if listType is "text", only allow text to be placed in the list
                // if listType is specific "text" values, only allow those values in the list

                if (contents.get(tag).listType == Type.get("text")) {
                    // Validate String
                    boolean isTextValid = true;
                    String textContent = (String) content;
                    if (!textContent.startsWith("'") || !textContent.endsWith("'")) {
                        isTextValid = false;
                    }
                    if (isTextValid) {
                        contentList.add(content);
                    } else {
                        System.out.println("Error: Cannot assign non-text to text feature.");
                    }
                } else if (contents.get(tag).listType == Type.get("construct")) {
                    contentList.add(content);
                }
            } else if (contents.get(tag).type == Type.get("text")) {
                contents.get(tag).content = (String) content;
            } else {
                contents.get(tag).content = content;
            }
        }
    }

    public Content get(String tag) {
        if (contents.containsKey(tag)) {
            return contents.get(tag);
        }
        return null;
    }

    public static void add(String tag) {

        // TODO: add <list-feature-tag> : <content>

    }

}
