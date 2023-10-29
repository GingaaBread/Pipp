package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *  The paragraph node represents the structural paragraph style configuration
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class Paragraph extends Node {

    private String indentation;

    @Override
    protected void checkForWarnings() {}

}
