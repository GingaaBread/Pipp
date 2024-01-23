package creation;

import frontend.ast.BodyNode;
import processing.Processor;

/**
 * The BodyHandler class is used to handle all body elements of the document.
 * Body elements are all elements that are not part of the configuration.
 * In example way a body element could "handle" being rendered is using the ImageRenderer class to render an image.
 *
 * @version 1.0
 * @since 1.0
 */
public class BodyHandler {

    /**
     * Prevents instantiation
     */
    private BodyHandler() {
        throw new UnsupportedOperationException("Should not instantiate static helper class");
    }

    /**
     * Tries to handle all body elements and throws an exception if any has not yet been implemented
     */
    public static void handleAll() {
        Processor.getDocumentBody().forEach(BodyNode::handleBodyElement);
    }

}
