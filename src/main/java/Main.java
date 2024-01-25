import frontend.FrontEndBridge;
import warning.WarningQueue;

import java.io.File;
import java.util.logging.Logger;

/**
 * This class is the entrypoint to the application and currently uses a hardcoded sample file.
 * It starts the compilation process and then prints all warnings to the console.
 *
 * @version 1.0
 * @since 1.0
 */
public class Main {

    /**
     * Uses a hardcoded sample file to start the compilation process.
     * Also prints all warnings after compilation.
     *
     * @param args Java program arguments
     */
    public static void main(String[] args) {
        final var logger = Logger.getLogger(Main.class.getName());

        // Specify the file path relative to the src/ folder
        final String documentFilePath = "src/main/resources/Sample.pipp";
        final String bibliographyFilePath = "src/main/resources/bibliography.pipp";

        final var documentFile = new File(documentFilePath);
        final var bibliographyFile = new File(bibliographyFilePath);


        // Compile the pipp and bibliography files
        new FrontEndBridge(documentFile, bibliographyFile).compile();
        logger.info("Compilation successful. Now printing possible warnings:");

        // After a successful compilation print all warnings
        WarningQueue.printAll();
    }

}
