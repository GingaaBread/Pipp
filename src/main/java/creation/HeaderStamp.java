package creation;

import processing.Processor;

/**
 * Used to add a header to the document, which includes the names of all authors and assessors, the title of the
 * document, and the publication date.
 *
 * @version 1.0
 * @since 1.0
 */
public class HeaderStamp {

    /**
     * Renders the header stamp to the current position in the document
     */
    public static void renderHeader() {
        final var headerBuilder = new StringBuilder();

        // First print the institution if it exists
        if (Processor.publicationInstitution != null)
            TextRenderer.renderLeftAlignedText(Processor.publicationInstitution);

        // Then print the chair if it exists
        if (Processor.publicationChair != null)
            TextRenderer.renderLeftAlignedText(Processor.publicationChair);

        for (var author : Processor.authors) TextRenderer.renderLeftAlignedText(author.nameToString());

        // List the assessors' names separated by a comma and one space
        for (int i = 0; i < Processor.assessors.length; i++) {
            var assessor = Processor.assessors[i];
            headerBuilder.append(assessor.nameToString());

            if (i < Processor.assessors.length - 1) headerBuilder.append(", ");
        }

        if (Processor.assessors.length > 0) {
            TextRenderer.renderLeftAlignedText(headerBuilder.toString());
            headerBuilder.setLength(0);
        }

        // List the title of the publication
        if (Processor.publicationTitle.getTexts().size() > 0) {
            TextRenderer.renderText(
                    Processor
                            .publicationTitle
                            .getTexts()
                            .stream()
                            .map(titleText -> {
                                if (titleText.getText() != null)
                                    return new Text(titleText.getText(), Processor.sentenceFont, Processor.sentenceFontSize,
                                            Processor.sentenceFontColour);
                                else if (titleText.getEmphasis() != null)
                                    return new Text(titleText.getEmphasis().getEmphasisedText(), Processor.emphasisFont,
                                            Processor.emphasisFontSize, Processor.emphasisFontColour);
                                else if (titleText.getWork() != null)
                                    return new Text(titleText.getWork().getEmphasisedWork(), Processor.workFont,
                                            Processor.workFontSize, Processor.workFontColour);
                                else throw new UnsupportedOperationException("Title text type " + titleText +
                                            " is not yet supported!");
                            })
                            .toList(),
                    ContentAlignment.LEFT
            );
        }

        // List the date of the publication only if there is no given semester
        if (Processor.publicationDate != null && Processor.publicationSemester == null) {
            final var formattedDate = Processor.usedStyleGuide.dateToString(Processor.publicationDate);
            headerBuilder.append(formattedDate);
            TextRenderer.renderLeftAlignedText(headerBuilder.toString());
            headerBuilder.setLength(0);
        }

        if (Processor.publicationSemester != null)
            TextRenderer.renderLeftAlignedText(Processor.publicationSemester);
    }

}
