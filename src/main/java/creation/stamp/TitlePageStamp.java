package creation.stamp;

import creation.content.ContentAlignment;
import creation.content.text.Text;
import creation.content.text.TextRenderer;
import creation.page.PageCreator;
import error.PippException;
import processing.Processor;

import java.io.IOException;
import java.util.LinkedList;

public class TitlePageStamp extends IntroductoryStamp {

    /**
     * Prevents instantiation
     */
    private TitlePageStamp() {
        throw new UnsupportedOperationException("Cannot instantiate helper class");
    }

    public static void renderTitlePage() {
        renderInstitutionIfExists();
        renderChairIfExists();
        renderTitleIfExists();
        renderAssessorsIfAnyExist();
        renderDateOrSemesterIfExist();

        renderTitle();

        renderAuthorsIfAnyExist();
        PageCreator.createNewPage();
    }

    private static void renderTitle() {
        TextRenderer.renderText(
                Processor.getDocumentTitle().asTextList(),
                ContentAlignment.CENTRE
        );
    }

    /**
     * Renders all authors one after another using left content alignment.
     */
    protected static void renderAuthorsIfAnyExist() {
        final var texts = new LinkedList<Text>();

        for (var author : Processor.getAuthors()) {
            texts.add(new Text(author.nameToString(), Processor.getSentenceFontData()));

            if (author.getArea() != null)
                texts.add(new Text(author.getArea(), Processor.getSentenceFontData()));

            if (author.getId() != null)
                texts.add(new Text(author.getId(), Processor.getSentenceFontData()));


            if (author.getEmailAddress() != null)
                texts.add(new Text(author.getEmailAddress(), Processor.getSentenceFontData()));
        }

        try {
            TextRenderer.renderBottomLeftText(texts);
        } catch (IOException e) {
            throw new PippException("Could not render the title page");
        }
    }
}
