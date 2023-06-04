package frontend.ast.config.style;

import error.IncorrectFormatException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/**
 *  The paragraph node represents the structural paragraph style configuration
 *
 *  @since 1.0
 *  @version 1.0
 */
@Getter
@Setter
public class Paragraph extends Node {
    private String indentation;

    @Override
    public String toString() {
        return "\n\tParagraph{" +
                "indentation='" + indentation + '\'' +
                '}';
    }

    @Override
    protected void checkForWarnings() {}

    @Override
    protected void checkForErrors() {
        if (indentation != null) {
            try {
                if (Double.parseDouble(indentation) < 0)
                    throw new IncorrectFormatException("2: Non-negative decimal expected.");
            } catch (Exception e) {
                throw new IncorrectFormatException("2: Non-negative decimal expected.");
            }
        }
    }
}
