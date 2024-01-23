package frontend;

import frontend.lexical_analysis.Scanner;
import frontend.lexical_analysis.Token;
import frontend.parsing.Parser;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FrontEndBridge {

    private final Scanner scanner;
    private final Parser parser;
    private final List<Token> tokens;
    private File documentFileToRead;
    private File bibliographyFileToRead;
    private BufferedReader reader;

    /**
     * The standard method of compilation requires a file, which contains the Pipp code.
     * This constructor should be the default approach when compiling.
     *
     * @param documentFile     - the non-null document file that should be read.
     * @param bibliographyFile - the non-null bibliography file that should be read.
     */
    public FrontEndBridge(@NonNull final File documentFile, @NonNull final File bibliographyFile) {
        this();
        this.documentFileToRead = documentFile;
        this.bibliographyFileToRead = bibliographyFile;
    }

    /**
     * Provides a method to use predefined text, instead of a text file during compilation.
     * This can be useful for debugging and testing purposes.
     *
     * @param textToRead - the non-null text that should be read. It can be empty.
     */
    public FrontEndBridge(@NonNull final String textToRead) {
        this();

        for (int i = 0; i < textToRead.length(); i++) {
            var character = textToRead.charAt(i);
            scanner.scan(character);
        }

        scanner.submitToken();
    }

    /**
     * A private constructor to create the mandatory fields
     */
    private FrontEndBridge() {
        this.tokens = new LinkedList<>();
        this.parser = new Parser(this);
        this.scanner = new Scanner(this);
    }

    public void enqueueToken(@NonNull final Token token) {
        tokens.add(token);
    }

    public Token dequeueToken() {
        if (tokens.isEmpty()) throw new IllegalStateException("Token Queue is empty");

        return tokens.remove(0);
    }

    public Token lookahead(int index) {
        if (index >= tokens.size() || index < 0) throw new IndexOutOfBoundsException();

        return tokens.get(index);
    }

    public boolean containsTokens() {
        return !tokens.isEmpty();
    }

    public void compile() {
        if (documentFileToRead == null)
            throw new IllegalStateException("Should not try to read the empty file. If you are trying to debug or " +
                    "test, use the second constructor, instead.");

        try (var bibliographyReader = new BufferedReader(new FileReader(bibliographyFileToRead))) {
            reader = new BufferedReader(new FileReader(documentFileToRead));

            if (bibliographyFileToRead != null) {
                int current;
                while ((current = bibliographyReader.read()) != -1) scanner.scan((char) current);

                scanner.submitToken();
                if (!tokens.isEmpty()) {
                    parser.bibliography();
                }
                tokens.clear();
            }

            read();

            scanner.submitToken();
            if (!tokens.isEmpty()) parser.s();

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read() throws IOException {
        int current;
        while ((current = reader.read()) != -1) scanner.scan((char) current);
    }

}
