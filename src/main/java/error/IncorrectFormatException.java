package error;

public class IncorrectFormatException extends PippException {
    public IncorrectFormatException(String message) {
        super("33" + message);
    }
}
