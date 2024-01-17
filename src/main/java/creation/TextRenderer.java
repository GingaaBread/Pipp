package creation;

import error.PippException;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import processing.Processor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * The TextRenderer class is an essential part of the document creation process, used to render formatted text
 * to the document. It offers an automatic format depending on the used style configuration, which
 * takes margin, leading, and other factors into consideration, and allows aligning the text to a specific
 * position within the document. Furthermore, multiple text styles can be used in the same line.
 *
 * @version 1.0
 * @since 1.0
 */
public class TextRenderer {

    /**
     * Prevents object instantiation
     */
    private TextRenderer() {
        throw new UnsupportedOperationException("Should not instantiate a utility class");
    }

    /**
     * Shorthand method to render text to the current y position of the page.
     *
     * @param textComponentsToRender the text components that should be rendered on the page
     * @param alignment              the text alignment that should be used for the rendered text
     */
    public static void renderText(@NonNull final List<Text> textComponentsToRender,
                                  @NonNull final ContentAlignment alignment) {
        renderText(textComponentsToRender, alignment, PageCreator.currentYPosition);
    }

    /**
     * Utility method to automatically render normal text aligned left.
     * Renders the specified text on the current page, using the page's current y position as the starting y
     * position, and the style's margin as the starting x position. Takes leading into consideration.
     * Cuts the individual words using the space as the splitting character
     *
     * @param text the text that should be rendered on the page
     */
    public static void renderLeftAlignedText(@NonNull final String text) {
        renderText(List.of(new Text(text, Processor.sentenceFont, Processor.sentenceFontSize, Processor.sentenceFontColour)),
                ContentAlignment.LEFT);
    }

    /**
     * Used to render text components using the specified alignment in the document without registering them as content
     * of the current page. This can be useful to ignore certain texts, such as the page number, from affecting whether
     * there is content on the current page.
     * Renders the specified text on the current page, using the page's current y position as the starting y
     * position, and the style's margin as the starting x position. Takes leading into consideration.
     * Cuts the individual words using the space as the splitting character.
     * Note that the method does not change the strings in any way.
     *
     * @param textComponentsToRender - the text components that should be rendered on the page
     * @param alignment              - the text alignment that should be used for the rendered text
     */
    public static void renderNoContentText(@NonNull final List<Text> textComponentsToRender,
                                           @NonNull final ContentAlignment alignment,
                                           final float startY,
                                           final Float firstIndentation) {
        try {
            // Calculates the distance of the bottom of one line to the top of the next line
            final float leading = Processor.getLeading();

            // Determines how much space the text can take up, by subtracting the margin for both sides
            final float availableWidth = Processor.getAvailableContentWidth();
            float maximumWidth = availableWidth;
            float currentLineWidth = 0;
            float lastXOffset = 0;

            // Sets the starting positions to the margin to the left, and the current paper's y position
            float startX = Processor.margin;

            // Converts the indentation in inches to points
            Float usedIndentation = null;
            if (firstIndentation != null) usedIndentation = firstIndentation * Processor.POINTS_PER_INCH;

            // Creates the content stream with the append mode, which prevents overriding existing streams
            var contentStream = new PDPageContentStream(PageAssembler.getDocument(), PageCreator.getCurrent(),
                    PDPageContentStream.AppendMode.APPEND, false);

            // Sets up the content stream
            contentStream.setFont(Processor.sentenceFont, Processor.sentenceFontSize);
            contentStream.setNonStrokingColor(Processor.sentenceFontColour);
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY);

            boolean hasIndentedFirstPart = false;

            // Contains the currently created lines
            final var textBuilder = new LinkedList<Text>();
            final var rest = new LinkedList<Text>();

            for (int k = 0; k < textComponentsToRender.size(); k++) {
                var textPart = textComponentsToRender.get(k);
                final var textPartFont = textPart.getFont();
                final var textPartFontColour = textPart.getFontColour();
                final var textPartFontSize = textPart.getFontSize();

                // Indents the first part if necessary
                if (usedIndentation != null && k == 0) {
                    contentStream.newLineAtOffset(usedIndentation, 0f);
                    maximumWidth -= usedIndentation;
                    hasIndentedFirstPart = true;
                }

                // Divides the text into its words, using exactly one (!) space as the split char
                final var words = textPart.getContent().split(" ");

                // Used to skip the first word when appending a space after all other words
                var isFirstWord = true;
                var isLastWordOfNotLastTextPart = false;

                // Apply the correct font style
                contentStream.setFont(textPart.getFont(), textPart.getFontSize());
                contentStream.setNonStrokingColor(textPart.getFontColour());

                // Tries to check if the next word can be rendered in the current line or if it does not fit
                for (int j = 0; j < words.length; j++) {
                    String word = words[j];
                    isLastWordOfNotLastTextPart = j == words.length - 1 && k != textComponentsToRender.size() - 1;

                    // Calculate the width of the entire line with a space and the current word
                    final float wordWidth = Processor.sentenceFont.getStringWidth(
                            (isFirstWord ? "" : " ") + word + (isLastWordOfNotLastTextPart ? " " : "")) /
                            1000 * Processor.sentenceFontSize;

                    // Check if the next line does not fit into the current page anymore
                    if (PageCreator.currentYPosition < Processor.margin) {
                        if (!textBuilder.isEmpty()) {
                            final var textSize = textBuilder.size();
                            for (int i = 0; i < textSize; i++) rest.addLast(textBuilder.remove());
                        }

                        if (isFirstWord) {
                            rest.addLast(new Text(word + (isLastWordOfNotLastTextPart ? " " : ""), textPart.getFont(), textPart.getFontSize(),
                                    textPart.getFontColour()));
                            isFirstWord = false;
                        } else {
                            rest.addLast(new Text(" " + word + (isLastWordOfNotLastTextPart ? " " : ""), textPart.getFont(), textPart.getFontSize(),
                                    textPart.getFontColour()));
                        }
                    }
                    // If the word does not fit in the current line, render the line and start a new line
                    else if (wordWidth > maximumWidth) {
                        // When trying to center the text, we first need to calculate the total width for our offset
                        if (alignment == ContentAlignment.CENTER) {
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
                            contentStream.setFont(text.getFont(), text.getFontSize());
                            contentStream.setNonStrokingColor(text.getFontColour());

                            var textToRender = text.getContent();

                            contentStream.showText(textToRender);
                        }

                        maximumWidth = availableWidth - (hasIndentedFirstPart ? usedIndentation : 0);
                        currentLineWidth = wordWidth;
                        textBuilder.clear();

                        // Check if the word is too long to fit in one line
                        if (wordWidth > maximumWidth) {
                            var currentLine = word;
                            var nextLineWidth = wordWidth;
                            var reachedNewPage = false;

                            while (nextLineWidth > maximumWidth && !reachedNewPage) {
                                var stringForNextLine = new StringBuilder();

                                while (nextLineWidth > maximumWidth) {
                                    stringForNextLine.append(currentLine.charAt(currentLine.length() - 1));
                                    currentLine = currentLine.substring(0, currentLine.length() - 1);
                                    nextLineWidth = Processor.sentenceFont.getStringWidth(currentLine) / 1000 *
                                            Processor.sentenceFontSize;
                                }

                                PageCreator.currentYPosition -= leading;

                                if (PageCreator.currentYPosition < Processor.margin) {
                                    reachedNewPage = true;
                                    rest.add(new Text(stringForNextLine.reverse().toString(), textPartFont,
                                            textPartFontSize, textPartFontColour));
                                } else {
                                    contentStream.showText(currentLine);
                                    contentStream.newLineAtOffset(hasIndentedFirstPart ? -usedIndentation : 0, -leading);

                                    if (hasIndentedFirstPart) maximumWidth += usedIndentation;

                                    hasIndentedFirstPart = false;

                                    currentLine = stringForNextLine.reverse().toString();
                                    nextLineWidth = Processor.sentenceFont.getStringWidth(currentLine) / 1000 *
                                            Processor.sentenceFontSize;
                                    stringForNextLine.setLength(0);
                                }
                            }

                            contentStream.showText(currentLine);
                            contentStream.newLineAtOffset(0, -leading);
                        } else {
                            PageCreator.currentYPosition -= leading;
                            contentStream.newLineAtOffset(hasIndentedFirstPart ? -usedIndentation : 0, -leading);
                            hasIndentedFirstPart = false;

                            textBuilder.addLast(new Text(word + (isLastWordOfNotLastTextPart ? " " : ""), textPart.getFont(), textPart.getFontSize(),
                                    textPart.getFontColour()));
                            maximumWidth -= wordWidth;
                        }
                        // The word does still fit in the current line
                    } else {
                        // Add the word to the builder and add a space
                        // character if the word is not the first word in the line
                        if (isFirstWord) {
                            textBuilder.addLast(new Text(word + (isLastWordOfNotLastTextPart ? " " : ""),
                                    textPart.getFont(), textPart.getFontSize(), textPart.getFontColour()));
                            isFirstWord = false;
                        } else textBuilder.addLast(new Text(" " + word +
                                (isLastWordOfNotLastTextPart ? " " : ""), textPart.getFont(),
                                textPart.getFontSize(), textPart.getFontColour()));

                        currentLineWidth += wordWidth;
                        maximumWidth -= wordWidth;
                    }
                }
            }

            // If there is still one last remaining line, it should be displayed
            if (rest.isEmpty()) {
                // When trying to center the text, we first need to calculate the total width for our offset
                if (alignment == ContentAlignment.CENTER) {
                    final float xOffset = (PageCreator.getCurrent().getMediaBox().getWidth() - currentLineWidth) / 2
                            - Processor.margin;
                    contentStream.newLineAtOffset(xOffset, 0);
                }

                final var textSize = textBuilder.size();
                for (int i = 0; i < textSize; i++) {
                    var text = textBuilder.remove();

                    // Apply the correct font style
                    contentStream.setFont(text.getFont(), text.getFontSize());
                    contentStream.setNonStrokingColor(text.getFontColour());

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
                var currentFont = rest.get(0).getFont();
                var currentFontSize = rest.get(0).getFontSize();
                var currentFontColour = rest.get(0).getFontColour();
                for (Text text : rest) {
                    if (text.getFont() != currentFont) {
                        collectedRest.addLast(new Text(itemBuilder.toString(), currentFont, currentFontSize,
                                currentFontColour));
                        itemBuilder.setLength(0);
                        currentFont = text.getFont();
                    }

                    itemBuilder.append(text.getContent());
                }

                if (!itemBuilder.isEmpty()) {
                    collectedRest.addLast(new Text(itemBuilder.toString(), currentFont, currentFontSize,
                            currentFontColour));
                    itemBuilder.setLength(0);
                }

                renderNoContentText(collectedRest, alignment, PageCreator.currentYPosition, firstIndentation);
                PageCreator.currentPageIsEmpty = false;
            }
        } catch (IOException e) {
            throw new PippException("Could not print text to the current line");
        }
    }

    /**
     * Base method to render text components in the specified alignment in the document.
     * Renders the specified text on the current page, using the page's current y position as the starting y
     * position, and the style's margin as the starting x position. Takes leading into consideration.
     * Cuts the individual words using the space as the splitting character.
     * Note that the method does not change the strings in any way.
     *
     * @param textComponentsToRender - the text components that should be rendered on the page
     * @param alignment              - the text alignment that should be used for the rendered text
     */
    public static void renderText(@NonNull final List<Text> textComponentsToRender,
                                  @NonNull final ContentAlignment alignment,
                                  final float startY) {
        renderNoContentText(textComponentsToRender, alignment, startY, null);

        // Now that there has been at least one line rendered on the current page, update the flag
        PageCreator.currentPageIsEmpty = false;
    }

    /**
     * Renders the text on the page by using an indentation that offsets the first text component by that amount.
     *
     * @param textComponentsToRender the text components that should be rendered. the first will be indented.
     * @param alignment              the desired text alignment
     * @param startY                 the start y position of the text render
     * @param indentation            the amount of indentation in inches
     */
    public static void renderIndentedText(@NonNull final List<Text> textComponentsToRender,
                                          @NonNull final ContentAlignment alignment,
                                          final float startY,
                                          final float indentation) {
        renderNoContentText(textComponentsToRender, alignment, startY, indentation);

        // Now that there has been at least one line rendered on the current page, update the flag
        PageCreator.currentPageIsEmpty = false;
    }

    /**
     * Returns true if the specified text (a string in a certain font and font size) can be rendered in a single line
     * or not. Note that this does not render the text, but simply checks if it could be rendered in one line.
     *
     * @param asText the non-null text component that should be checked
     * @return true - if the text would fit in one line; false - if it would not
     */
    public static boolean textFitsInOneLine(@NonNull Text asText) {
        final float availableWidth = Processor.dimensions.getWidth() - 2 * Processor.margin;
        final float textWidth;
        try {
            textWidth = asText.getFont().getStringWidth(asText.getContent()) / 1000 * asText.getFontSize();
        } catch (IOException e) {
            throw new PippException("Could not assess if the specified text fits in one line");
        }

        return textWidth <= availableWidth;
    }

}
