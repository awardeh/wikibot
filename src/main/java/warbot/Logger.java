package warbot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public interface Logger {
    String INPUTS = "./inputs.txt";
    String OUTPUTS = "./outputs.txt";

    static void logInput(String input) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(INPUTS, true));
        out.write(input);
        out.newLine();
        out.close();
    }

    static void logOutput(String output) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(OUTPUTS, true));
        out.write(output);
        out.newLine();
        out.close();
    }
}
