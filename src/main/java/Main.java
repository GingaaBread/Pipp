import frontend.FrontEndBridge;
import warning.WarningQueue;

import java.io.*;

/**
 *  This class is the entrypoint to the application and currently uses a hardcoded sample file.
 *  It starts the compilation process and then prints all warnings to the console.
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
public class Main {

    /**
     *  Uses a hardcoded sample file to start the compilation process.
     *  Also prints all warnings afterwards.
     * @param args Java program arguments
     */
    public static void main(String[] args) {
        // Specify the file path relative to the src/ folder
        String filePath = "src/main/resources/Sample.pipp";

        // Create a File object with the file path
        File file = new File(filePath);

        // Compile the pipp file
        new FrontEndBridge(file).compile();

        // After compilation print all warnings
        WarningQueue.printAll();
    }

}
