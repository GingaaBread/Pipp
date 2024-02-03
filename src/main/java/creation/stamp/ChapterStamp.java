package creation.stamp;

import creation.content.ContentAlignment;
import creation.content.text.Text;
import creation.content.text.TextRenderer;
import frontend.ast.config.Title;
import lombok.NonNull;
import processing.Processor;

public class ChapterStamp {

    /**
     * Prevents instantiation
     */
    private ChapterStamp() {
        throw new UnsupportedOperationException("Should not instantiate static helper class");
    }

    public static void stampChapter(@NonNull final Title chapterTitle, final int chapterLevel) {
        final var asList = chapterTitle
                .getTexts()
                .stream()
                .map(titleText -> {
                    if (titleText.getWork() != null) {
                        if (chapterLevel >= Processor.getChapterWorkFontData().length) {
                            // TODO Warning
                            return new Text(titleText.getWork().getEmphasisedWork(),
                                    Processor.getChapterWorkFontData()[Processor.getChapterWorkFontData().length - 1]);
                        } else {
                            return new Text(titleText.getWork().getEmphasisedWork(),
                                    Processor.getChapterWorkFontData()[chapterLevel]);
                        }
                    } else if (titleText.getEmphasis() != null) {
                        if (chapterLevel >= Processor.getChapterEmphasisFontData().length) {
                            // TODO Warning
                            return new Text(titleText.getEmphasis().getEmphasisedText(), Processor
                                    .getChapterEmphasisFontData()[Processor.getChapterEmphasisFontData().length - 1]);
                        } else {
                            return new Text(titleText.getEmphasis().getEmphasisedText(),
                                    Processor.getChapterEmphasisFontData()[chapterLevel]);
                        }
                    } else {
                        if (chapterLevel >= Processor.getChapterSentenceFontData().length) {
                            // TODO Warning
                            return new Text(titleText.getText(), Processor
                                    .getChapterSentenceFontData()[Processor.getChapterSentenceFontData().length - 1]);
                        } else {
                            return new Text(titleText.getText(),
                                    Processor.getChapterSentenceFontData()[chapterLevel]);
                        }
                    }
                })
                .toList();

        TextRenderer.renderText(asList, ContentAlignment.CENTER);
    }

}
