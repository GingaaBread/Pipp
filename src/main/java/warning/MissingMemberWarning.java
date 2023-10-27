package warning;

import lombok.NonNull;

/**
 *  This type of warning is used to warn the user about some kind of member being missing.
 *  These are warnings that are usually critical, but do not warrant being errors.
 *  Example: There is no configured author
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
public class MissingMemberWarning extends Warning {

    /**
     *  Creates a new warning to warn about a missing member
     * @param message the message that should be shown to the user
     * @param severity the severity of the warning
     */
    public MissingMemberWarning(@NonNull final String message, @NonNull final WarningSeverity severity) {
        super("2" + message, severity);
    }

}
