import frontend.FrontEndBridge;
import warning.WarningQueue;

import java.io.File;

/**
 * This class is the entrypoint to the application and currently uses a hardcoded sample file.
 * It starts the compilation process and then prints all warnings to the console.
 *
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
public class Main {

    /**
     * Uses a hardcoded sample file to start the compilation process.
     * Also prints all warnings afterwards.
     *
     * @param args Java program arguments
     */
    public static void main(String[] args) {
        // Specify the file path relative to the src/ folder
        final String documentFilePath = "src/main/resources/Sample.pipp";
        final String bibliographyFilePath = "src/main/resources/bibliography.pipp";

        final var documentFile = new File(documentFilePath);
        final var bibliographyFile = new File(bibliographyFilePath);


        // Compile the pipp file
        new FrontEndBridge(documentFile, bibliographyFile).compile();

        System.out.println("Compilation complete. Now printing possible warnings:");
        // After compilation print all warnings
        WarningQueue.printAll();
    }

}
