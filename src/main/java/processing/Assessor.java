package processing;

import error.IncorrectFormatException;
import lombok.Data;
import lombok.NonNull;

/**
 *  The Assessor class represents an assessor of the user's document.
 *  That is a person, typically one of the people evaluating the document, represented by a name and a role.
 *  An assessor requires a name, but the role can be left out as well.
 *
 *  @author Gino Glink
 *  @since 1.0
 *  @version 1.0
 */
@Data
public class Assessor {

    /**
     *  Defines the first name of the assessor. If the name is specified as one string, the first name
     *  includes all characters until the first white space character.
     */
    private String firstname;

    /**
     *  Defines the last name of the assessor. If the name is specified as one string, the first name
     *  includes all characters after the first white space character.
     */
    private String lastname;

    /**
     *  Defines the role of the assessor. If the assessor does not have a role, this value will be null.
     *  Example roles could be "Instructor", "Teacher", "Professor", etc.
     */
    private String role;

    /**
     *  Creates a new assessor by specifying the first and last name separately.
     *  Note that the parameters are taken "as is".
     * @param firstname - the first name of the string as a whole string. For example: "John"
     * @param lastname - the last name of the string as a whole string. For example: "Doe"
     * @param role - the role of the assessor as a whole string if it exists, or null if it does not exist
     */
    public Assessor(@NonNull String firstname, @NonNull String lastname, String role) {
        if (firstname.isBlank() || lastname.isBlank())
            throw new IncorrectFormatException("9: Could not parse name property.");

        this.role = role;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    /**
     *  Creates a new assessor by specifying the name as a whole string.
     *  In that case, all characters until the first white space character are interpreted as the first name,
     *  and all characters after it are interpreted as the last name.
     *  If the first or last names are blank, the compilation fails with an exception
     * @param name - the name of the assessor as a whole string. For example: "John Doe"
     * @param role - the role of the assessor as a whole string if it exists, or null if it does not exist
     */
    public Assessor(@NonNull String name, String role) {
        this.role = role;

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
            throw new IncorrectFormatException("9: Could not parse name property. Have you supplied a first and " +
                    "last name?");
    }

    /**
     *  Used to get the first and last name of the assessor separated by a single space.
     * @return the name of the author as a String
     */
    public String nameToString()
    {
        return firstname + " " + lastname;
    }

}
