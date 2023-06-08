package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/**
 *  The paragraph node represents the structural paragraph style configuration
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
public class Paragraph extends Node {

    // Todo: Change
    private String indentation;

    @Override
    public String toString() {
        return "\n\tParagraph{" +
                "indentation='" + indentation + '\'' +
                '}';
    }

    @Override
    protected void checkForWarnings() {}

}
