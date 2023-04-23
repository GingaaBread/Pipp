import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        // Specify the file path relative to the src/ folder
        String filePath = "src/Sample.pipp";

        // Create a File object with the file path
        File file = new File(filePath);

        // Create a StringBuilder to store the file contents
        StringBuilder sb = new StringBuilder();

        try {
            // Create a Scanner to read the file
            var scanner = new java.util.Scanner(file);

            // Read the file contents line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                sb.append(line);
                sb.append(System.lineSeparator()); // Append line separator for each line
            }

            // Close the Scanner
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

        var scanner = new Scanner(fileContents);
    }
}
