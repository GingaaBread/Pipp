package creation.stamp;

import creation.content.text.Text;
import creation.content.text.TextRenderer;
import creation.page.PageCreator;
import frontend.ast.config.Emphasis;
import frontend.ast.config.Title;
import frontend.ast.config.Work;
import lombok.NonNull;
import processing.Processor;
import processing.constant.ChapterSpacingType;
import warning.MissingMemberWarning;
import warning.WarningQueue;
import warning.WarningSeverity;

/**
 * The chapter stamp class is used to render a chapter to the current page using the text renderer class.
 * It uses the font data determined by the processor and applies the correct font data of the desired chapter level.
 *
 * @version 1.0
 * @since 1.0
 */
public class ChapterStamp {

    /**
     * Prevents instantiation
     */
    private ChapterStamp() {
        throw new UnsupportedOperationException("Should not instantiate static helper class");
    }

    /**
     * Renders all title text components of the chapter title on the current document page using the desired
     * chapter level. Also takes the desired chapter spacing into consideration.
     *
     * @param chapterTitle the chapter title component that should be rendered
     * @param chapterLevel the depth level of the chapter
     */
    public static void stampChapter(@NonNull final Title chapterTitle, final int chapterLevel) {
        final var asList = chapterTitle
                .getTexts()
                .stream()
                .map(titleText -> {
                    if (titleText.getWork() != null)
                        return workToText(titleText.getWork(), chapterLevel);
                    else if (titleText.getEmphasis() != null)
                        return emphasisToText(titleText.getEmphasis(), chapterLevel);
                    else
                        return sentenceToText(titleText.getText(), chapterLevel);
                })
                .toList();

        if (Processor.getChapterSpacingType() == ChapterSpacingType.EXTRA_LINE) {
            // Use the default sentence font for the leading calculation
            final float leading = 1.2f * Processor.getSentenceFontData().fontSize() * Processor.getSpacing();
            PageCreator.currentYPosition -= leading;
        }

        TextRenderer.renderText(asList, Processor.getChapterAlignment());

        if (Processor.getChapterSpacingType() == ChapterSpacingType.EXTRA_LINE) {
            // Use the default sentence font for the leading calculation
            final float leading = 1.2f * Processor.getSentenceFontData().fontSize() * Processor.getSpacing();
            PageCreator.currentYPosition -= leading;
        }
    }

    private static @NonNull Text workToText(@NonNull final Work work, int chapterLevel) {
        if (chapterLevel >= Processor.getChapterWorkFontData().length) {
            enqueueFontDataWarning(work.getEmphasisedWork());
            return new Text(work.getEmphasisedWork(),
                    Processor.getChapterWorkFontData()[Processor.getChapterWorkFontData().length - 1]);
        } else {
            return new Text(work.getEmphasisedWork(), Processor.getChapterWorkFontData()[chapterLevel]);
        }
    }

    private static @NonNull Text emphasisToText(@NonNull final Emphasis emphasis, int chapterLevel) {
        if (chapterLevel >= Processor.getChapterEmphasisFontData().length) {
            enqueueFontDataWarning(emphasis.getEmphasisedText());
            return new Text(emphasis.getEmphasisedText(),
                    Processor.getChapterEmphasisFontData()[Processor.getChapterEmphasisFontData().length - 1]);
        } else {
            return new Text(emphasis.getEmphasisedText(), Processor.getChapterEmphasisFontData()[chapterLevel]);
        }
    }

    private static @NonNull Text sentenceToText(@NonNull final String sentence, int chapterLevel) {
        if (chapterLevel >= Processor.getChapterSentenceFontData().length) {
            enqueueFontDataWarning(sentence);
            return new Text(sentence,
                    Processor.getChapterSentenceFontData()[Processor.getChapterSentenceFontData().length - 1]);
        } else {
            return new Text(sentence, Processor.getChapterSentenceFontData()[chapterLevel]);
        }
    }

    /**
     * Used to warn the user about chapters being too deep to have font data set up in the style guide.
     *
     * @param chapterName the chapter text for referencing the affected chapter
     */
    private static void enqueueFontDataWarning(@NonNull final String chapterName) {
        WarningQueue.enqueue(new MissingMemberWarning("The level of the chapter with the text '" +
                chapterName + "' has no defined font data in the used style guide.", WarningSeverity.LOW));
    }

}
