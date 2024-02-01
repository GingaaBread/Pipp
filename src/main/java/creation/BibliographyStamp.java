package creation;

import processing.Processor;
import processing.bibliography.BibliographySource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BibliographyStamp {

    private BibliographyStamp() {
        throw new UnsupportedOperationException("Should not instantiate static helper class");
    }

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
                Processor.getChapterFontData()[0].font(),
                Processor.getChapterFontData()[0].fontSize(),
                Processor.getChapterFontData()[0].fontColor())
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

        // TODO: Change into hanging indentation
        sortedEntries.forEach(entry -> TextRenderer.renderText(
                Arrays.stream(Processor.getUsedStyleGuide().formatBibliographyEntry(entry)).toList(),
                ContentAlignment.LEFT)
        );

        PageCreator.currentPageIsEmpty = false;
    }

}
