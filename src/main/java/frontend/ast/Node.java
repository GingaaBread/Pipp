package frontend.ast;

/**
 *  The Node class represents a node in Pipp's Abstract Syntax Tree (AST)
 *  Nodes have to extend from this class and are forced to check for warnings and errors
 *
 *  @since 1.0
 *  @version 1.0
 */
public abstract class Node {

    /**
     *  Forces all nodes to provide a check if the input provided by the user
     *  could in any way have unexpected results. These warnings should not depend
     *  on the provided style guide.
     */
    protected abstract void checkForWarnings();

    /**
     *  Forces all nodes to provide a check for faulty user input.
     *  These errors should not depend on the provided style guide.
     */
    protected abstract void checkForErrors();

}
