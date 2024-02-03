package creation.stamp;

import creation.content.ContentAlignment;
import creation.page.PageCreator;
import creation.content.text.TextRenderer;
import frontend.ast.paragraph.ParagraphInstruction;
import lombok.NonNull;
import processing.Processor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to add text paragraphs to the document.
 * This takes a list of paragraph instructions (sentences, emphasis, work references, etc.) and renders them
 * in the document. It also takes the indentation margin of the used style guide into consideration.
 *
 * @version 1.0
 * @since 1.0
 */
public class ParagraphStamp {

    /**
     * Prevents instantiation of this class
     */
    private ParagraphStamp() {
        throw new UnsupportedOperationException("Should not instantiate helper class");
    }

    /**
     * Renders all instructions (sentences, work references, emphasis, etc.) of this paragraph on the current page
     * and indents the first sentence by the amount determined by the used style guide.
     *
     * @param contentToRender a non-null list of all instructions that make up the paragraph.
     */
    public static void renderParagraph(@NonNull final List<ParagraphInstruction> contentToRender) {
        final var instructionTexts = contentToRender
                .stream()
                .flatMap(instruction -> Arrays.stream(instruction.toTextComponent()))
                .collect(Collectors.toCollection(LinkedList::new));

        TextRenderer.renderIndentedText(instructionTexts, ContentAlignment.LEFT, PageCreator.currentYPosition,
                Processor.getParagraphIndentation());
    }

}
