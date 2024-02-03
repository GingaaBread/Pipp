package frontend.ast.paragraph;

import creation.content.text.Text;
import frontend.ast.Node;

/**
 * Used as a base class for all AST nodes that are paragraph instructions.
 * Used by the {@link Paragraph} node to list all sub instructions in that paragraph.
 *
 * @version 1.0
 * @since 1.0
 */
public abstract class ParagraphInstruction extends Node {

    /**
     * Forces paragraph instructions to define how they should be rendered on the screen when created.
     *
     * @return the instruction as a text consisting of a font, font size, font colour and content.
     */
    public abstract Text[] toTextComponent();

}
