package processing;

import lombok.Data;

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
     *  Creates a new assessor specifying the name as a whole string.
     *  In that case, all characters until the first white space character are interpreted as the first name,
     *  and all characters after it are interpreted as the last name.
     *  If the first or last names are blank, the compilation fails with an exception
     * @param name - the name of the assessor as a whole string. For example: "John Doe"
     */
    public Assessor(String name) {
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
            throw new IllegalArgumentException("Specified name does not yield first and last name");
    }

}
