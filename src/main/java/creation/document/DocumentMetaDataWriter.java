package creation.document;

import creation.page.PageAssembler;
import processing.Processor;

import java.util.Calendar;

/**
 * The DocumentMetaDataWriter class is responsible for reading the document's configurations in order to
 * set the document's metadata. Examples include the authors, publication date, and document title.
 * Note that the creator is always going to be Pipp.
 *
 * @version 1.0
 * @since 1.0
 */
public class DocumentMetaDataWriter {

    /**
     * Prevents instantiation
     */
    private DocumentMetaDataWriter() {
        throw new UnsupportedOperationException("Should not instantiate static helper class");
    }

    /**
     * Uses the data specified in the {@link Processor} to write metadata to the document.
     * This includes the current compiler version, the names of the authors, the creation date, etc.
     */
    public static void writeMetaData() {
        // Get the document metadata from the assembler
        var info = PageAssembler.getDocument().getDocumentInformation();

        // Set the creator to Pipp's current version
        info.setCreator("Pipp v." + Processor.COMPILER_VERSION);

        // Set the author metadata to all authors using their first and last names separated by a comma
        final var authors = Processor.getAuthors();
        if (authors.length > 0) {
            var nameBuilder = new StringBuilder();
            for (var author : authors) {
                nameBuilder.append(author.nameToString());
                nameBuilder.append(", ");
            }

            // Also remove the last comma
            info.setAuthor(nameBuilder.substring(0, nameBuilder.toString().length() - 2));
        }

        // Set the creation date metadata to the specified publication date
        final var publicationDate = Processor.getPublicationDate();
        if (publicationDate != null) {
            var calendar = Calendar.getInstance();
            //noinspection MagicConstant
            calendar.set(
                    publicationDate.getYear(),
                    publicationDate.getMonthValue() - 1, // ZERO BASED, therefore - 1
                    publicationDate.getDayOfMonth()
            );
            info.setCreationDate(calendar);
        }

        // Set the document's title
        info.setTitle(Processor.getDocumentTitle().getTextsSeparated());

        info.setKeywords("Pipp");

        // Note: Producer metadata should not be used as it is used for converted files
    }

}
