package warning;

public class InconsistencyWarning extends Warning {
    public InconsistencyWarning(String message) {
        super("1" + message);
        super.setSeverity(WarningSeverity.HIGH);
    }
}
