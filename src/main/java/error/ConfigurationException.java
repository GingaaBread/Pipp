package error;

public class ConfigurationException extends PippException {

    public ConfigurationException(String message) {
        super("31" + message);
    }
}
