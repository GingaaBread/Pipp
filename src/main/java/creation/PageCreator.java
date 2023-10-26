package creation;

import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDPage;
import processing.Processor;

/**
 *  Used to create new pages and track them as the current page.
 *  Also stores the y position of the current page.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
public class PageCreator {

    /**
     *  The currently considered page in the document.
     *  Note that finished pages cannot be written to anymore, only this instance can be written to.
     */
    @Getter
    private static PDPage current;

    /**
     *  Marks the y position of the current page.
     *  This is used to know where to render a new line, and is reset whenever
     *  a new page is created.
     */
    public static float currentYPosition;

    /**
     *  Assembles the last page (if new page is not the first) and creates a new page with a stamp
     */
    public static void createNewPage() {
        // Assembles the last page if it exists
        if (PageCreator.current != null) PageAssembler.commitCurrentPage();

        // Create a new page object using the processor's dimensions
        PageCreator.current = new PDPage(Processor.dimensions);

        // Reset the y position
        currentYPosition = Processor.dimensions.getHeight() - Processor.margin;

        // Automatically apply the page stamp
        PageNumberStamp.stampCurrentPage();
    }

}
