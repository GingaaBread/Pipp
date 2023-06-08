package processing;

/**
 *  The Whitespace Allowance Type determines whether the user is allowed to use whitespace characters
 *  in sentences or if it should be automatically removed.
 *  A style guide may choose to allow or forbid the use of whitespace, or allow whitespace only if it has
 *  been escaped. If unsure, allowing escaped whitespace is recommended.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
public enum WhitespaceAllowanceType {

    /**
     *  YES allows using whitespace in sentences without Pipp removing the whitespace or printing warnings
     */
    YES,

    /**
     * ESCAPED allows using whitespace in sentences, but only if the whitespace has been escaped.
     * Unescaped whitespace will be removed by Pipp.
     */
    ESCAPED,

    /**
     *  NO does not allow whitespace. Whitespace will be automatically removed by Pipp, and warnings will be
     *  printed, reminding the user that whitespace is not allowed.
     */
    NO

}
