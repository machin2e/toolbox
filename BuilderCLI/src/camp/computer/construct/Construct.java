package camp.computer.construct;

import java.util.HashMap;
import java.util.List;

import camp.computer.workspace.Manager;

public class Construct extends Identifier {

    // Concept -> Feature : Construct -> Content

    public Type type = null;

//    public HashMap<String, Content> contents = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?
    public HashMap<String, Feature> features = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?
    public HashMap<String, State> states = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?

    // TODO: configuration(s) : assign state to multiple features <-- do this for _Container_ not Concept

    private Construct(Concept concept) {

        this.type = concept.type;

        // Create Content for each Feature
        for (Feature feature : concept.features.values()) {
//            Content content = new Content(feature);
            features.put(feature.identifier, feature);
            if (feature.type.size() == 1) {
                states.put(feature.identifier, new State(feature.type.get(0))); // Initialize with only available type if there's only one available
            } else {
                states.put(feature.identifier, null); // Default to "any" type by setting null
            }
        }

    }

    public static Construct add(Type type) {
        if (Concept.has(type)) {
            Concept concept = Concept.get(type);
            Construct construct = new Construct(concept);
            long uid = Manager.add(construct);
            return construct;
        }
        return null;
    }

    // If listType is "any", allow anything to go in the list
    // if listType is "text", only allow text to be placed in the list
    // if listType is specific "text" values, only allow those values in the list
    public void set(String tag, Object content) {
        if (features.containsKey(tag)) {

            Type contentType = Feature.getType((String) content);
            Feature featureContent = features.get(tag);
            if (featureContent.type.contains(contentType)) {
                State state = states.get(tag);

                if (contentType == Type.get("none")) {

                    // Remove the type of the stored content
                    if (state == null) {
                        state = new State(contentType);
                    } else if (state.type != contentType) {
//                        featureContent.contentType = null;
//                        featureContent.content = null;
                        state = new State(contentType);
                    }

                } else if (contentType == Type.get("list")) {

                    // Change the type of the stored content if it is not a list
                    if (state == null) {
                        state = new State(contentType);
                    } else if (state.type != contentType) {
//                        featureContent.contentType = List.class;
//                        featureContent.content = new ArrayList<>();
                        state = new State(contentType);
                    }

                    // Update the content

                } else if (contentType == Type.get("text")) {

                    // Change the type of the stored content if it is not a string (for text)
                    if (state == null) {
                        state = new State(contentType);
                    } else if (state.type != contentType) {
//                        featureContent.contentType = String.class;
//                        featureContent.content = null;
                        state = new State(contentType);
                    }

//                    if (Content.isText((String) content)) {
                    if (featureContent.domain == null || featureContent.domain.contains((String) content)) {
                        state.content = (String) content;
                    } else {
                        System.out.println("Error: Specified text is not in the feature's domain.");
                    }
//                    } else {
//                        System.out.println("Error: Cannot assign non-text to text feature.");
//                    }

                } else {



                }
            } else {
                System.out.println("Error: Feature type mismatches content type.");
            }


            /*
            if (contents.get(tag).type == Type.get("none")) {
                // TODO: Can't assign anything to the feature content
                System.out.println("Error: Cannot assign feature with type 'none'.");
            } else if (contents.get(tag).type == Type.get("any")) {
                // TODO: Verify that this is correct!
                contents.get(tag).content = content;
            } else if (contents.get(tag).type == Type.get("list")) {
                List contentList = (List) contents.get(tag).content;

                // TODO: Check type of list contents and restrict to the type (or any if "any")
                // TODO: If specific text tokens are allowed AS WELL AS text construct, text construct subsumes the tokens and the tokens are not included in the domain

//                if (contents.get(identifier).listType == Type.get("text")) {
                if (contents.get(tag).listTypes.contains(Type.get("text"))) {
                    if (Content.isText((String) content)) {
                        contentList.add(content);
                    } else {
                        System.out.println("Error: Cannot add non-text to list (only can contain text).");
                    }
//                } else if (contents.get(identifier).listType == Type.get("construct")) {
//                } else if (contents.get(identifier).listTypes.contains(Type.get("construct"))) {
                } else {
                    // TODO: Determine if the construct content is allowed into the list based on the specific type!
                    contentList.add(content);
                }
            } else if (contents.get(tag).type == Type.get("text")) {
                if (Content.isText((String) content)) {
                    contents.get(tag).content = (String) content;
                } else {
                    System.out.println("Error: Cannot assign non-text to text feature.");
                }
            } else {
                contents.get(tag).content = content;
            }
            */
        }
    }

    public Feature get(String tag) {
        if (features.containsKey(tag)) {
            return features.get(tag);
        }
        return null;
    }

    // TODO: add <list-feature-identifier> : <content>
    public void add(String tag, Object content) {
        if (features.containsKey(tag)) {
            Feature feature = features.get(tag);
            State state = states.get(tag);

            // Check if feature can be a list
            if (!feature.type.contains(Type.get("list"))) {
                System.out.println("Error: Cannot add to a non-list.");
                return;
            }

            // Check if feature is currently configured as a list
            if (state.type != Type.get("list")) {
                // Change the type of the stored content if it is not a list
                if (state == null) {
                    state = new State(Type.get("list"));
                } else if (state.type != Type.get("list")) {
                    state = new State(Type.get("list"));
                }
            }

            // Add the content to the list
            Type contentType = Feature.getType((String) content);
            if (contentType != null
                    && (feature.listTypes == null || feature.listTypes.contains(contentType))) {

                if (contentType == Type.get("none")) {

//                    // Remove the type of the stored content
//                    if (featureContent.state == null) {
//                        featureContent.state = new State(contentType);
//                    } else if (featureContent.state.type != contentType) {
////                        featureContent.contentType = null;
////                        featureContent.content = null;
//                        featureContent.state = new State(contentType);
//                    }

                } else if (contentType == Type.get("list")) {

                    // Change the type of the stored content if it is not a list
                    if (state == null) {
                        state = new State(contentType);
                    } else if (state.type != contentType) {
//                        featureContent.contentType = List.class;
//                        featureContent.content = new ArrayList<>();
                        state = new State(contentType);
                    }

                    // Update the content

                } else if (contentType == Type.get("text")) {

                    // Change the type of the stored content if it is not a string (for text)
//                    if (featureContent.state == null) {
//                        featureContent.state = new State(contentType);
//                    } else if (featureContent.state.type != contentType) {
////                        featureContent.contentType = String.class;
////                        featureContent.content = null;
//                        featureContent.state = new State(contentType);
//                    }

//                    if (Content.isText((String) content)) {
                    if (feature.domain == null || feature.domain.contains((String) content)) {
                        List list = (List) state.content;
//                        contents.get(tag).state.content = (String) content;
                        list.add(content);
                    } else {
                        System.out.println("Error: Specified " + contentType + " is not in the feature's domain.");
                    }
//                    } else {
//                        System.out.println("Error: Cannot assign non-text to text feature.");
//                    }

                } else {



                }
            } else {
                System.out.println("Error: Feature type mismatches content type.");
            }


            /*
            if (contents.get(tag).type == Type.get("none")) {
                // TODO: Can't assign anything to the feature content
                System.out.println("Error: Cannot assign feature with type 'none'.");
            } else if (contents.get(tag).type == Type.get("any")) {
                // TODO: Verify that this is correct!
                contents.get(tag).content = content;
            } else if (contents.get(tag).type == Type.get("list")) {
                List contentList = (List) contents.get(tag).content;

                // TODO: Check type of list contents and restrict to the type (or any if "any")
                // TODO: If specific text tokens are allowed AS WELL AS text construct, text construct subsumes the tokens and the tokens are not included in the domain

//                if (contents.get(identifier).listType == Type.get("text")) {
                if (contents.get(tag).listTypes.contains(Type.get("text"))) {
                    if (Content.isText((String) content)) {
                        contentList.add(content);
                    } else {
                        System.out.println("Error: Cannot add non-text to list (only can contain text).");
                    }
//                } else if (contents.get(identifier).listType == Type.get("construct")) {
//                } else if (contents.get(identifier).listTypes.contains(Type.get("construct"))) {
                } else {
                    // TODO: Determine if the construct content is allowed into the list based on the specific type!
                    contentList.add(content);
                }
            } else if (contents.get(tag).type == Type.get("text")) {
                if (Content.isText((String) content)) {
                    contents.get(tag).content = (String) content;
                } else {
                    System.out.println("Error: Cannot assign non-text to text feature.");
                }
            } else {
                contents.get(tag).content = content;
            }
            */
        }
    }

}
