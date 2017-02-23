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
    public void set(String tag, Object stateExpression) {
        if (features.containsKey(tag)) {

            Type stateType = Type.get((String) stateExpression);
            Feature feature = features.get(tag);
            if (feature.type.contains(stateType)) {
                State state = states.get(tag);

                if (stateType == Type.get("none")) {

                    // Remove the type of the stored objectInstance
                    if (state == null) {
                        state = State.getState(stateType);
                    } else if (state.type != stateType) {
//                        featureContent.classType = null;
//                        featureContent.objectInstance = null;
                        state = State.getState(stateType);
                    }

                } else if (stateType == Type.get("list")) {

                    // Change the type of the stored objectInstance if it is not a list
                    if (state == null) {
                        state = State.getState(stateType);
                    } else if (state.type != stateType) {
//                        featureContent.classType = List.class;
//                        featureContent.objectInstance = new ArrayList<>();
                        state = State.getState(stateType);
                    }

                    // Update the objectInstance

                } else if (stateType == Type.get("text")) {

                    // Change the type of the stored objectInstance if it is not a string (for text)
//                    if (state == null) {
//                        state = State.getPersistentState(stateType);
//                    } else if (state.type != stateType) {
////                        featureContent.classType = String.class;
////                        featureContent.objectInstance = null;
//                        state = State.getPersistentState(stateType);
//                    }

//                    if (Content.isText((String) objectInstance)) {
                   state = State.getState((String) stateExpression);
//                    if (feature.domain == null || feature.domain.contains(stateExpression)) {
                    if (feature.domain == null || feature.domain.contains(state)) { // TODO: Make sure 'contains' works!
//                        state.objectInstance = (String) stateExpression;
                        states.put(tag, state);
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

    /**
     * Adds a {@code State} to a <em>list</em> {@code Construct}, which is a {@code Construct} with
     * a {@code Type} uniquely identified by its {@code identifier} equal to {@code "list"}.
     *
     * {@code expression} is a <em>state expression</em>.
     *
     * @param featureIdentifier
     * @param expression
     */
    public void add(String featureIdentifier, String expression) {
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
            Type stateType = Type.get((String) expression);
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


//                    // Encapsulate text state
//                    State state = State.getState(stateType);
//                    state.objectInstance = expression;

                    // Encapsulate text state
                    State state = State.getState(expression);


//                    if (Content.isText((String) objectInstance)) {
//                    if (feature.domain == null || feature.domain.contains((String) expression)) {
                    if (feature.domain == null || feature.domain.contains(state)) {
                    // TODO: if (feature.domain == null || feature.domain.contains(state)) {
                            List list = (List) featureState.objectInstance;
//                        contents.get(tag).state.objectInstance = (String) objectInstance;
//                            list.add(expression);
                            list.add(state);
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
//                        state = State.getPersistentState(contentType);
//                    } else if (state.type != contentType) {
////                        featureContent.classType = List.class;
////                        featureContent.objectInstance = new ArrayList<>();
//                        state = State.getPersistentState(contentType);
//                    }

                    // Encapsulate text state
                    State state = State.getState(expression);

                    // Add to the list in memory
//                    if (Content.isText((String) objectInstance)) {
                    if (state != null) {
//                        if (feature.domain == null || feature.domain.contains((String) expression)) { // TODO: Update domain to contain State objects so it can contain port and other Constructs
                        if (feature.domain == null || feature.domain.contains(state)) { // TODO: Update domain to contain State objects so it can contain port and other Constructs
                            List list = (List) featureState.objectInstance;
//                        contents.get(tag).state.objectInstance = (String) objectInstance;
                            list.add(state);
                        } else {
                            System.out.println(Application.ANSI_RED + "Error: Specified " + stateType + " is not in the feature's domain." + Application.ANSI_RESET);
                        }
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
