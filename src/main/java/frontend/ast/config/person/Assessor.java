package frontend.ast.config.person;

import error.ConfigurationException;
import error.MissingMemberException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import warning.UnlikelinessWarning;
import warning.WarningQueue;
import warning.WarningSeverity;

import java.util.regex.Pattern;

/**
 * The assessor node contains properties for setting the name and role of the assessor
 * It should always only use either name or first + lastname, but never both
 * Role should never be supplied without having set up the name properly
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Assessor extends Node {

    /**
     * The non-blank title of the assessor provided by the user (Dr., Prof., etc.)
     */
    private String title;

    /**
     * The non-blank name of the assessor provided by the user.
     * If the name is used, the first and last name configurations should not be used as the
     * name is used to create these automatically.
     */
    private String name;

    /**
     * The non-blank first name of the assessor provided by the user.
     * If the first name is used, the last name must also be used, and the name may not be used.
     */
    private String firstname;

    /**
     * The non-blank last name of the assessor provided by the user.
     * If the last name is used, the first name must also be used, and the name may not be used.
     */
    private String lastname;

    /**
     * The non-blank role of the assessor provided by the user.
     * The role could be "Instructor", "Professor", "Examiner", etc.
     */
    private String role;

    /**
     * The non-blank mail address of the assessor.
     * This is null if none is specified.
     */
    private String emailAddress;

    /**
     * The non-blank area of the assessor.
     * This is null if none is specified.
     */
    private String area;

    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    /**
     * The Assessor node produces errors if any field is blank or if an incorrect name configuration is given
     */
    @Override
    public void checkForWarnings() {
        if (emailAddress != null) {
            var mailIsValid = EMAIL_ADDRESS_PATTERN.matcher(emailAddress).matches();
            if (!mailIsValid) {
                WarningQueue.enqueue(new UnlikelinessWarning("6: The email address '[" +
                        emailAddress + "]' seems incorrect.", WarningSeverity.HIGH));
            }
        }

        checkBlankFields();
        if (name == null && firstname == null && lastname == null)
            throw new ConfigurationException("1: An assessor requires a name configuration, but neither " +
                    "name, firstname nor lastname has been configured.");
        else if (name != null && (firstname != null || lastname != null))
            throw new ConfigurationException("2: An assessor can only be given a name configuration " +
                    "OR a firstname and lastname configuration.");
        else if (name == null && firstname != null && lastname == null)
            throw new ConfigurationException("3: An assessor cannot only have a firstname configuration. " +
                    "Either also provide a lastname configuration or only use the name configuration.");
        else if (name == null && firstname == null)
            throw new ConfigurationException("4: An assessor cannot only have a lastname configuration. " +
                    "Either also provide a firstname configuration or only use the name configuration.");
    }

    private void checkBlankFields() {
        if (title != null && title.isBlank() ||
                name != null && name.isBlank() ||
                firstname != null && firstname.isBlank() ||
                lastname != null && lastname.isBlank() ||
                area != null && area.isBlank() ||
                emailAddress != null && emailAddress.isBlank() ||
                role != null && role.isBlank())
            throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }

}
