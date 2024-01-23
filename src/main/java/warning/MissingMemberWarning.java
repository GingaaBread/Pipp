package warning;

import lombok.NonNull;

/**
 * This type of warning is used to warn the user about some kind of member being missing.
 * These are warnings that are usually critical, but are not significant enough to be considered errors.
 * Uses the error warning code '2'.
 * Example: There is no configured author in the document.
 *
 * @version 1.0
 * @since 1.0
 */
public class MissingMemberWarning extends Warning {

    /**
     * Creates a new warning to warn about a missing member
     *
     * @param message  the message that should be shown to the user
     * @param severity the severity of the warning
     */
    public MissingMemberWarning(@NonNull final String message, @NonNull final WarningSeverity severity) {
        super("2" + message, severity);
    }

}
