package camp.computer.construct;

import java.util.HashMap;
import java.util.List;

import camp.computer.Application;
import camp.computer.Interpreter;
import camp.computer.workspace.Manager;

public class Construct extends Identifier {

    // <CONCEPT>
    public Type type = null;

    public HashMap<String, Feature> features = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?
    // </CONCEPT>

    public HashMap<String, State> states = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?

    // TODO: configuration(s) : assign state to multiple features <-- do this for _Container_ not Concept

    private Construct(Concept concept) {

        this.type = concept.type;

        // Create Content for each Feature
        for (Feature feature : concept.features.values()) {
//            Content objectInstance = new Content(feature);
            features.put(feature.identifier, feature);
            if (feature.type.size() == 1) {
                states.put(feature.identifier, State.getState(feature.type.get(0))); // Initialize with only available type if there's only one available
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

                    // Remove the type of the stored objectInstance
                    if (state == null) {
                        state = State.getState(contentType);
                    } else if (state.type != contentType) {
//                        featureContent.classType = null;
//                        featureContent.objectInstance = null;
                        state = State.getState(contentType);
                    }

                } else if (contentType == Type.get("list")) {

                    // Change the type of the stored objectInstance if it is not a list
                    if (state == null) {
                        state = State.getState(contentType);
                    } else if (state.type != contentType) {
//                        featureContent.classType = List.class;
//                        featureContent.objectInstance = new ArrayList<>();
                        state = State.getState(contentType);
                    }

                    // Update the objectInstance

                } else if (contentType == Type.get("text")) {

                    // Change the type of the stored objectInstance if it is not a string (for text)
                    if (state == null) {
                        state = State.getState(contentType);
                    } else if (state.type != contentType) {
//                        featureContent.classType = String.class;
//                        featureContent.objectInstance = null;
                        state = State.getState(contentType);
                    }

//                    if (Content.isText((String) objectInstance)) {
                    if (featureContent.domain == null || featureContent.domain.contains((String) content)) {
                        state.objectInstance = (String) content;
                    } else {
                        System.out.println(Application.ANSI_RED + "Error: Specified text is not in the feature's domain." + Application.ANSI_RESET);
                    }
//                    } else {
//                        System.out.println("Error: Cannot assign non-text to text feature.");
//                    }

                } else {


                }
            } else {
                System.out.println(Application.ANSI_RED + "Error: Feature type mismatches objectInstance type." + Application.ANSI_RESET);
            }


            /*
            if (contents.get(tag).type == Type.get("none")) {
                // TODO: Can't assign anything to the feature objectInstance
                System.out.println("Error: Cannot assign feature with type 'none'.");
            } else if (contents.get(tag).type == Type.get("any")) {
                // TODO: Verify that this is correct!
                contents.get(tag).objectInstance = objectInstance;
            } else if (contents.get(tag).type == Type.get("list")) {
                List contentList = (List) contents.get(tag).objectInstance;

                // TODO: Check type of list contents and restrict to the type (or any if "any")
                // TODO: If specific text tokens are allowed AS WELL AS text construct, text construct subsumes the tokens and the tokens are not included in the domain

//                if (contents.get(identifier).listType == Type.get("text")) {
                if (contents.get(tag).listTypes.contains(Type.get("text"))) {
                    if (Content.isText((String) objectInstance)) {
                        contentList.add(objectInstance);
                    } else {
                        System.out.println("Error: Cannot add non-text to list (only can contain text).");
                    }
//                } else if (contents.get(identifier).listType == Type.get("construct")) {
//                } else if (contents.get(identifier).listTypes.contains(Type.get("construct"))) {
                } else {
                    // TODO: Determine if the construct objectInstance is allowed into the list based on the specific type!
                    contentList.add(objectInstance);
                }
            } else if (contents.get(tag).type == Type.get("text")) {
                if (Content.isText((String) objectInstance)) {
                    contents.get(tag).objectInstance = (String) objectInstance;
                } else {
                    System.out.println("Error: Cannot assign non-text to text feature.");
                }
            } else {
                contents.get(tag).objectInstance = objectInstance;
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

    // TODO: add <list-feature-identifier> : <objectInstance>
    public void add(String featureIdentifier, Object stateExpression) {
        if (features.containsKey(featureIdentifier)) {
            Feature feature = features.get(featureIdentifier);
            State featureState = states.get(featureIdentifier);

            // Check if feature can be a list
            if (!feature.type.contains(Type.get("list"))) {
                System.out.println(Application.ANSI_RED + "Error: Cannot add to a non-list." + Application.ANSI_RESET);
                return;
            }

            // Check if feature is currently configured as a list
            if (featureState.type != Type.get("list")) {
                // Change the type of the stored objectInstance if it is not a list
                if (featureState == null) {
                    featureState = State.getState(Type.get("list"));
                } else if (featureState.type != Type.get("list")) {
                    featureState = State.getState(Type.get("list"));
                }
            }

            // Add the objectInstance to the list
            Type stateType = Feature.getType((String) stateExpression);
            if (stateType != null
                    && (feature.listTypes == null || feature.listTypes.contains(stateType))) {

                if (stateType == Type.get("none")) {

//                    // Remove the type of the stored objectInstance
//                    if (featureContent.state == null) {
//                        featureContent.state = new State(classType);
//                    } else if (featureContent.state.type != classType) {
////                        featureContent.classType = null;
////                        featureContent.objectInstance = null;
//                        featureContent.state = new State(classType);
//                    }

                } else if (stateType == Type.get("list")) {

                    // Change the type of the stored objectInstance if it is not a list
                    if (featureState == null) {
                        featureState = State.getState(stateType);
                    } else if (featureState.type != stateType) {
//                        featureContent.classType = List.class;
//                        featureContent.objectInstance = new ArrayList<>();
                        featureState = State.getState(stateType);
                    }

                    // Update the objectInstance

                } else if (stateType == Type.get("text")) {

                    // Change the type of the stored objectInstance if it is not a string (for text)
//                    if (featureContent.state == null) {
//                        featureContent.state = new State(classType);
//                    } else if (featureContent.state.type != classType) {
////                        featureContent.classType = String.class;
////                        featureContent.objectInstance = null;
//                        featureContent.state = new State(classType);
//                    }


                    // Encapsulate text state
                    State state = State.getState(stateType);
                    state.objectInstance = stateExpression;



//                    if (Content.isText((String) objectInstance)) {
                    if (feature.domain == null || feature.domain.contains((String) stateExpression)) {
                    // TODO: if (feature.domain == null || feature.domain.contains(state)) {
                            List list = (List) featureState.objectInstance;
//                        contents.get(tag).state.objectInstance = (String) objectInstance;
                            list.add(stateExpression);
                        } else {
                            System.out.println(Application.ANSI_RED + "Error: Specified " + stateType + " is not in the feature's domain." + Application.ANSI_RESET);
                        }
//                    }
//                    } else {
//                        System.out.println("Error: Cannot assign non-text to text feature.");
//                    }

                } else {

//                    // Change the type of the stored objectInstance if it is not a list
//                    if (state == null) {
//                        state = State.getState(contentType);
//                    } else if (state.type != contentType) {
////                        featureContent.classType = List.class;
////                        featureContent.objectInstance = new ArrayList<>();
//                        state = State.getState(contentType);
//                    }

                    // Encapsulate text state
                    State state = null;
                    if (Interpreter.isConstructToken((String) stateExpression)) {

                        String typeToken = (String) stateExpression;

                        String typeIdentifierToken = typeToken.substring(0, typeToken.indexOf("(")).trim(); // text before '('
                        String addressTypeToken = typeToken.substring(typeToken.indexOf("(") + 1, typeToken.indexOf(":")).trim(); // text between '(' and ':'
                        String addressToken = typeToken.substring(typeToken.indexOf(":") + 1, typeToken.indexOf(")")).trim(); // text between ':' and ')'

                        long uid = Long.parseLong(addressToken.trim());

                        Identifier identifier = Manager.get(uid);
                        if(identifier.getClass() == Construct.class) {
                            state = State.getState(stateType);
                            state.objectInstance = (Construct) identifier; // TODO:
                        }
                    }
                // TODO: parse out Construct instance

                    // Add to the list in memory
//                    if (Content.isText((String) objectInstance)) {
                    if (feature.domain == null || feature.domain.contains((String) stateExpression)) { // TODO: Update domain to contain State objects so it can contain port and other Constructs
                        List list = (List) featureState.objectInstance;
//                        contents.get(tag).state.objectInstance = (String) objectInstance;
                        list.add(state.objectInstance);
                    } else {
                        System.out.println(Application.ANSI_RED + "Error: Specified " + stateType + " is not in the feature's domain." + Application.ANSI_RESET);
                    }

                }
            } else {
                System.out.println(Application.ANSI_RED + "Error: Feature type mismatches objectInstance type." + Application.ANSI_RESET);
            }


            /*
            if (contents.get(tag).type == Type.get("none")) {
                // TODO: Can't assign anything to the feature objectInstance
                System.out.println("Error: Cannot assign feature with type 'none'.");
            } else if (contents.get(tag).type == Type.get("any")) {
                // TODO: Verify that this is correct!
                contents.get(tag).objectInstance = objectInstance;
            } else if (contents.get(tag).type == Type.get("list")) {
                List contentList = (List) contents.get(tag).objectInstance;

                // TODO: Check type of list contents and restrict to the type (or any if "any")
                // TODO: If specific text tokens are allowed AS WELL AS text construct, text construct subsumes the tokens and the tokens are not included in the domain

//                if (contents.get(identifier).listType == Type.get("text")) {
                if (contents.get(tag).listTypes.contains(Type.get("text"))) {
                    if (Content.isText((String) objectInstance)) {
                        contentList.add(objectInstance);
                    } else {
                        System.out.println("Error: Cannot add non-text to list (only can contain text).");
                    }
//                } else if (contents.get(identifier).listType == Type.get("construct")) {
//                } else if (contents.get(identifier).listTypes.contains(Type.get("construct"))) {
                } else {
                    // TODO: Determine if the construct objectInstance is allowed into the list based on the specific type!
                    contentList.add(objectInstance);
                }
            } else if (contents.get(tag).type == Type.get("text")) {
                if (Content.isText((String) objectInstance)) {
                    contents.get(tag).objectInstance = (String) objectInstance;
                } else {
                    System.out.println("Error: Cannot assign non-text to text feature.");
                }
            } else {
                contents.get(tag).objectInstance = objectInstance;
            }
            */
        }
    }

    @Override
    public String toString() {
        return type + "(id:" + uid + ")";
    }

}
