package creation;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.printing.PDFPageable;
import processing.Processor;

import java.io.IOException;
import java.util.Calendar;

public class PDFCreator {

    public static final String outputPath = "src/main/resources/out.pdf";

    public static PDDocument document = new PDDocument();
    public static float currentYPosition = 0;

    public static void create() throws IOException {
        currentYPosition = Processor.dimensions.getHeight() - Processor.margin;

        final var pageFactory = new PageFactory();
        final var lineFactory = new LineFactory();

        var page = pageFactory.createNewPage();
        lineFactory.printText(page, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        document.addPage(page);

        // Set the meta data
        var info = document.getDocumentInformation();
        info.setCreator("Pipp v.1.0");
        if (Processor.authors.length > 0) {
            var nameBuilder = new StringBuilder();
            for (var author : Processor.authors) {
                nameBuilder.append(author.getFirstname());
                nameBuilder.append(" ");
                nameBuilder.append(author.getLastname());
                nameBuilder.append(", ");
            }
            info.setAuthor(nameBuilder.substring(0, nameBuilder.toString().length() - 2));
        }

        if (Processor.publicationDate != null) {
            var calendar = Calendar.getInstance();
            calendar.set(
                    Processor.publicationDate.getYear(),
                    Processor.publicationDate.getMonthValue() - 1, // ZERO BASED, therefore - 1
                    Processor.publicationDate.getDayOfMonth());
            info.setCreationDate(calendar);
        }

        // TODO: Title
        info.setTitle("My first PDF!");
        info.setKeywords("Keywords");

        // TODO: Document Type by Authors
        //info.setSubject(processor.get);

        // Producer should not be used as it is used for converted files

        document.save(outputPath);
        document.close();
    }

}
