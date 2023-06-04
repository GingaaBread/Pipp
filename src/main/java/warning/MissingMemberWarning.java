package warning;

public class MissingMemberWarning extends Warning {
    public MissingMemberWarning(String message) {
        super("2" + message);
        super.setSeverity(WarningSeverity.CRITICAL);
    }
}
