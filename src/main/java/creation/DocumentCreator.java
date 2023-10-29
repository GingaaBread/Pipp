package creation;

import java.io.IOException;
import java.util.List;

/**
 *  The DocumentCreator class is responsible for starting the creation process, and saving the document
 *  to the specified path. It therefore constructs the framework for all other creation classes.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
public class DocumentCreator {

    /**
     *  Determines the path to the file, where the final document should be saved.
     *  It will be saved under the specified name.
     */
    public static final String outputPath = "src/main/resources/out.pdf";

    /**
     *  Instantiates the creation process by setting the document's metadata, rendering the required components,
     *  and then assembling the pages. The created document is saved under the path specified in the outputPath
     *  variable.
     */
    public static void create() {
        // Set the metadata
        DocumentMetaDataWriter.writeMetaData();

        // The PDF at least has one empty page
        PageCreator.createNewPage();

        // Create the document's body elements
        BodyHandler.handle();

        // Always save the last page
        PageAssembler.commitCurrentPage();

        // Finally, save the file
        try {
            final var document = PageAssembler.getDocument();

            document.save(outputPath);
            document.close();
        } catch (IOException e) {
            System.out.println("Could not save and close the document");
        }
    }

}
