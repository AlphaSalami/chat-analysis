import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessagePerUser {

    public static HashMap<String, Integer> messagesPerUser(File file) throws IOException {
        String reg = "(] <\\w+>)";
        ArrayList<String> messages = ReadFile.readFile(file);
        HashMap<String, Integer> sortedData = new HashMap<>();
        Pattern pattern = Pattern.compile(reg);
        for (String line: messages) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String user = matcher.group().substring(3, matcher.group().length() - 1);
                sortedData.put(user, sortedData.getOrDefault(user, 0) + 1);
            }
        }
        return sortedData;
    }
}
