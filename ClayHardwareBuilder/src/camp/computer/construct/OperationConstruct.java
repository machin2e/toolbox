package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@code OperationConstruct} represents an <em>operation</em>. An operation is either
 * (a) a <em>primitive operation</em> defined in the domain-specific language supported by the CLI,
 * or (b) a <em>constructed operation</em> composed of a sequence of primitive operations and other
 * constructed primitives.
 */
public class OperationConstruct extends Construct {

    public List<String> operations = new ArrayList<>();

    public OperationConstruct() {
        super();
    }

}
