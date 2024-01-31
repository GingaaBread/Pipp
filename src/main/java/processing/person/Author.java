package processing.person;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Represents the author of a type of work represented by a first and last name.
 * The author can also have an optional identification number/string.
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class Author extends Person {

    /**
     * Defines the identification number/string of the author.
     * Providing an author's ID is optional, so this field can be null.
     */
    private String id;

    /**
     * Creates a new author by specifying the first and last names separately.
     * Note that leading and trailing white space is removed.
     *
     * @param firstname - the first name of the string as a whole string. For example: "John"
     * @param lastname  - the last name of the string as a whole string. For example: "Doe"
     */
    public Author(@NonNull final String firstname, @NonNull final String lastname) {
        super(firstname, lastname);
    }

    /**
     * Creates a new author by specifying the name as a whole string.
     * In that case, all characters until the last white space character are interpreted as the first name,
     * and all characters after it are interpreted as the last name.
     * Note that leading and trailing white space is removed.
     *
     * @param name - the name of the assessor as a whole string. For example: "John Doe"
     */
    public Author(@NonNull final String name) {
        super(name);
    }

}
