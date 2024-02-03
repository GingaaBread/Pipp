package creation.document;

import creation.handler.BodyHandler;
import creation.handler.ChapterHandler;
import creation.page.PageAssembler;
import creation.page.PageCreator;
import creation.stamp.BibliographyStamp;
import error.PippException;

import java.io.IOException;

/**
 * The DocumentCreator class is responsible for starting the creation process, and saving the document
 * to the specified path. It therefore constructs the framework for all other creation classes.
 *
 * @version 1.0
 * @since 1.0
 */
public class DocumentCreator {

    /**
     * Determines the path to the file, where the final document should be saved.
     * It will be saved under the specified name.
     */
    public static final String OUTPUT_PATH = "src/main/resources/out.pdf";

    /**
     * Prevents instantiation
     */
    private DocumentCreator() {
        throw new UnsupportedOperationException("Should not instantiate static helper class");
    }

    /**
     * Instantiates the creation process by setting the document's metadata, rendering the required components,
     * and then assembling the pages. The created document is saved under the path specified in the outputPath
     * variable.
     */
    public static void create() {
        // Set the metadata
        DocumentMetaDataWriter.writeMetaData();

        // The PDF at least has one empty page
        PageCreator.createNewPage();

        // Create the document's body elements
        BodyHandler.handleAll();

        // After the body elements (including chapters) have been handled look for chapter misconfigurations
        ChapterHandler.checkForWarnings();

        // If there is at least one entry in the bibliography, display it
        BibliographyStamp.stampBibliography();

        // Always save the last page
        PageAssembler.commitCurrentPage();

        // Finally, save the file
        try {
            final var document = PageAssembler.getDocument();

            document.save(OUTPUT_PATH);
            document.close();
        } catch (IOException e) {
            throw new PippException("Could not save and close the document");
        }
    }

}
