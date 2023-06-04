package warning;

public class UnlikelinessWarning extends Warning {
    public UnlikelinessWarning(String message) {
        super("3" + message);
        super.setSeverity(WarningSeverity.LOW);
    }
}
