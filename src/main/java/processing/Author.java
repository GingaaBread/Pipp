package processing;

import error.IncorrectFormatException;
import lombok.Data;
import lombok.NonNull;

/**
 *  Represents the author of a type of work represented by a first and last name.
 *  The author can also have an optional identification number/string.
 *
 *  @author Gino Glink
 *  @version 1.0
 *  @since 1.0
 */
@Data
public class Author
{
    /**
     *  Defines the first name of the author.
     *  If the author has multiple first names, they can all be put in the variable.
     */
    private String firstname;

    /**
     *  Defines the last name of the author.
     *  If the author has multiple last names, they can all be put in the variable.
     */
    private String lastname;

    /**
     *  Defines the identification number/string of the author.
     *  Providing an author's ID is optional, so this field can be null.
     */
    private String id;

    /**
     *  Creates a new author by specifying the first and last name separately.
     *  Note that the parameters are taken "as is".
     * @param firstname - the first name of the string as a whole string. For example: "John"
     * @param lastname - the last name of the string as a whole string. For example: "Doe"
     * @param id - the ID of the author as a whole string if it exists, or null if it does not exist
     */
    public Author(@NonNull String firstname, @NonNull String lastname, String id) {
        if (firstname.isBlank() || lastname.isBlank())
            throw new IncorrectFormatException("9: Could not parse name property.");

        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    /**
     *  Creates a new author by specifying the name as a whole string.
     *  In that case, all characters until the first white space character are interpreted as the first name,
     *  and all characters after it are interpreted as the last name.
     *  If the first or last names are blank, the compilation fails with an exception
     * @param name - the name of the assessor as a whole string. For example: "John Doe"
     * @param id - the role of the assessor as a whole string if it exists, or null if it does not exist
     */
    public Author(@NonNull String name, String id) {
        this.id = id;

        name = name.trim();
        var bobTheBuilder = new StringBuilder();

        for (int i = 0; i < name.length(); i++) {
            var current = name.charAt(i);

            if (firstname == null && Character.isWhitespace(current)) {
                firstname = bobTheBuilder.toString();
                bobTheBuilder = new StringBuilder();
            } else bobTheBuilder.append(current);
        }

        lastname = bobTheBuilder.toString().trim();

        if (firstname == null || firstname.isBlank() || lastname.isBlank())
            throw new IncorrectFormatException("9: Could not parse name property.");
    }

    /**
     *  Used to get the first and last name of the author separated by a single space.
     * @return the name of the author as a String
     */
    public String nameToString()
    {
        return firstname + " " + lastname;
    }

}
