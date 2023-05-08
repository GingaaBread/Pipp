package frontend;

import frontend.lexical_analysis.Scanner;
import frontend.lexical_analysis.Token;
import lombok.Setter;
import frontend.parsing.Parser;

import java.io.*;

public class FrontEndBridge {

    private File fileToRead;
    private Scanner scanner;
    private Parser parser;

    private BufferedReader reader;
    private long currentReaderIndex;
    private boolean firstTokenWasRead;

    public FrontEndBridge(File fileToRead) {
        this.fileToRead = fileToRead;

        this.parser = new Parser(this);
        this.scanner = new Scanner(this);
    }

    public void compile() {
        try {
            reader = new BufferedReader(new FileReader(fileToRead));

            read();
            scanner.submitToken();

            reader.close();
        } catch (IOException e) {
            System.out.println("File cannot be read");
            e.printStackTrace();
        }
    }

    public void receiveToken(final Token token) {
        parser.provideToken(token);

        if (!firstTokenWasRead) {
            firstTokenWasRead = true;
            parser.s();
        }
    }

    public void requestNextToken() {
        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read() throws IOException {
        int current;
        var res = reader.skip(currentReaderIndex);

        if (res != currentReaderIndex) throw new IOException("Reader could not be skipped");

        while ((current = reader.read()) != -1) {
            scanner.scan((char) current);
            currentReaderIndex++;
        }
    }

}
