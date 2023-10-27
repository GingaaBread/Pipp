package warning;

/**
 *  Determines how severe a warning is.
 *  Some warnings may be of low severity (maybe an obsolete attribute is used), whereas others
 *  may be critical to rendering a proper document (maybe multiple authors share the same name, which could
 *  indicate a copy and paste error)
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
public enum WarningSeverity {

    /**
     *  Determines that the warning is critical to the proper render of the document, but is not enough to warrant
     *  being an error.
     *  Example: multiple authors share the same name, which might indicate a copy and paste error
     */
    CRITICAL,

    /**
     *  Determines that the warning is severe, but not critical to render the document properly.
     *  Example: MLA demands the use of a header, but a title page is specified by the user.
     */
    HIGH,

    /**
     *  Determines that the warning is not very severe, but could be useful to the user.
     *  Example: Setting an unrealistic large font size.
     */
    LOW

}
