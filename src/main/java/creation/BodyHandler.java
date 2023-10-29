package creation;

import frontend.ast.BodyNode;
import processing.Processor;

/**
 *  The BodyHandler class is used to handle all body elements of the document.
 *  Body elements are all elements that are not part of the configuration.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
public class BodyHandler {

    /**
     *  Tries to handle all body elements and throws an exception if any has not yet been implemented
     */
    public static void handle() {
        Processor.documentBody.forEach(BodyNode::handleBodyElement);
    }

}
