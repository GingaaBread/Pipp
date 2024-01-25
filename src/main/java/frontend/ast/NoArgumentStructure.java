package frontend.ast;

import creation.HeaderStamp;
import creation.PageCreator;
import creation.TitleStamp;
import error.PippException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import processing.Processor;
import processing.StructureType;

/**
 * The no argument structure node contains a structure, which receives no arguments.
 * This is because the structure is used to build content based on the user's configurations.
 *
 * @version 1.0
 * @since 1.0
 */
@Setter
@Getter
@AllArgsConstructor
@ToString
public class NoArgumentStructure extends BodyNode {

    /**
     * The type of the structure.
     * For example, "bibliography" is a structure with no arguments.
     */
    private StructureType type;

    /**
     * The No argument structure node does not produce errors
     */
    @Override
    public void checkForWarnings() {
        // Does not produce errors
    }

    /**
     * Determines what stamps or renders should be applied when this element is rendered on the document
     */
    @Override
    public void handleBodyElement() {
        switch (type) {
            case TITLE -> TitleStamp.renderTitle();
            case HEADER -> HeaderStamp.renderHeader();
            case BLANKPAGE -> PageCreator.createBlankPage(
                    Processor.getDocumentBody().getLast() == this);
            default -> throw new PippException("Document type " + type + " is not yet implemented!");
        }
    }

}
