package creation;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import processing.ProcessingOptions;

import java.io.IOException;
import java.util.Calendar;

public class PDFCreator {

    public static final String outputPath = "src/main/resources/out.pdf";

    public static void create() throws IOException {
        var doc = new PDDocument();

        if (ProcessingOptions.configuration != null) {
                // MLA uses "standard, white 8.5 x 11-inch paper"
                var blankPage = new PDPage(PDRectangle.LETTER);

                var contentStream = new PDPageContentStream(doc, blankPage);

                // TODO: 1inch margin all around
                // TODO: header with page numbers

                contentStream.beginText();
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(20, 450);
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

                contentStream.showText("Hi");
                contentStream.newLine();
                contentStream.showText("Hello World!");

                var info = doc.getDocumentInformation();
                info.setCreator("Max Mustermann");
                info.setAuthor("Max Mustermann");
                info.setCreationDate(Calendar.getInstance());
                info.setTitle("My first PDF!");
                info.setKeywords("Keywords");
                info.setProducer("Pipp");

                contentStream.endText();

                contentStream.close();

                doc.addPage(blankPage);
        }
        else throw new UnsupportedOperationException("Stylesheet is not supported or does not exist.");

        doc.save(outputPath);
        doc.close();
    }

}
