package processing;

/**
 *  Contains information and options about the compilation process
 *
 *  //todo remove stylesheet use and replace it with this
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

}
