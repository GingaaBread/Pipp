import frontend.FrontEndBridge;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {

        // Specify the file path relative to the src/ folder
        String filePath = "src/main/resources/Sample.pipp";

        // Create a File object with the file path
        File file = new File(filePath);

        var frontEndBridge = new FrontEndBridge(file);
        frontEndBridge.compile();

        /* PDF Creation */

        /*

        var publication = new Publication();
        publication.setName("Grün Verlag");
        publication.setDate(new Calendar.Builder().set(Calendar.YEAR, 1990).build());
        Book book = new Book(new Author[] { new Author("Peter", "Fox") }, "Mein Leben", publication);

        System.out.println(book.toBibliography(StyleSheet.MLA9));

        PDFCreator.create(StyleSheet.MLA9);

         */

        /*
        var test = new MLA9();

        System.out.println(test.formatText("        Hello   this \t\t is a test ."));
         */
    }
}
