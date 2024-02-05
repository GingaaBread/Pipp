package creation.stamp;

import creation.content.ContentAlignment;
import creation.content.text.Text;
import creation.content.text.TextRenderer;
import creation.page.PageCreator;
import processing.Processor;
import processing.bibliography.BibliographySource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * The bibliography stamp class is used to render the bibliography section of the document on the current
 * document position. It retrieves all bibliography entries that have been cited at least once, and renders
 * them on a new, separate page. It introduces the bibliography section using a bibliography title, which uses
 * the first chapter font data. The entries are sorted lexicographically by the first author's last name, or the work
 * title if there is no author. The works are rendered using hanging (inverse) indentation.
 *
 * @version 1.0
 * @since 1.0
 */
public class BibliographyStamp {

    /**
     * Prevents instantiation
     */
    private BibliographyStamp() {
        throw new UnsupportedOperationException("Should not instantiate static helper class");
    }

    /**
     * Renders the bibliography section on a new page.
     */
    public static void stampBibliography() {
        final var citedEntries = Processor
                .getBibliographyEntries()
                .values()
                .stream()
                .filter(BibliographySource::isHasBeenCited)
                .toArray(BibliographySource[]::new);

        if (citedEntries.length == 0) return;

        PageCreator.createNewPage();

        TextRenderer.renderText(List.of(new Text(
                Processor.getUsedStyleGuide().formatBibliographyTitle(citedEntries),
                Processor.getChapterSentenceFontData()[0].font(),
                Processor.getChapterSentenceFontData()[0].fontSize(),
                Processor.getChapterSentenceFontData()[0].fontColor())
        ), ContentAlignment.CENTER);

        // Sort entries by the lastname of the first author, or the title if there is no author
        final var sortedEntries = Processor
                .getBibliographyEntries()
                .values()
                .stream()
                .filter(BibliographySource::isHasBeenCited)
                .sorted(Comparator.comparing(entry -> {
                    if (entry.getAuthors().length > 0) return entry.getAuthors()[0].getLastname();
                    else return entry.getTitle();
                }))
                .toList();

        sortedEntries.forEach(entry -> TextRenderer.renderIndentedText(
                        Arrays.stream(Processor.getUsedStyleGuide().formatBibliographyEntry(entry)).toList(),
                        ContentAlignment.LEFT,
                        PageCreator.currentYPosition,
                        Processor.getParagraphIndentation(),
                        true
                )
        );

        PageCreator.currentPageIsEmpty = false;
    }

}
