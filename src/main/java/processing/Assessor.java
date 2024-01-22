package processing;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * The Assessor class represents an assessor of the user's document.
 * That is a person, typically one of the people evaluating the document, represented by a role.
 * An assessor requires a name, but the role can be left out as well.
 *
 * @version 1.0
 * @since 1.0
 */
public class Assessor extends Person {

    /**
     * Defines the role of the assessor. If the assessor does not have a role, this value will be null.
     * Example roles could be "Instructor", "Teacher", "Professor", etc.
     */
    @Getter
    @Setter
    private String role;

    /**
     * Creates a new assessor by specifying the first and last names separately.
     * Note that leading and trailing white space is removed.
     *
     * @param firstname - the first name of the string as a whole string. For example: "John"
     * @param lastname  - the last name of the string as a whole string. For example: "Doe"
     */
    public Assessor(@NonNull final String firstname, @NonNull final String lastname) {
        super(firstname, lastname);
    }

    /**
     * Creates a new assessor by specifying the name as a whole string.
     * In that case, all characters until the last white space character are interpreted as the first name,
     * and all remaining characters  are interpreted as the last name.
     * Note that leading and trailing white space is removed.
     *
     * @param name - the name of the assessor as a whole string. For example: "John Doe"
     */
    public Assessor(@NonNull final String name) {
        super(name);
    }

}
