package warning;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Used as a base class that all types of Pipp warnings can use to warn the user.
 * All warnings are defined by an individual severity and a message that are supposed to inform the user about the
 * importance and origin of the warning.
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public abstract class Warning {

    /**
     * Determines how severe the warning is.
     * Some warnings may be more important to the user than others.
     */
    private WarningSeverity severity;

    /**
     * This is the message that will be shown to the user
     */
    private String message;

    /**
     * Creates a new warning, which requires some kind of message that will be shown to the user
     *
     * @param message  the message that should be shown to the user
     * @param severity the severity of the warning
     */
    protected Warning(@NonNull final String message, @NonNull final WarningSeverity severity) {
        this.message = message;
        this.severity = severity;
    }

    /**
     * Formats the warning so that it displays the severity as a string and then the warning
     *
     * @return the warning as a String
     */
    @Override
    public String toString() {
        return "(" + severity + "): " + message;
    }

}
