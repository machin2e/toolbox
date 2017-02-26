package camp.computer.construct;

import java.util.HashMap;
import java.util.List;

import camp.computer.Application;
import camp.computer.workspace.Manager;

public class Construct extends Identifier {

    // In Redis, primitive types has types and content; non-primitive has no content.
    // TODO: Use "features" object as a HashMap for non-primitive to reference features;
    // TODO:      ArrayList for primitive "list" types;
    // TODO:      String for primitive "text" types;
    // TODO:      Double for primitive "number" types;
    // TODO:      null for primitive "none" types

    // <CONCEPT>
    public Type type = null;

    public HashMap<String, Feature> features = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?
    // TODO: (Replace ^ with this, based on TODO block above:) Bytes storing actual object and object types
    public Class objectType = null; // null for "none", List for "list", String for "text", Construct for non-primitive types
    public Object object = null;
    // </CONCEPT>

    // TODO: Remove this after removing the State class.
    public HashMap<String, State> states = new HashMap<>(); // TODO: Remove? Remove setupConfiguration?

    // TODO: configuration(s) : assign state to multiple features <-- do this for _Container_ not Concept


    // TODO:
    // 1. Use types, features, and states for non-terminal (structure) constructs (custom non-primitive constructs)
    // 2. For terminal states (i.e., to replace State), don't use features or states hashes. Store actual data in objectType and object (from State).

    private Construct(Concept concept) {

        this.type = concept.type;

        // Create Content for each Feature
        for (Feature feature : concept.features.values()) {
            features.put(feature.identifier, feature);
            if (feature.types.size() == 1) {
                states.put(feature.identifier, State.getState(feature.types.get(0))); // Initialize with only available types if there's only one available
            } else {
                states.put(feature.identifier, null); // Default to "any" types by setting null
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
    public void set(String featureIdentifier, String expression) {
        if (features.containsKey(featureIdentifier)) {

            Type stateType = Type.get((String) expression);
            Feature feature = features.get(featureIdentifier);
            if (feature.types.contains(stateType)) {

                /*
                // Get feature's current state
                State state = states.get(featureIdentifier);

                if (stateType == Type.get("none")) {

                    // Remove the types of the stored object
                    if (state == null) {
                        state = State.getState(stateType);
                    } else if (state.types != stateType) {
//                        featureContent.objectType = null;
//                        featureContent.object = null;
                        state = State.getState(stateType);
                    }

                } else if (stateType == Type.get("list")) {

                    // Change the types of the stored object if it is not a list
                    if (state == null) {
                        state = State.getState(stateType);
                    } else if (state.types != stateType) {
//                        featureContent.objectType = List.class;
//                        featureContent.object = new ArrayList<>();
                        state = State.getState(stateType);
                    }

                    // Update the object

                } else if (stateType == Type.get("text")) {

                    // Change the types of the stored object if it is not a string (for text)
//                    if (state == null) {
//                        state = State.getPersistentState(stateType);
//                    } else if (state.types != stateType) {
////                        featureContent.objectType = String.class;
////                        featureContent.object = null;
//                        state = State.getPersistentState(stateType);
//                    }

//                    if (Content.isText((String) object)) {
                   state = State.getState((String) expression);
//                    if (feature.domain == null || feature.domain.contains(stateExpression)) {
                    if (feature.domain == null || feature.domain.contains(state)) { // TODO: Make sure 'contains' works!
//                        state.object = (String) stateExpression;
                        states.put(featureIdentifier, state);
                    } else {
                        System.out.println(Application.ANSI_RED + "Error: Specified text is not in the feature's domain." + Application.ANSI_RESET);
                    }
//                    } else {
//                        System.out.println("Error: Cannot assign non-text to text feature.");
//                    }

                } else {


                }
                */

                if (stateType == Type.get("none")) {

                    State state = State.getState(expression);

                    if (feature.domain == null || feature.domain.contains(state)) { // TODO: Make sure 'contains' works!
                        states.put(featureIdentifier, state);
                        // TODO: Update Construct in database
                    } else {
                        System.out.println(Application.ANSI_RED + "Error: Specified text is not in the feature's domain." + Application.ANSI_RESET);
                    }

//                } else if (stateType == Type.get("text")) {
//
//                    State state = State.getState(expression);
//
//                    if (state != null) {
//                        if (feature.domain == null || feature.domain.contains(state)) { // TODO: Make sure 'contains' works!
//                            states.put(featureIdentifier, state);
//                            // TODO: Update Construct in database
//                        } else {
//                            System.out.println(Application.ANSI_RED + "Error: Specified text is not in the feature's domain." + Application.ANSI_RESET);
//                        }
//                    } else {
//                        System.out.println(Application.ANSI_RED + "Error: Interpreter error. State is null." + Application.ANSI_RESET);
//                    }

                } else if (stateType == Type.get("list")) {

                    // TODO: Allow lists to be assigned? Yes!
                    System.out.println(Application.ANSI_RED + "Error: Cannot SET on a list. (This might change!)." + Application.ANSI_RESET);

                } else {

//                    State state = State.getState(expression);
//
//                    // Add to the list in memory
//                    // TODO: if (state != null && state != states.get(featureIdentifier)) {
//                    if (state != null) {
//                        if (feature.domain == null || feature.domain.contains(state)) { // TODO: Update domain to contain State objects so it can contain port and other Constructs
//                            State featureState = states.get(featureIdentifier);
//                            Construct featureConstruct = (Construct) featureState.object;
////                        contents.get(tag).state.object = (String) object;
//                            featureConstruct.add(state);
//                        } else {
//                            System.out.println(Application.ANSI_RED + "Error: Specified " + stateType + " is not in the feature's domain." + Application.ANSI_RESET);
//                        }
//                    }

                    State state = State.getState(expression);

                    if (state != null) {
                        if (feature.domain == null || feature.domain.contains(state)) { // TODO: Make sure 'contains' works!
                            states.put(featureIdentifier, state);
                            // TODO: Update Construct in database
                        } else {
                            System.out.println(Application.ANSI_RED + "Error: Specified text is not in the feature's domain." + Application.ANSI_RESET);
                        }
                    } else {
                        System.out.println(Application.ANSI_RED + "Error: Interpreter error. State is null." + Application.ANSI_RESET);
                    }

//                    System.out.println(Application.ANSI_RED + "Error: Feature types mismatches object types." + Application.ANSI_RESET);

                }
            }


            /*
            if (contents.get(tag).types == Type.get("none")) {
                // TODO: Can't assign anything to the feature object
                System.out.println("Error: Cannot assign feature with types 'none'.");
            } else if (contents.get(tag).types == Type.get("any")) {
                // TODO: Verify that this is correct!
                contents.get(tag).object = object;
            } else if (contents.get(tag).types == Type.get("list")) {
                List contentList = (List) contents.get(tag).object;

                // TODO: Check types of list contents and restrict to the types (or any if "any")
                // TODO: If specific text tokens are allowed AS WELL AS text construct, text construct subsumes the tokens and the tokens are not included in the domain

//                if (contents.get(identifier).listType == Type.get("text")) {
                if (contents.get(tag).listTypes.contains(Type.get("text"))) {
                    if (Content.isText((String) object)) {
                        contentList.add(object);
                    } else {
                        System.out.println("Error: Cannot add non-text to list (only can contain text).");
                    }
//                } else if (contents.get(identifier).listType == Type.get("construct")) {
//                } else if (contents.get(identifier).listTypes.contains(Type.get("construct"))) {
                } else {
                    // TODO: Determine if the construct object is allowed into the list based on the specific types!
                    contentList.add(object);
                }
            } else if (contents.get(tag).types == Type.get("text")) {
                if (Content.isText((String) object)) {
                    contents.get(tag).object = (String) object;
                } else {
                    System.out.println("Error: Cannot assign non-text to text feature.");
                }
            } else {
                contents.get(tag).object = object;
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

    // TODO: add <list-feature-identifier> : <object>

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
            if (!feature.types.contains(Type.get("list"))) {
                System.out.println(Application.ANSI_RED + "Error: Cannot add to a non-list." + Application.ANSI_RESET);
                return;
            }

            // Check if feature is currently configured as a list
            if (featureState.type != Type.get("list")) {
                // Change the types of the stored object if it is not a list
                if (featureState == null) {
                    featureState = State.getState(Type.get("list"));
                } else if (featureState.type != Type.get("list")) {
                    featureState = State.getState(Type.get("list"));
                }
            }

            // Add the object to the list
            Type stateType = Type.get((String) expression);
            if (stateType != null
                    && (feature.listTypes == null || feature.listTypes.contains(stateType))) {

                if (stateType == Type.get("none")) {

//                    // Remove the types of the stored object
//                    if (featureContent.state == null) {
//                        featureContent.state = new State(objectType);
//                    } else if (featureContent.state.types != objectType) {
////                        featureContent.objectType = null;
////                        featureContent.object = null;
//                        featureContent.state = new State(objectType);
//                    }

                } else if (stateType == Type.get("list")) {

                    // Change the types of the stored object if it is not a list
                    if (featureState == null) {
                        featureState = State.getState(stateType);
                    } else if (featureState.type != stateType) {
//                        featureContent.objectType = List.class;
//                        featureContent.object = new ArrayList<>();
                        featureState = State.getState(stateType);
                    }

                    // Update the object

                } else if (stateType == Type.get("text")) {

                    // Change the types of the stored object if it is not a string (for text)
//                    if (featureContent.state == null) {
//                        featureContent.state = new State(objectType);
//                    } else if (featureContent.state.types != objectType) {
////                        featureContent.objectType = String.class;
////                        featureContent.object = null;
//                        featureContent.state = new State(objectType);
//                    }


//                    // Encapsulate text state
//                    State state = State.getState(stateType);
//                    state.object = expression;

                    // Encapsulate text state
                    State state = State.getState(expression);


//                    if (Content.isText((String) object)) {
//                    if (feature.domain == null || feature.domain.contains((String) expression)) {
                    if (feature.domain == null || feature.domain.contains(state)) {
                    // TODO: if (feature.domain == null || feature.domain.contains(state)) {
                            List list = (List) featureState.object;
//                        contents.get(tag).state.object = (String) object;
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

//                    // Change the types of the stored object if it is not a list
//                    if (state == null) {
//                        state = State.getPersistentState(contentType);
//                    } else if (state.types != contentType) {
////                        featureContent.objectType = List.class;
////                        featureContent.object = new ArrayList<>();
//                        state = State.getPersistentState(contentType);
//                    }

                    // Encapsulate text state
                    State state = State.getState(expression);

                    // Add to the list in memory
//                    if (Content.isText((String) object)) {
                    if (state != null) {
//                        if (feature.domain == null || feature.domain.contains((String) expression)) { // TODO: Update domain to contain State objects so it can contain port and other Constructs
                        if (feature.domain == null || feature.domain.contains(state)) { // TODO: Update domain to contain State objects so it can contain port and other Constructs
                            List list = (List) featureState.object;
//                        contents.get(tag).state.object = (String) object;
                            list.add(state);
                        } else {
                            System.out.println(Application.ANSI_RED + "Error: Specified " + stateType + " is not in the feature's domain." + Application.ANSI_RESET);
                        }
                    }

                }
            } else {
                System.out.println(Application.ANSI_RED + "Error: Feature types mismatches object types." + Application.ANSI_RESET);
            }


            /*
            if (contents.get(tag).types == Type.get("none")) {
                // TODO: Can't assign anything to the feature object
                System.out.println("Error: Cannot assign feature with types 'none'.");
            } else if (contents.get(tag).types == Type.get("any")) {
                // TODO: Verify that this is correct!
                contents.get(tag).object = object;
            } else if (contents.get(tag).types == Type.get("list")) {
                List contentList = (List) contents.get(tag).object;

                // TODO: Check types of list contents and restrict to the types (or any if "any")
                // TODO: If specific text tokens are allowed AS WELL AS text construct, text construct subsumes the tokens and the tokens are not included in the domain

//                if (contents.get(identifier).listType == Type.get("text")) {
                if (contents.get(tag).listTypes.contains(Type.get("text"))) {
                    if (Content.isText((String) object)) {
                        contentList.add(object);
                    } else {
                        System.out.println("Error: Cannot add non-text to list (only can contain text).");
                    }
//                } else if (contents.get(identifier).listType == Type.get("construct")) {
//                } else if (contents.get(identifier).listTypes.contains(Type.get("construct"))) {
                } else {
                    // TODO: Determine if the construct object is allowed into the list based on the specific types!
                    contentList.add(object);
                }
            } else if (contents.get(tag).types == Type.get("text")) {
                if (Content.isText((String) object)) {
                    contents.get(tag).object = (String) object;
                } else {
                    System.out.println("Error: Cannot assign non-text to text feature.");
                }
            } else {
                contents.get(tag).object = object;
            }
            */
        }
    }

    @Override
    public String toString() {
        return Application.ANSI_BLUE + type + Application.ANSI_RESET + " (id:" + uid + ")";
    }

}
