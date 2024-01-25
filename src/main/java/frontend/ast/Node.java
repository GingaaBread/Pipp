package frontend.ast;

/**
 * The Node class represents a node in Pipp's Abstract Syntax Tree (AST)
 * Nodes have to extend from this class and are forced to check for warnings and errors
 *
 * @version 1.0
 * @since 1.0
 */
public abstract class Node {

    /**
     * Forces all nodes to provide a check if the input provided by the user
     * could in any way have unexpected results. These warnings should not depend
     * on the provided style guide. Note that all nodes only check for warnings.
     * Errors are checked during the processing phase by the {@link processing.Processor}
     */
    public abstract void checkForWarnings();

}
