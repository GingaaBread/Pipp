package creation;

import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import processing.StyleSheet;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;

public class PDFCreator {

    public static final String outputPath = "src/main/resources/out.pdf";

    public static void create(StyleSheet styleSheet) throws IOException {
        var doc = new PDDocument();

        switch (styleSheet) {
            case MLA9 -> {
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
            default -> throw new UnsupportedOperationException("Stylesheet " + styleSheet + " is not supported.");
        }


        doc.save(outputPath);
        doc.close();
    }

}
