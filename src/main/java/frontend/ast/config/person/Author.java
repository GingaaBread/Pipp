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
 * The author node represents an author of the document
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Author extends Node {

    /**
     * The non-blank title of the author specified by the user (Prof., Dr., etc.)
     */
    private String title;

    /**
     * The non-blank name of the author specified by the user.
     * If used, the first and last name properties may not be used as the name property is used to
     * generate these automatically
     */
    private String name;

    /**
     * The non-blank first name of the author specified by the user.
     * If used, the last name property must be used, and the name property may not be used.
     */
    private String firstname;

    /**
     * The non-blank last name of the author specified by the user.
     * If used, the first name property must be used, and the name property may not be used.
     */
    private String lastname;

    /**
     * The non-blank identification number/string specified by the user.
     * This can be used to identify the author via the use of an ID field
     */
    private String id;

    /**
     * The non-blank mail address of the author.
     * This is null if none is specified.
     */
    private String emailAddress;

    /**
     * The non-blank area of the author.
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
     * The author node produces warnings if the name is not set up correctly or if there are blank fields
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

        if (name == null && firstname == null && lastname == null)
            throw new ConfigurationException("6: An author requires a name configuration, but neither " +
                    "name, firstname nor lastname has been configured.");
        else if (name != null && (firstname != null || lastname != null))
            throw new ConfigurationException(ConfigurationException.ERR_MSG_7);
        else if (name == null && firstname != null && lastname == null)
            throw new ConfigurationException("8: An author cannot only have a firstname configuration. " +
                    "Either also provide a lastname configuration or only use the name configuration.");
        else if (name == null && firstname == null)
            throw new ConfigurationException("9: An author cannot only have a lastname configuration. " +
                    "Either also provide a firstname configuration or only use the name configuration.");
        else checkBlankFields();
    }

    /**
     * Throws a member missing exception if any field is blank
     */
    private void checkBlankFields() {
        if (title != null && title.isBlank() ||
                name != null && name.isBlank() ||
                firstname != null && firstname.isBlank() ||
                lastname != null && lastname.isBlank() ||
                area != null && area.isBlank() ||
                emailAddress != null && emailAddress.isBlank() ||
                id != null && id.isBlank())
            throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }

}
