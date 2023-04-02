import java.io.*;
import java.util.*;

public class WriteFile {

    public static void writeFile(File file, ArrayList<String> messages) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (String line: messages) {
            bw.write(line);
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }
}
