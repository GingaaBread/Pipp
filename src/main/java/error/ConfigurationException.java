package error;

/**
 * The configuration exception type is used for errors that are all caused by a certain configuration.
 * An example use case would be not allowing the use of emphasis, but trying to use it, nonetheless.
 *
 * @version 1.0
 * @since 1.0
 */
public class ConfigurationException extends PippException {

    /**
     * Provides access to the 9 error message shown when the user is not allowed to use emphasis, but does.
     * Therefore, the string does not have to be copy-pasted over and over again.
     */
    public static String ERR_MSG_9 = "9: The style guide does not allow the use of emphasis, but you are " +
            "trying to emphasise text nonetheless.";

    /**
     * The incorrect format exception type uses the error code 33.
     *
     * @param message the message that should be printed
     */
    public ConfigurationException(String message) {
        super("31" + message);
    }
}
