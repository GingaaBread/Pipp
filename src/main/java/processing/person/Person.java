package processing.person;

import error.IncorrectFormatException;
import lombok.Data;
import lombok.NonNull;

/**
 * This class is used for a person that can be identified by a first and last name, and an optional title such as
 * Dr., Prof., etc.
 * Note that the title must be set via the property's setter as there is no constructor for it.
 * The first and last names can be parsed using a single name property, which will separate the first and last name by
 * the last space in the string.
 *
 * @version 1.0
 * @since 1.0
 */
@Data
public class Person {

    /**
     * Defines the title of the author (Prof., Dr., etc.).
     * If the author has no title, this field is null.
     */
    private String title;

    /**
     * Defines the first name of the author.
     * If the author has multiple first names, they can all be put in the variable.
     */
    private String firstname;

    /**
     * Defines the last name of the author.
     * If the author has multiple last names, they can all be put in the variable.
     */
    private String lastname;

    /**
     * Creates a new Person by parsing the name string.
     * This will try to separate the first name from the last name.
     * Trailing white space is removed and the last space marks the end of the first name,
     * Then follows the last name.
     *
     * @param name the name of the Person that should be parsed and converted into a first and last name.
     */
    public Person(@NonNull final String name) {
        tryParseName(name);
    }

    /**
     * Creates a new Person by setting the first and last name manually.
     *
     * @param firstname the first name of the Person with leading and trailing white space removed
     * @param lastname  the last name of the Person with leading and trailing white space removed
     * @throws IncorrectFormatException if the first or last name is blank
     */
    public Person(@NonNull final String firstname, @NonNull final String lastname) {
        if (firstname.isBlank() || lastname.isBlank())
            throw new IncorrectFormatException("9: Could not parse name property.");

        this.firstname = firstname.trim();
        this.lastname = lastname.trim();
    }

    /**
     * Used to get the first and last name of the author separated by a single space.
     *
     * @return the name of the author as a String
     */
    public String nameToString() {
        return (title == null ? "" : (title + " ")) + firstname + " " + lastname;
    }

    /**
     * Tries to parse the name into a first and last name by scanning for the last space after removing leading
     * and trailing white space from the name. The part before that last space is the first name and the remaining
     * part is the last name. Note that this does not affect the title in any way.
     *
     * @param name the name that should be parsed into a first and last name
     * @throws IllegalArgumentException when the first or last name is already set
     * @throws IncorrectFormatException when the first or last name are null or blank at the end of the method
     */
    private void tryParseName(@NonNull String name) {
        if (firstname != null || lastname != null)
            throw new IllegalStateException("Should not try to parse a name when first and last name exist");

        // Trim the input to remove leading and trailing white space "   John Doe  " -> "John Doe"
        name = name.trim();

        // Scan the name backwards until the first space is found. This part is the last name
        for (int i = name.length() - 1; i != 0; i--) {
            var current = name.charAt(i);

            if (Character.isWhitespace(current)) {
                firstname = name.substring(0, i).trim();
                lastname = name.substring(i).trim();
                break;
            }
        }

        if (firstname == null || firstname.isBlank() || lastname == null || lastname.isBlank())
            throw new IncorrectFormatException("9: Could not parse name property.");
    }

}
