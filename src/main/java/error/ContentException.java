package error;

public class ContentException extends PippException {
    public ContentException(String message) {
        super("34" + message);
    }
}
