package error;

public class MissingMemberException extends PippException {
    public MissingMemberException(String message) {
        super("32" + message);
    }
}
