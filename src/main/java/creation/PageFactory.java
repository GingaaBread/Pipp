package creation;

import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDPage;
import processing.Processor;

public class PageFactory {

    /**
     *  The currently considered page in the document.
     *  Note that finished pages cannot be written to anymore, only this instance can be written to.
     */
    @Getter
    private static PDPage current;

    public static float currentYPosition;

    /**
     *  Assembles the last page (if new page is not the first) and creates a new page with a stamp
     */
    public static void createNewPage() {
        // Assembles the last page if it exists
        if (PageFactory.current != null) PageAssembler.finishCurrentPage();

        // Create a new page object using the processor's dimensions
        PageFactory.current = new PDPage(Processor.dimensions);

        // Reset the y position
        currentYPosition = Processor.dimensions.getHeight() - Processor.margin;

        // Automatically apply the page stamp
        PageNumberStamp.stampCurrentPage();
    }

}
