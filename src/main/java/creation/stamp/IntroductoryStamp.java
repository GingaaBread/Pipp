package creation.stamp;

import creation.content.ContentAlignment;
import creation.content.text.Text;
import creation.content.text.TextRenderer;
import processing.Processor;

public abstract class IntroductoryStamp {

    /**
     * If the user has defined a publication institution, it is rendered on the header using left content alignment.
     */
    protected static void renderInstitutionIfExists() {
        if (Processor.getPublicationInstitution() == null) return;
        TextRenderer.renderLeftAlignedText(Processor.getPublicationInstitution());
    }

    /**
     * If the user has defined a publication chair, it is rendered on the header using left content alignment.
     */
    protected static void renderChairIfExists() {
        if (Processor.getPublicationChair() == null) return;
        TextRenderer.renderLeftAlignedText(Processor.getPublicationChair());
    }

    /**
     * Renders all authors one after another using left content alignment.
     */
    protected static void renderAuthorsIfAnyExist() {
        for (var author : Processor.getAuthors()) TextRenderer.renderLeftAlignedText(author.nameToString());
    }

    /**
     * Renders all assessors as a single unit of text using left content alignment.
     * Assessors are separated by a comma.
     */
    protected static void renderAssessorsIfAnyExist() {
        final var headerBuilder = new StringBuilder();
        final var assessors = Processor.getAssessors();

        // List the assessors' names separated by a comma and one space
        for (int i = 0; i < assessors.length; i++) {
            var assessor = assessors[i];
            headerBuilder.append(assessor.nameToString());

            if (i < assessors.length - 1) headerBuilder.append(", ");
        }

        // Only render if there is at least one assessor
        if (assessors.length > 0) TextRenderer.renderLeftAlignedText(headerBuilder.toString());
    }

    /**
     * Renders the title using the different font components and left content alignment
     */
    protected static void renderTitleIfExists() {
        if (Processor.getPublicationTitle().getTexts().isEmpty()) return;
        TextRenderer.renderText(
                Processor
                        .getPublicationTitle()
                        .getTexts()
                        .stream()
                        .map(titleText -> {
                            if (titleText.getText() != null)
                                return new Text(titleText.getText(), Processor.getSentenceFontData());
                            else if (titleText.getEmphasis() != null)
                                return new Text(titleText.getEmphasis().getEmphasisedText(),
                                        Processor.getEmphasisFontData());
                            else if (titleText.getWork() != null)
                                return new Text(titleText.getWork().getEmphasisedWork(), Processor.getWorkFontData());
                            else throw new UnsupportedOperationException("Title text type " + titleText +
                                        " is not yet supported!");
                        })
                        .toList(),
                ContentAlignment.LEFT
        );
    }

    /**
     * Renders the formatted date if specified by the user, but only if the date exists and the user has not defined
     * a semester as well.
     */
    protected static void renderDateOrSemesterIfExist() {
        if (Processor.getPublicationSemester() != null)
            TextRenderer.renderLeftAlignedText(Processor.getPublicationSemester());
        else if (Processor.getPublicationDate() != null) {
            final var formattedDate = Processor.getUsedStyleGuide().dateToString(Processor.getPublicationDate());
            TextRenderer.renderLeftAlignedText(formattedDate);
        }
    }
}
