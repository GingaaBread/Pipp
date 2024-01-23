package warning;

import lombok.NonNull;

/**
 * This type of warning is used to warn the user about something being rather unlikely.
 * Uses the warning error code '3'.
 * Example: two authors share the same name, which could happen, but is likely an indication of a copy-paste-error.
 *
 * @version 1.0
 * @since 1.0
 */
public class UnlikelinessWarning extends Warning {

    /**
     * Creates a new warning indicating that something is rather unlikely
     *
     * @param message  the message that should be shown to the user
     * @param severity the severity of warning
     */
    public UnlikelinessWarning(@NonNull final String message, @NonNull final WarningSeverity severity) {
        super("3" + message, severity);
    }

}
