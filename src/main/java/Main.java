import lexical_analysis.Scanner;
import processing.style.MLA9;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {

        var scanner = new Scanner();

        // Specify the file path relative to the src/ folder
        String filePath = "src/main/resources/Sample.pipp";

        // Create a File object with the file path
        File file = new File(filePath);

        try {
            var bufferedReader = new BufferedReader(new FileReader(file));

            int current;
            while ((current = bufferedReader.read()) != -1) {
                scanner.scan((char) current);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
            e.printStackTrace();
        }



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
