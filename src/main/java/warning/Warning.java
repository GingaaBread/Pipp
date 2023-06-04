package warning;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Warning {
    private WarningSeverity severity;

    private String message;

    public Warning(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "(" + severity + "): " + message;
    }
}
