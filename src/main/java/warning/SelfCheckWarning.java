package warning;

import lombok.NonNull;

/**
 * This warning indicates that the user should self-check content of the document.
 * It uses the error code '4'.
 * Example: The style guide leaves it to the user whether they should use emphasised text or not. The compiler will
 * therefore allow the use of emphasis, but a self-check warning will be displayed.
 *
 * @version 1.0
 * @since 1.0
 */
public class SelfCheckWarning extends Warning {

    /**
     * Provides access to the first warning message shown when it is up to user if emphasis should be used or not.
     * Therefore, the string does not have to be copy-pasted over and over again.
     */
    public static String WARNING_MSG_1 = "1: You are using a style guide that recommends" +
            " only using emphasis if absolutely necessary. Make sure that is the case.";

    /**
     * Creates a new warning that indicates that the user should self-check content of the document
     *
     * @param message  the message that should be shown to the user
     * @param severity the severity of the warning
     */
    public SelfCheckWarning(@NonNull final String message, @NonNull final WarningSeverity severity) {
        super("4" + message, severity);
    }

}
