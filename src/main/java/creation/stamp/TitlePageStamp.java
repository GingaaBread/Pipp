package creation.stamp;

import creation.content.ContentAlignment;
import creation.content.text.Text;
import creation.content.text.TextRenderer;
import creation.page.PageAssembler;
import creation.page.PageCreator;
import processing.Processor;

import java.util.List;

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
        for (var author : Processor.getAuthors()) {
            TextRenderer.renderLeftAlignedText(author.nameToString());

            if (author.getArea() != null)
                TextRenderer.renderLeftAlignedText(author.getArea());

            if (author.getId() != null)
                TextRenderer.renderLeftAlignedText(author.getId());

            if (author.getEmailAddress() != null)
                TextRenderer.renderLeftAlignedText(author.getEmailAddress());
        }
    }
}
