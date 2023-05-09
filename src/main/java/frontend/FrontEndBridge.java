package frontend;

import frontend.lexical_analysis.Scanner;
import frontend.lexical_analysis.Token;
import frontend.parsing.Parser;
import lombok.Getter;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;

public class FrontEndBridge {

    private final File fileToRead;
    private final Scanner scanner;
    private final Parser parser;

    private BufferedReader reader;

    @Getter
    private final Queue<Token> tokens;

    public FrontEndBridge(File fileToRead) {
        this.fileToRead = fileToRead;
        tokens = new ArrayDeque<>();

        this.parser = new Parser(this);
        this.scanner = new Scanner(this);
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
