package creation;

import error.PippException;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import processing.Processor;

import java.io.IOException;
import java.util.LinkedList;

public class LineFactory {

    @NonNull
    public void printText(final PDPage page, final String text) {
        try {
            final float leading = 1.2f * Processor.fontSize * Processor.spacing;
            final float availableWidth = Processor.dimensions.getWidth() - 2 * Processor.margin;
            final float startX = Processor.margin, startY = PDFCreator.currentYPosition;
            final var contentStream = new PDPageContentStream(PDFCreator.document, page,
                    PDPageContentStream.AppendMode.APPEND, false);

            contentStream.setFont(Processor.font, Processor.fontSize);
            contentStream.setStrokingColor(Processor.fontColour);
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY);
            contentStream.newLineAtOffset(0,0);

            final var words = text.split(" ");
            final var lineBuilder = new StringBuilder();
            var isFirstWord = true;

            for (String word : words) {
                final float width = Processor.font.getStringWidth(lineBuilder + " " + word) / 1000
                        * Processor.fontSize;

                if (width > availableWidth) {
                    contentStream.showText(lineBuilder.toString());
                    contentStream.newLineAtOffset(0, -leading);
                    lineBuilder.setLength(0);
                    lineBuilder.append(word);
                } else {
                    if (isFirstWord) {
                        lineBuilder.append(word);
                        isFirstWord = false;
                    } else lineBuilder.append(" ").append(word);
                }
            }
            contentStream.showText(lineBuilder.toString());

            contentStream.endText();
            contentStream.close();
        } catch (IOException e) {
            throw new PippException("Could not print text to the current line");
        }
    }
}
