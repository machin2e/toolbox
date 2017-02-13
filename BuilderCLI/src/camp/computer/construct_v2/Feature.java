package camp.computer.construct_v2;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@code VariableValueSet} stores a list of tag values for a specified tag identified by its unique label.
 */
public class Feature<T> {

    // Version 0.0.4
    //
    // has port                                                          // port feature can be assigned a construct of type 'port' (inferred when no arguments if 'port' matches an existing construct type)
    // has port : port                                                   // same as above but explicitly specifies the type of the port
    // has direction : text                                              // example of same as previous, but feature can be assigned constructs of reserved construct type 'text'
    // has direction : text : 'input'                                    // can be assigned text matching 'input' only, making this a constant
    // has direction : text : 'none', 'input', 'output', 'bidirectional' // direction feature can be assigned text matching any of the specified strings
    // has direction : list : 'none', 'input', 'output', 'bidirectional' // direction feature is a list that can contain any of the specified strings
    // has my-constructs : list : device, port                           // my-constructs feature is a list that can contain any of the specified constructs; if any of the constructs
    // has my-list : list                                                // my-list is a list that can contain anything
    // has direction : list : text                                       // direction is a list that can contain elements of reserved construct type 'text'
    // has port : list : port                                            // port feature is a list that can contain elements of construct type 'port'

    // Version 0.0.5
    //
    // has <feature-identifier> [text|list] [: <type(s), token(s) or constraint(s)>]
    //
    // - has port                                                          // port feature can be assigned a construct of type 'port' (inferred when no arguments if 'port' matches an existing construct type)
    //   [INVALID] has port port port
    //   has port : port                                                   // same as above but explicitly specifies the type of the port
    //   has port : device                                                 // port feature can be assigned a construct of type 'device' (this example is nonsense in the context of Clay, but it illustrates the point)
    //   has port device                                                   // save as above, alternative syntax
    //   has port port : port                                              // third token in first segment and the only segment in the second segment must be identical; or syntax error
    // - has direction text                                                // example of same as previous, but feature can be assigned constructs of reserved construct type 'text'
    //   has direction : text                                              // same as above
    //   has direction text : text
    // - has direction : 'input'                                           // can be assigned text matching 'input' only, making this a constant
    //   has direction text : 'input'                                      // same as above
    // - has direction : 'none', 'input', 'output', 'bidirectional'        // direction feature can be assigned text matching any of the specified strings
    //   has direction text : 'none', 'input', 'output', 'bidirectional'   // same as above
    // - has my-construct : device, port                                   // my-constructs feature is a list that can contain any of the specified constructs; if any of the constructs; there's no alternative syntax with a third token in first segment?
    // - has my-list list                                                  // my-list is a list that can contain anything
    // - has direction list : text                                         // direction is a list that can contain elements of reserved construct type 'text'
    // - has direction list : 'none', 'input', 'output', 'bidirectional'   // direction feature is a list that can contain any of the specified strings
    // - has my-constructs list : device, port                             // my-constructs feature is a list that can contain any of the specified constructs; if any of the constructs
    // - has port list : port                                              // port feature is a list that can contain elements of construct type 'port'
    // - has foo : port, 'bar'                                             // foo feature can be assigned any construct of type 'port' and text equal to 'bar' (not any arbitrary text)
    // - has foo list : port, 'bar'                                             // foo feature is a list that can contain constructs of type 'port' and text equal to 'bar' (not any arbitrary text)
    //
    // TODO for 0.0.5:
    // - has my-number number: >5, <10                                              // port feature is a list that can contain elements of construct type 'port'
    // - has blah text: /slddslkj/
    // - new path
    //   has source-port port
    //   has port list
    //   let source-port ~~be assigned if it's in the port list~~
    //   let port ~~be appended if the ports satisfy `are-compatible`~~

    /*
    public enum Type {
        // Primitive Constructs:
        NONE,     // denotes the absence of a type
        ANY,      // denotes any type
        TEXT,     // reserved text construct
        // NUMBER,   // reserved number construct
        LIST,     // reserved list construct
        // Custom Construct Placeholder:
        // TODO: Create class to model the specific construct type...
        CUSTOM_CONSTRUCT // custom-defined construct
    }
    */

    // tag/key: string
    // type: string, list, construct-name
    // domain: list of accepted tokens; (or) for lists, stores list of values that can be stored in the list

    /**
     * {@code tag} is a {@code String} that uniquely identifies the {@code Variable} in the
     * containing {@code VariableMap}. <em>in the namespace</em>.
     */
    public String tag = null; // e.g., mode; direction; voltage

    public Type type;
    // TODO: public String listElementType = null; // Only used for LIST type. Constrains what it can contain...

//    public List<?> domain = new ArrayList<>();
    public List<String> domain = new ArrayList<>(); // if size == 0, then unconstrained!
    public Type listType;

    /*
    // has project list
    tag = "my-tag"
    type = Feature.Type.LIST;
    domain.add(Construct.get("list"));
    */

    /*
    // new script
    // has text string
    tag = "text"
    type = Feature.Type.TEXT
    domain.add();
    */

    /*
    // new workspace
    // has project list
    // let project : project
    tag = "project"
    type = Feature.Type.LIST
    domain.add(Construct.get("project"));
    */

    /*
    // new port
    // has mode
    // let mode : 'none', 'digital', 'analog', ...
    tag = "mode"
    type = Feature.Type.TEXT
    domain.add("none", "digital", "analog");
    */

    /*
    // new configuration
    // has mode list
    // let mode : "none", "digital", "analog", ...
    tag = "mode"
    type = Feature.Type.LIST
    domain.add("none", "digital", "analog");
    */

    /*
    // <expression>
    tag =
    type =
    domain.add();
    */

    //----------------

    public T content = null;

    public Feature(String tag) {

        this.tag = tag;
        this.content = null;

    }

    public Feature(String tag, T content) {

        this.tag = tag;
        this.content = content;

    }

}
