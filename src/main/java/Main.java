import processing.style.MLA9;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Specify the file path relative to the src/ folder
        String filePath = "src/main/resources/Sample.pipp";

        // Create a File object with the file path
        File file = new File(filePath);

        // Create a StringBuilder to store the file contents
        StringBuilder sb = new StringBuilder();

        try {
            // Create a lexical_analysis.Scanner to read the file
            var scanner = new java.util.Scanner(file);

            // Read the file contents line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                sb.append(line);
                sb.append(System.lineSeparator()); // Append line separator for each line
            }

            // Close the lexical_analysis.Scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
            e.printStackTrace();
        }

        // Convert the StringBuilder to a String
        String fileContents = sb.toString();

        // Print the file contents
        System.out.println("File Contents:");

        System.out.println(fileContents);

        // Scan the file
        //new Scanner(fileContents);

        /* PDF Creation */

        /*

        var publication = new Publication();
        publication.setName("Grün Verlag");
        publication.setDate(new Calendar.Builder().set(Calendar.YEAR, 1990).build());
        Book book = new Book(new Author[] { new Author("Peter", "Fox") }, "Mein Leben", publication);

        System.out.println(book.toBibliography(StyleSheet.MLA9));

        PDFCreator.create(StyleSheet.MLA9);

         */

        var test = new MLA9();

        System.out.println(test.formatText("        Hello   this \t\t is a test ."));
    }
}
