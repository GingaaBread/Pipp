package warning;

import lombok.NonNull;

/**
 *  This type of warning is used to warn the user about something being rather unlikely.
 *  Example: two authors share the same name, which could happen, but is probably an indication of a copy
 *  and paste error.
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
public class UnlikelinessWarning extends Warning {

    /**
     *  Creates a new warning indicating that something is rather unlikely
     * @param message the message that should be shown to the user
     * @param severity the severity of warning
     */
    public UnlikelinessWarning(@NonNull final String message, @NonNull final WarningSeverity severity) {
        super("3" + message, severity);
    }

}
