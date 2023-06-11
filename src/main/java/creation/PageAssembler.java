package creation;

import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PageAssembler {

    /**
     *  Represents the final PDF document
     */
    @Getter
    private final static PDDocument document = new PDDocument();

    /**
     *  Adds the current page to the document.
     *  If you want to create a new page afterwards, DO NOT USE THIS METHOD! Use the PageFactory, instead, which
     *  calls this method.
     */
    public static void finishCurrentPage() {
        PageAssembler.document.addPage(PageFactory.getCurrent());
    }

}
