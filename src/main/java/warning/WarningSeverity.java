package warning;

/**
 * Determines how severe a warning is.
 * Certain warnings may be of low severity to indicate lesser significance, whereas others
 * may be more critical and therefore indicating much higher significance.
 *
 * @version 1.0
 * @since 1.0
 */
public enum WarningSeverity {

    /**
     * Determines that the warning is critical to the proper render of the document, but is not enough to warrant
     * being defined as an error.
     * Example: multiple authors share the same name, which indicates a copy-paste-error.
     */
    CRITICAL,

    /**
     * Determines that the warning is severe, but not critical to render the document properly.
     * Example: MLA demands the use of a header, but a title page is specified by the user.
     */
    HIGH,

    /**
     * Determines that the warning is not very severe, but could be useful to the user.
     * Example: Setting an unrealistic large font size.
     */
    LOW

}
