package creation.handler;

import creation.stamp.ChapterStamp;
import error.ContentException;
import frontend.ast.structure.Chapter;
import lombok.NonNull;
import warning.SelfCheckWarning;
import warning.WarningQueue;
import warning.WarningSeverity;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to handle chapter instructions.
 * If the specified chapter is a valid chapter, it will be rendered on the document.
 * If it is invalid, an exception is thrown, instead.
 *
 * @version 1.0
 * @since 1.0
 */
public class ChapterHandler {

    /**
     * Saves all defined chapters with their names, levels, etc. to look for warnings like
     * only using one chapter level instance. Also used for the table of contents.
     */
    private static final List<Chapter> definedChapters = new LinkedList<>();

    /**
     * Tracks the level of the current chapter to detect if the new chapter is one or more
     * levels too deep. Only a subchapter may follow after a chapter, not a sub-subchapter, etc.
     */
    private static int currentChapterLevel;

    /**
     * Prevents instantiation
     */
    private ChapterHandler() {
        throw new UnsupportedOperationException("Should not instantiate static helper class");
    }

    /**
     * Throws an exception if the chapter level is too deep, or else renders the chapter on the current position
     * in the document and uses its chapter level as the current chapter level.
     *
     * @param chapter the chapter that should be rendered
     */
    public static void handleChapterDeclaration(@NonNull final Chapter chapter) {
        if (chapter.getLevel() > currentChapterLevel + 1)
            throw new ContentException("5: Chapter '" + chapter.getTitle().getTextsSeparated() +
                    "' is one or more chapter levels too deep.");

        definedChapters.add(chapter);
        currentChapterLevel = chapter.getLevel();

        ChapterStamp.stampChapter(chapter.getTitle(), chapter.getLevel());
    }

    /**
     * Throws a warning if there are chapters that are the only chapters in their respective levels.
     */
    public static void checkForWarnings() {
        definedChapters
                .stream()
                .collect(Collectors.groupingBy(Chapter::getLevel, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 1)
                .flatMap(entry -> definedChapters
                        .stream()
                        .filter(chapter -> chapter.getLevel() == entry.getKey()))
                .toList()
                .forEach(instance -> WarningQueue.enqueue(new SelfCheckWarning(
                        "6: The chapter '" + instance.getTitle().getTextsSeparated() +
                                "' is the only chapter instance of its level.", WarningSeverity.CRITICAL
                )));
    }

}
