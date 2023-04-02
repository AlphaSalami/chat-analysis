import java.io.*;
import java.util.*;

public class MostMessages {

    private final String file;
    private final int topNum;

    public MostMessages(String title, int topNum) {
        this.file = title + ".irc";
        this.topNum = topNum;
    }

    public void run() throws IOException {
        long startTime = System.nanoTime();
        String filepath = Constants.rawYear() + this.file;
        HashMap<String, Integer> sortedData = MessagePerUser.messagesPerUser(new File(filepath));
        LinkedHashMap<String, Integer> topData = new LinkedHashMap<>();

        sortedData.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(this.topNum)
                .forEachOrdered(x -> topData.put(x.getKey(), x.getValue()));

        int totalMessages = sortedData.values().stream().mapToInt(Integer::intValue).sum();
        int uniqueChatters = sortedData.size();

        System.out.printf("%d messages total. %d unique chatters.\n", totalMessages, uniqueChatters);

        int count = 1;
        for (Map.Entry<String, Integer> entry : topData.entrySet()) {
            String username = entry.getKey();
            int messageCount = entry.getValue();
            System.out.printf("%d. %s sent %d messages\n", count++, username, messageCount);

        }
        double finalTime = (double) (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.printf(("\nTime taken: (%fs)"), finalTime);
    }
}
