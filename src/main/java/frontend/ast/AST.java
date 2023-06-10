package frontend.ast;

import frontend.ast.config.Configuration;
import lombok.Getter;
import lombok.NonNull;

import java.util.Stack;

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
     *  The document body consists of nodes given by the user.
     *  It is the job of the {@link processing.Processor} to find whether the input is legal, the AST only collects
     *  the nodes specified one after the other.
     *  To push a new node to the stack, use the push-method of this class.
     */
    private final Stack<Node> documentBody = new Stack<>();

    /**
     *  Adds the specified document node to the top of the stack.
     *  The stack contains the body of the document.
     *
     * @param node - the node that should be added to the top of the stack
     */
    @NonNull
    public void pushDocumentNode(final Node node) {
        documentBody.add(node);
    }

    /**
     * Provides a textual representation of the AST
     *
     * @return - the properties of the AST class as a string
     */
    @Override
    public String toString() {
        return "AST{" +
                "\nconfiguration=" + configuration +
                ",\ndocumentBody=" + documentBody +
                '}';
    }

    /**
     *  Checks for warnings in all child nodes.
     */
    public void checkForWarnings() {
        configuration.checkForWarnings();
    }

}
