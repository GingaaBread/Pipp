package creation.page;

import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * Used to assemble all PDPages into one PDDocument object, which represents
 * the final PDF document. Once the last page should be added to the document,
 * the @method commitCurrentPage is used.
 *
 * @version 1.0
 * @since 1.0
 */
public class PageAssembler {

    /**
     * Represents the final document that will be rendered as a PDF
     */
    @Getter
    private static final PDDocument document = new PDDocument();

    /**
     * Prevents instantiation
     */
    private PageAssembler() {
        throw new UnsupportedOperationException("Should not instantiate static helper class");
    }

    /**
     * Adds the current page to the document.
     * If you want to create a new page afterwards, DO NOT USE THIS METHOD! Use the PageCreator, instead, which
     * calls this method.
     */
    public static void commitCurrentPage() {
        document.addPage(PageCreator.getCurrent());
    }

}
