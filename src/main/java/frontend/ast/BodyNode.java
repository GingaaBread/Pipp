package frontend.ast;

/**
 * The body node class provides a base class for nodes that are part of the document body.
 * That includes images, blank pages, titles, headers, etc.
 * This forces children to implement a method that handles the document creation when the body node is handled.
 *
 * @version 1.0
 * @since 1.0
 */
public abstract class BodyNode extends Node {

    /**
     * Forces bode nodes to deal with the document creation when handled in the backend.
     */
    public abstract void handleBodyElement();

}
