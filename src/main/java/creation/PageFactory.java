package creation;

import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import processing.Processor;

public class PageFactory {

    private final PageNumberStamp stamp;

    public PageFactory() {
        this.stamp = new PageNumberStamp();
    }

    public PDPage createNewPage() {
        // Create a new page object using the processor's dimensions
        var page = new PDPage(Processor.dimensions);

        // Apply the page stamp
        page = stamp.stampPage(page);

        // Finally, return the new page
        return page;
    }

}
