package error;

/**
 * The missing member exception type is used for errors that are all caused by some kind of member being missing.
 * An example use case would be the absence of a meaningful configuration, and instead only supplying a blank string.
 *
 * @version 1.0
 * @since 1.0
 */
public class MissingMemberException extends PippException {

    /**
     * Provides access to the first error message shown when a text component only contains whitespace.
     * Therefore, the string does not have to be copy-pasted over and over again.
     */
    public static final String ERR_MSG_1 = "1: A text component cannot be blank";

    /**
     * The missing member exception type uses the error code 32.
     *
     * @param message the message that should be printed to the screen.
     */
    public MissingMemberException(String message) {
        super("32" + message);
    }

}
