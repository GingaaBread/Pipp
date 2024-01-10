package creation;

import frontend.ast.paragraph.ParagraphInstruction;
import lombok.NonNull;
import processing.Processor;

import java.util.LinkedList;

public class ParagraphStamp {

    public static void renderParagraph(@NonNull final LinkedList<ParagraphInstruction> contentToRender) {
        // TODO: Paragraph indent

        final LinkedList<Text> texts = new LinkedList<>();
        for (ParagraphInstruction instruction : contentToRender) {
            texts.add(Processor.paragraphInstructionToText(instruction));
        }

        TextRenderer.renderText(texts, TextAlignment.LEFT);
    }

}
