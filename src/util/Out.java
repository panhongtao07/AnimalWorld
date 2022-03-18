package util;

import java.io.*;

class NoOutput extends OutputStream {
    @Override
    public void write(int b) throws IOException {
        return;
    }
}

public class Out {
    public static final PrintStream noOutput = new PrintStream(new NoOutput());
    public static final PrintStream realOutput = System.out;
    public static final PrintStream before = realOutput;
    public static final PrintStream race = realOutput;
    public static final PrintStream summary = realOutput;

    public static void blockOutput() {
        System.setOut(noOutput);
    }

    public static void unblockOutput() {
        System.setOut(realOutput);
    }
}
