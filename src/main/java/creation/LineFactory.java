package creation;

import error.PippException;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import processing.Processor;

import java.io.IOException;

public class LineFactory {

    /**
     *  Renders the specified text on the current page, using the page's current y position as the starting y
     *  position, and the style's margin as the starting x position. Takes leading into consideration.
     *  Cuts the individual words using the space as the splitting character
     *
     * @param text - the text that should be rendered on the page
     */
    @NonNull
    public static void renderLeftAlignedText(final String text) {
        try {
            // Calculates the distance of the bottom of one line to the top of the next line
            final float leading = 1.2f * Processor.fontSize * Processor.spacing;

            // Determines how much space the text can take up, by subtracting the margin for both sides
            final float availableWidth = Processor.dimensions.getWidth() - 2 * Processor.margin;

            // Sets the starting positions to the margin to the left, and the current paper's y position
            final float startX = Processor.margin, startY = PageFactory.currentYPosition;

            // Creates the content stream with the append mode, which allows overriding existing streams
            final var contentStream = new PDPageContentStream(PageAssembler.getDocument(), PageFactory.getCurrent(),
                    PDPageContentStream.AppendMode.APPEND, false);

            // Sets up the content stream
            contentStream.setFont(Processor.font, Processor.fontSize);
            contentStream.setStrokingColor(Processor.fontColour);
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY);
            contentStream.newLineAtOffset(0,0);

            // Divides the text into its words, using exactly one (!) space as the split char
            final var words = text.split(" ");

            // Contains the currently created lines
            final var lineBuilder = new StringBuilder();
            final var rest = new StringBuilder();

            // Used to skip the first word when appending a space after all other words
            var isFirstWord = true;

            // Tries to check if the next word can be rendered in the current line or if it does not fit
            for (String word : words) {
                // Calculate the width of the entire line with a space and the current word
                final float width = Processor.font.getStringWidth(lineBuilder + " " + word) / 1000
                        * Processor.fontSize;

                // Check if the next line does not fit into the current page anymore
                if (PageFactory.currentYPosition < Processor.margin) {
                    if (!lineBuilder.isEmpty()) {
                        rest.append(lineBuilder);
                        rest.append(" ");
                        lineBuilder.setLength(0);
                    }

                    rest.append(word);
                    rest.append(" ");
                }
                // If the word does not fit in the current line, render the line and start a new line
                // TODO: Handle words that are too long to fit in one line (cut them off?)
                else if (width > availableWidth) {
                    contentStream.showText(lineBuilder.toString());
                    PageFactory.currentYPosition -= leading;

                    contentStream.newLineAtOffset(0, -leading);
                    lineBuilder.setLength(0);
                    lineBuilder.append(word);
                } else {
                    // If the word does fit in the current line, add it to the builder and add a space
                    // character if the word is not the first word in the line
                    if (isFirstWord) {
                        lineBuilder.append(word);
                        isFirstWord = false;
                    } else lineBuilder.append(" ").append(word);
                }
            }

            // There will be a "rest" line that needs to be rendered at the end
            if (rest.length() == 0) contentStream.showText(lineBuilder.toString());

            // Wrap up the stream
            contentStream.endText();
            contentStream.close();

            if (!rest.isEmpty()) {
                PageFactory.createNewPage();
                renderLeftAlignedText(rest.toString());
            }
        } catch (IOException e) {
            throw new PippException("Could not print text to the current line");
        }
    }

}
