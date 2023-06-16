package creation;

import java.io.IOException;

public class PDFCreator {

    public static final String outputPath = "src/main/resources/out.pdf";

    public static void create() throws IOException {
        // Set the meta data
        DocumentMetaDataWriter.writeMetaData();

        // The PDF at least has one empty page
        PageFactory.createNewPage();

        HeaderStamp.renderHeader();

        // Render some dummy text for testing purposes
        var longString = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
        LineFactory.renderLeftAlignedText(longString.repeat(50));

        // Always save the last page
        PageAssembler.finishCurrentPage();

        // Finally, save the file
        final var document = PageAssembler.getDocument();
        document.save(outputPath);
        document.close();
    }

}
