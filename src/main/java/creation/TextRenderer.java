package creation;

import error.PippException;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import processing.Processor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *  The TextRenderer class is an essential part of the document creation process, used to render formatted text
 *  to the document. It offers an automatic format depending on the used style configuration, which
 *  takes margin, leading, and other factors into consideration, and allows aligning the text to a specific
 *  position within the document. Furthermore, multiple text styles can be used in the same line.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
public class TextRenderer {

    /**
     *  Shorthand method to render text to the current y position of the page.
     *
     * @param textComponentsToRender - the text components that should be rendered on the page
     * @param alignment - the text alignment that should be used for the rendered text
     */
    public static void renderText(@NonNull final List<Text> textComponentsToRender,
                                  @NonNull final TextAlignment alignment) {
        renderText(textComponentsToRender, alignment, PageCreator.currentYPosition);
    }

    /**
     *  Utility method to automatically render normal text aligned left.
     *  Renders the specified text on the current page, using the page's current y position as the starting y
     *  position, and the style's margin as the starting x position. Takes leading into consideration.
     *  Cuts the individual words using the space as the splitting character
     *
     * @param text - the text that should be rendered on the page
     */
    public static void renderLeftAlignedText(@NonNull final String text) {
        renderText(List.of(new Text(text, TextStyle.NORMAL)), TextAlignment.LEFT);
    }

    /**
     * Used to render text components using the specified alignment in the document without registering them as content
     * of the current page. This can be useful to ignore certain texts, such as the page number, from affecting whether
     * there is content on the current page.
     Renders the specified text on the current page, using the page's current y position as the starting y
     *  position, and the style's margin as the starting x position. Takes leading into consideration.
     *  Cuts the individual words using the space as the splitting character.
     *  Note that the method does not change the strings in any way.
     *
     * @param textComponentsToRender - the text components that should be rendered on the page
     * @param alignment - the text alignment that should be used for the rendered text
     */
    public static void renderNoContentText(@NonNull final List<Text> textComponentsToRender,
                                           @NonNull final TextAlignment alignment,
                                           final float startY) {
        try {
            // Calculates the distance of the bottom of one line to the top of the next line
            final float leading = 1.2f * Processor.fontSize * Processor.spacing;

            // Determines how much space the text can take up, by subtracting the margin for both sides
            final float availableWidth = Processor.dimensions.getWidth() - 2 * Processor.margin;
            float maximumWidth = availableWidth;
            float currentLineWidth = 0, lastXOffset = 0;

            // Sets the starting positions to the margin to the left, and the current paper's y position
            float startX = Processor.margin;

            // Creates the content stream with the append mode, which prevents overriding existing streams
            final var contentStream = new PDPageContentStream(PageAssembler.getDocument(), PageCreator.getCurrent(),
                    PDPageContentStream.AppendMode.APPEND, false);

            // Sets up the content stream
            contentStream.setFont(Processor.font, Processor.fontSize);
            contentStream.setStrokingColor(Processor.fontColour);
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY);

            // Contains the currently created lines
            final var textBuilder = new LinkedList<Text>();
            final var rest = new LinkedList<Text>();

            // TODO: IMPLEMENT RIGHT
            // // page.getMediaBox().getWidth() -
            //                            //Processor.numerationMargin - contentWidth;

            for (var textPart : textComponentsToRender) {
                // Divides the text into its words, using exactly one (!) space as the split char
                final var words = textPart.getContent().split(" ");

                // Used to skip the first word when appending a space after all other words
                var isFirstWord = true;

                // Apply the correct font style
                if (textPart.getStyle() == TextStyle.ITALIC)
                    contentStream.setFont(Processor.usedStyleGuide.emphasisedFont(), Processor.fontSize);
                else if (textPart.getStyle() == TextStyle.NORMAL)
                    contentStream.setFont(Processor.usedStyleGuide.font(), Processor.fontSize);

                // Tries to check if the next word can be rendered in the current line or if it does not fit
                for (String word : words) {
                    // Calculate the width of the entire line with a space and the current word
                    final float wordWidth = Processor.font.getStringWidth((isFirstWord ? "" : " ") + word) /
                            1000 * Processor.fontSize;

                    // Check if the next line does not fit into the current page anymore
                    if (PageCreator.currentYPosition < Processor.margin) {
                        if (!textBuilder.isEmpty()) {
                            final var textSize = textBuilder.size();
                            for (int i = 0; i < textSize; i++) {
                                rest.addLast(textBuilder.remove());
                                rest.addLast(new Text(" ", textPart.getStyle()));
                            }
                        }

                        rest.addLast(new Text(word + " ", textPart.getStyle()));
                    }
                    // If the word does not fit in the current line, render the line and start a new line
                    else if (wordWidth > maximumWidth) {
                        // When trying to center the text, we first need to calculate the total width for our offset
                        if (alignment == TextAlignment.CENTER) {
                            final float xOffset = (PageCreator.getCurrent().getMediaBox().getWidth() -
                                    currentLineWidth) / 2 - Processor.margin;
                            contentStream.newLineAtOffset(-lastXOffset, 0);
                            contentStream.newLineAtOffset(xOffset, 0);
                            lastXOffset = xOffset;
                        }

                        final var textSize = textBuilder.size();
                        for (int i = 0; i < textSize; i++) {
                            var text = textBuilder.remove();

                            // Apply the correct font style
                            if (text.getStyle() == TextStyle.ITALIC)
                                contentStream.setFont(Processor.usedStyleGuide.emphasisedFont(), Processor.fontSize);
                            else if (text.getStyle() == TextStyle.NORMAL)
                                contentStream.setFont(Processor.usedStyleGuide.font(), Processor.fontSize);

                            var textToRender = text.getContent();

                            contentStream.showText(textToRender);
                        }

                        maximumWidth = availableWidth;
                        currentLineWidth = wordWidth;
                        textBuilder.clear();

                        // Check if the word is too long to fit in one line
                        if (wordWidth > maximumWidth) {
                            var currentLine = word;
                            var nextLineWidth = wordWidth;

                            while (nextLineWidth > maximumWidth) {
                                var stringForNextLine = new StringBuilder();

                                while (nextLineWidth > maximumWidth) {
                                    stringForNextLine.append(currentLine.charAt(currentLine.length() - 1));
                                    currentLine = currentLine.substring(0, currentLine.length() - 1);
                                    nextLineWidth = Processor.font.getStringWidth(currentLine) / 1000 *
                                            Processor.fontSize;
                                }

                                contentStream.showText(currentLine);
                                contentStream.newLineAtOffset(0, -leading);
                                PageCreator.currentYPosition -= leading;

                                currentLine = stringForNextLine.reverse().toString();
                                nextLineWidth = Processor.font.getStringWidth(currentLine) / 1000 *
                                        Processor.fontSize;
                                stringForNextLine.setLength(0);
                            }

                            contentStream.showText(currentLine);
                            contentStream.newLineAtOffset(0, -leading);
                            PageCreator.currentYPosition -= leading;
                        } else {
                            PageCreator.currentYPosition -= leading;
                            contentStream.newLineAtOffset(0, -leading);

                            textBuilder.addLast(new Text(word + " ", textPart.getStyle()));
                            maximumWidth -= wordWidth;
                        }
                        // The word does still fit in the current line
                    } else {
                        // Add the word to the builder and add a space
                        // character if the word is not the first word in the line
                        if (isFirstWord) {
                            textBuilder.addLast(new Text(word, textPart.getStyle()));
                            isFirstWord = false;
                        } else textBuilder.addLast(new Text(" " + word, textPart.getStyle()));

                        currentLineWidth += wordWidth;
                        maximumWidth -= wordWidth;
                    }
                }
            }

            // If there is still one last remaining line, it should be displayed
            if (rest.isEmpty()) {
                // When trying to center the text, we first need to calculate the total width for our offset
                if (alignment == TextAlignment.CENTER) {
                    final float xOffset = (PageCreator.getCurrent().getMediaBox().getWidth() - currentLineWidth) / 2
                            - Processor.margin;
                    contentStream.newLineAtOffset(xOffset, 0);
                }

                final var textSize = textBuilder.size();
                for (int i = 0; i < textSize; i++) {
                    var text = textBuilder.remove();

                    // Apply the correct font style
                    if (text.getStyle() == TextStyle.ITALIC)
                        contentStream.setFont(Processor.usedStyleGuide.emphasisedFont(), Processor.fontSize);
                    else if (text.getStyle() == TextStyle.NORMAL)
                        contentStream.setFont(Processor.usedStyleGuide.font(), Processor.fontSize);

                    // Display the text component
                    contentStream.showText(text.getContent());
                }

                PageCreator.currentYPosition -= leading;
            }

            // Wrap up the stream
            contentStream.endText();
            contentStream.close();

            // If there is still remaining text that did not fit on the page, restart the method on a new page
            if (!rest.isEmpty()) {
                PageCreator.createNewPage();

                // Bundles the words together depending on the style
                var collectedRest = new LinkedList<Text>();
                var itemBuilder = new StringBuilder();
                TextStyle currentStyle = rest.get(0).getStyle();
                for (Text text : rest) {
                    if (text.getStyle() != currentStyle) {
                        collectedRest.addLast(new Text(itemBuilder.toString(), currentStyle));
                        itemBuilder.setLength(0);
                        currentStyle = text.getStyle();
                    } else itemBuilder.append(text.getContent());
                }

                if (!itemBuilder.isEmpty()) {
                    collectedRest.addLast(new Text(itemBuilder.toString(), currentStyle));
                    itemBuilder.setLength(0);
                }

                renderText(collectedRest, alignment);
            }
        } catch (IOException e) {
            throw new PippException("Could not print text to the current line");
        }
    }

    /**
     *  Base method to render text components in the specified alignment in the document.
     *  Renders the specified text on the current page, using the page's current y position as the starting y
     *  position, and the style's margin as the starting x position. Takes leading into consideration.
     *  Cuts the individual words using the space as the splitting character.
     *  Note that the method does not change the strings in any way.
     *
     * @param textComponentsToRender - the text components that should be rendered on the page
     * @param alignment - the text alignment that should be used for the rendered text
     */
    public static void renderText(@NonNull final List<Text> textComponentsToRender,
                                  @NonNull final TextAlignment alignment,
                                  final float startY) {
        renderNoContentText(textComponentsToRender, alignment, startY);

        // Now that there has been at least one line rendered on the current page, update the flag
        PageCreator.currentPageIsEmpty = false;
    }

}
