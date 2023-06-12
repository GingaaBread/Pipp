package creation;

import processing.Processor;

public class HeaderStamp {

    public static void renderHeader() {
        final var headerBuilder = new StringBuilder();

        // Start by listing the authors' names one after the other separated by a new line
        for (var author : Processor.authors) {
            headerBuilder.append(author.getFirstname());
            headerBuilder.append(" ");
            headerBuilder.append(author.getLastname());

            LineFactory.renderLeftAlignedText(headerBuilder.toString());
            headerBuilder.setLength(0);
        }

        // List the assessors' names separated by a comma and one space
        for (int i = 0; i < Processor.assessors.length; i++) {
            var assessor = Processor.assessors[i];

            headerBuilder.append(assessor.getFirstname());
            headerBuilder.append(" ");
            headerBuilder.append(assessor.getLastname());

            if (i < Processor.assessors.length - 1) headerBuilder.append(", ");
        }

        LineFactory.renderLeftAlignedText(headerBuilder.toString());
        headerBuilder.setLength(0);

        // List the title of the publication
        //headerBuilder.append(Processor.);
        //TODO: Publication

        // List the date of the publication
        if (Processor.publicationDate != null) {
            final var formattedDate = Processor.usedStyleGuide.dateToString(Processor.publicationDate);
            headerBuilder.append(formattedDate);
            LineFactory.renderLeftAlignedText(headerBuilder.toString());
            headerBuilder.setLength(0);
        }
    }

}
