package frontend.ast;

import frontend.ast.config.Configuration;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.LinkedList;

/**
 * The AST represents Pipp's Abstract Syntax Tree (AST).
 * The tree consists of a list of the provided user input.
 *
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
@Getter
@ToString
public class AST {

    /**
     * The configurations provided by the user input
     */
    private final Configuration configuration = new Configuration();

    /**
     * The document body consists of nodes given by the user.
     * It is the job of the {@link processing.Processor} to find whether the input is legal, the AST only collects
     * the nodes specified one after the other.
     */
    private final LinkedList<BodyNode> documentBody = new LinkedList<>();

    private final LinkedList<BibliographySource> bibliographySources = new LinkedList<>();

    public void includeBibliographySource(@NonNull final BibliographySource source) {
        bibliographySources.addLast(source);
    }

    /**
     * Adds the specified document node to the top of the stack.
     * The stack contains the body of the document.
     *
     * @param node - the node that should be added to the top of the stack
     */
    public void pushDocumentNode(@NonNull final BodyNode node) {
        documentBody.addLast(node);
    }

    /**
     * Checks for warnings in all child nodes.
     */
    public void checkForWarnings() {
        configuration.checkForWarnings();
    }

}
