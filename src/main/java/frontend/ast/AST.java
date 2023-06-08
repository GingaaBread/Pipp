package frontend.ast;

import frontend.ast.config.Configuration;
import lombok.Getter;

/**
 *  The AST represents Pipp's Abstract Syntax Tree (AST).
 *  The tree consists of a list of the provided user input.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
public class AST {

    /**
     *  The configurations provided by the user input
     */
    private final Configuration configuration = new Configuration();

    /**
     *  Provides a textual representation of the AST
     *
     * @return - the properties of the AST class as a string
     */
    @Override
    public String toString() {
        return "AST:\n" + configuration;
    }

    /**
     *  Checks for warnings in all child nodes.
     */
    public void checkForWarnings() {
        configuration.checkForWarnings();
    }

}
