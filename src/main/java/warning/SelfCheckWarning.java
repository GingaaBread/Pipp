package warning;

import lombok.NonNull;

/**
 * This warning indicates that the user should self-check content of the document.
 * It uses the error code '4'.
 * @version 1.0
 * @since 1.0
 */
public class SelfCheckWarning extends Warning {

    /**
     *  Creates a new warning that indicates that the user should self-check content of the document
     * @param message the message that should be shown to the user
     * @param severity the severity of the warning
     */
    public SelfCheckWarning(@NonNull final String message, @NonNull final WarningSeverity severity) {
        super("4" + message, severity);
    }

}
