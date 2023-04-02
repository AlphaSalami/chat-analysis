import java.io.*;
import java.util.*;

public class ReadFile {

    public static ArrayList<String> readFile(File file) throws IOException {
        String line;
        ArrayList<String> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));

        while ((line = br.readLine()) != null) {
            data.add(line);
        }
        br.close();
        return data;
    }
}
