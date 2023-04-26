package processing;

import processing.style.StyleSheet;

/**
 *  Contains information and options about the compilation process
 *
 *  @since 1.0
 *  @version 1.0
 */
public class ProcessingOptions {

    /**
     *  Determines the Pipp versions this compiler supports.
     *  If the user tries to scan a document with a Pipp version not included in this array, an exception is thrown
     */
    public static final String[] SUPPORTED_VERSIONS = new String[] {
            "1.0"
    };

    /**
     *  Determines which stylesheet should be used during compilation
     */
    public static StyleSheet usedStyleSheet = null;

    /**
     *  Determines the document's configuration options specified by the user
     */
    public static Configuration configuration = null;

}
