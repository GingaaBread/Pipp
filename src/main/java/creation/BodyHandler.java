package creation;

import error.PippException;
import processing.Processor;
import processing.StructureType;

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
        // Iterate over all body elements
        while (!Processor.documentBody.isEmpty()) {
            final var element = Processor.documentBody.pop();

            // Check if the element is a simple structure that should be rendered
            if (element instanceof final StructureType structure) {
                // Apply the proper structure stamp
                switch (structure) {
                    case HEADER -> HeaderStamp.renderHeader();
                    case TITLE -> TitleStamp.renderTitle();
                    case BLANKPAGE ->  PageCreator.createBlankPage();
                    default -> throw new PippException("Structure is not yet implemented!");
                }
            } else throw new PippException("Document type " + element + " is not yet implemented!");
        }
    }

}
