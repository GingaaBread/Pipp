package creation;

import processing.Processor;

/**
 *  Used to add a header to the document, which includes the names of all authors and assessors, the title of the
 *  document, and the publication date.
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
public class HeaderStamp {

    /**
     *  Renders the header stamp to the current position in the document
     */
    public static void renderHeader() {
        final var headerBuilder = new StringBuilder();

        // Start by listing the authors' names one after the other separated by a new line
        for (var author : Processor.authors) {
            headerBuilder.append(author.nameToString());

            TextRenderer.renderLeftAlignedText(headerBuilder.toString());
            headerBuilder.setLength(0);
        }

        // List the assessors' names separated by a comma and one space
        for (int i = 0; i < Processor.assessors.length; i++) {
            var assessor = Processor.assessors[i];
            headerBuilder.append(assessor.nameToString());

            if (i < Processor.assessors.length - 1) headerBuilder.append(", ");
        }

        TextRenderer.renderLeftAlignedText(headerBuilder.toString());
        headerBuilder.setLength(0);

        // List the title of the publication
        if (Processor.publicationTitle != null) {
            TextRenderer.renderText(
                Processor
                        .publicationTitle
                        .getTexts()
                        .stream()
                        .map(titleText -> {
                            if (titleText.getText() != null)
                                return new Text(titleText.getText(), TextStyle.REGULAR);
                            else if (titleText.getEmphasis() != null)
                                return new Text(titleText.getEmphasis().getEmphasisedText(), TextStyle.ITALIC);
                            else if (titleText.getWork() != null)
                                return new Text(titleText.getWork().getEmphasisedWork(), TextStyle.ITALIC);
                            else throw new UnsupportedOperationException("Title text type " + titleText +
                                        " is not yet supported!");
                        })
                        .toList(),
                    TextAlignment.LEFT
            );
        }

        // List the date of the publication
        if (Processor.publicationDate != null) {
            final var formattedDate = Processor.usedStyleGuide.dateToString(Processor.publicationDate);
            headerBuilder.append(formattedDate);
            TextRenderer.renderLeftAlignedText(headerBuilder.toString());
            headerBuilder.setLength(0);
        }
    }

}
