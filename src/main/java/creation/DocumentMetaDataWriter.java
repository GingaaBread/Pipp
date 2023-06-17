package creation;

import processing.Processor;

import java.util.Calendar;

/**
 *  The DocumentMetaDataWriter class is responsible for reading the document's configurations in order to
 *  set the document's metadata. Examples include the authors, publication date, and document title.
 *  Note that the creator is always going to be Pipp.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
public class DocumentMetaDataWriter {

    public static void writeMetaData() {
        // Get the document metadata from the assembler
        var info = PageAssembler.getDocument().getDocumentInformation();

        // Set the creator to Pipp's current version
        info.setCreator("Pipp v." + Processor.COMPILER_VERSION);

        // Set the author metadata to all authors using their first and last names separated by a comma
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

        // Set the creation date metadata to the specified publication date
        if (Processor.publicationDate != null) {
            var calendar = Calendar.getInstance();
            //noinspection MagicConstant
            calendar.set(
                    Processor.publicationDate.getYear(),
                    Processor.publicationDate.getMonthValue() - 1, // ZERO BASED, therefore - 1
                    Processor.publicationDate.getDayOfMonth());
            info.setCreationDate(calendar);
        }

        // Set the document's title
        info.setTitle(Processor.documentTitle.getTextsUnformatted());

        info.setKeywords("Pipp");

        // TODO: Document Type by Authors
        //info.setSubject(processor.get);

        // Producer should not be used as it is used for converted files
    }

}
