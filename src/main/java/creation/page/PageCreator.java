package creation.page;

import creation.stamp.PageNumberStamp;
import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.PDPage;
import processing.Processor;

/**
 * Used to create new pages and track them as the current page.
 * Also stores the y position of the current page.
 *
 * @version 1.0
 * @since 1.0
 */
public class PageCreator {
    /**
     * Marks the y position of the current page.
     * This is used to know where to render a new line, and is reset whenever
     * a new page is created.
     */
    public static float currentYPosition;
    /**
     * Yields true if there is no content rendered on the current page except for the page number stamp.
     * Note that classes that add content to the current page should set this flag to false.
     */
    @Setter
    private static boolean currentPageIsEmpty = true;
    /**
     * The currently considered page in the document.
     * Note that finished pages cannot be written to any more, only this instance can be written to.
     */
    @Getter
    private static PDPage current;

    /**
     * Prevents instantiation
     */
    private PageCreator() {
        throw new UnsupportedOperationException("Should not instantiate helper class");
    }

    /**
     * Assembles prior last page (if the new page is not the first) and creates a new page with a stamp
     */
    public static void createNewPage() {
        // Assembles the last page if it exists
        if (current != null) PageAssembler.commitCurrentPage();

        // Create a new page object using the processor's dimensions
        current = new PDPage(Processor.getDimensions());

        // The new page is empty
        currentPageIsEmpty = true;

        // Reset the y position
        currentYPosition = Processor.getDimensions().getHeight() - Processor.getMargin();

        // Automatically apply the page stamp
        PageNumberStamp.stampCurrentPage();
    }

    /**
     * Commits the current page, adds a blank page, and then adds a page.
     * Used for the blank instruction.
     * If the blank instruction is the last instruction in the document body or if the current page is empty,
     * it will prevent a second new page call.
     *
     * @param blankInstructionIsLastInstruction true if the calling blank instruction is the last instruction
     */
    public static void createBlankPage(boolean blankInstructionIsLastInstruction) {
        if (!currentPageIsEmpty && !blankInstructionIsLastInstruction) createNewPage();

        createNewPage();
    }

}
