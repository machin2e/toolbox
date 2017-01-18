package camp.computer.data.format.configuration;

/**
 * An {@code AttributeValueSet} stores a list of title values for a specified title identified by its unique label.
 */
public class Attribute<T> {

    public String title = "none"; // e.g., mode; direction; voltage

    public T value = null;

    public Attribute(String title, T value) {

        this.title = title;
        this.value = value;

    }

}
