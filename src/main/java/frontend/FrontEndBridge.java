package frontend;

import frontend.ast.AST;
import frontend.lexical_analysis.Scanner;
import frontend.lexical_analysis.Token;
import frontend.parsing.Parser;
import lombok.NonNull;
import processing.Processor;

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
    private File fileToRead;
    private BufferedReader reader;

    /**
     * The standard method of compilation requires a file, which contains the Pipp code.
     * This constructor should be the default approach when compiling.
     *
     * @param fileToRead - the non-null file that should be read.
     */
    public FrontEndBridge(@NonNull final File fileToRead) {
        this();
        this.fileToRead = fileToRead;
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

    public void startProcessor(@NonNull final AST ast) {
        new Processor().processAST(ast);
    }

    public boolean containsTokens() {
        return !tokens.isEmpty();
    }

    public void compile() {
        if (fileToRead == null)
            throw new IllegalStateException("Should not try to read the empty file. If you are trying to debug or " +
                    "test, use the second constructor, instead.");

        try {
            reader = new BufferedReader(new FileReader(fileToRead));

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
