package cli;

import processing.Processor;

public class Version {

    public static void onVersionRequest() {
        final var version = Processor.COMPILER_VERSION;

        var versionBuilder = "Pipp" + " " + version;

        System.out.println(versionBuilder);
    }

}
