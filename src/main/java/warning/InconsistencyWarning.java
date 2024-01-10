package warning;

import lombok.NonNull;

/**
 *  This type of warning warns the user about some form of inconsistency.
 *  An example could be using both inches and millimeters in the user's configuration.
 * @version 1.0
 * @since 1.0
 */
public class InconsistencyWarning extends Warning {

    /**
     *  Creates a new warning that indicates some form of inconsistency
     * @param message the message that should be shown to the user
     * @param severity the severity of the warning
     */
    public InconsistencyWarning(@NonNull final String message, @NonNull final WarningSeverity severity) {
        super("1" + message, severity);
    }

}
