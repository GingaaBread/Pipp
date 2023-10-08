import frontend.FrontEndBridge;
import warning.WarningQueue;

import java.io.*;

public class Main {
    public static void main(String[] args) {

        // Specify the file path relative to the src/ folder
        String filePath = "src/main/resources/Sample.pipp";

        new WarningQueue();

        // Create a File object with the file path
        File file = new File(filePath);

        var frontEndBridge = new FrontEndBridge(file);
        frontEndBridge.compile();

        WarningQueue.getInstance().printAll();
    }
}
