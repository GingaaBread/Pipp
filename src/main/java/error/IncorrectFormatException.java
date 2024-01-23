package error;

/**
 * The incorrect format exception type is used for errors that are all caused by a certain format not being met.
 * An example use case would be expecting a number, but given a word, instead.
 *
 * @version 1.0
 * @since 1.0
 */
public class IncorrectFormatException extends PippException {

    /**
     * Provides access to the second error message shown when there is no non-negative decimal number given.
     * Therefore, the string does not have to be copy-pasted over and over again.
     */
    public static final String ERR_MSG_2 = "2: Non-negative decimal expected.";

    /**
     * Provides access to the 15 error message shown when a positive integer ending with the percentage character is
     * expected. Therefore, the string does not have to be copy-pasted over and over again.
     */
    public static final String ERR_MSG_3 = "15: Positive integer percentage expected.";

    /**
     * Provides access to the 6 error message shown when a content alignment type (center, left, right, ...) is
     * expected. Therefore, the string does not have to be copy-pasted over and over again.
     */
    public static final String ERR_MSG_6 = "6: Content alignment type expected.";

    /**
     * The incorrect format exception type uses the error code 33.
     *
     * @param message the message that should be printed
     */
    public IncorrectFormatException(String message) {
        super("33" + message);
    }

}
