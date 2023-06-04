package frontend;

import frontend.ast.AST;
import frontend.lexical_analysis.Scanner;
import frontend.lexical_analysis.Token;
import frontend.parsing.Parser;
import processing.Processor;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FrontEndBridge {

    private final File fileToRead;
    private final Scanner scanner;
    private final Parser parser;

    private BufferedReader reader;

    private final List<Token> tokens;

    public FrontEndBridge(File fileToRead) {
        this.fileToRead = fileToRead;
        tokens = new LinkedList<>();

        this.parser = new Parser(this);
        this.scanner = new Scanner(this);
    }

    public void enqueue(Token token) {
        tokens.add(token);
    }

    public Token dequeue() {
        if (tokens.isEmpty()) throw new IllegalStateException("Token Queue is empty");

        return tokens.remove(0);
    }

    public Token lookahead(int index) {
        if (index >= tokens.size() || index < 0) throw new IndexOutOfBoundsException();

        return tokens.get(index);
    }

    public void startProcessor(final AST ast) {
        new Processor().processAST(ast);
    }

    public boolean isNotEmpty() {
        return !tokens.isEmpty();
    }

    public void compile() {
        try {
            reader = new BufferedReader(new FileReader(fileToRead));

            read();

            scanner.submitToken();
            if (tokens.size() > 0) parser.s();

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
