import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChatterWords {

    private final String targetUser;

    public ChatterWords(String targetUser) {
        this.targetUser = targetUser;
    }
    public static HashMap<String, Integer> getChatterWords(File file, String targetUser) {
        try {
            ArrayList<String> data = ReadFile.readFile(file);
            HashMap<String, Integer> wordCounter = new HashMap<>();
            int lines = 1;
            for (String line : data) {
                if (line.contains(String.format("<%s>", targetUser))) {
                    wordCounter.put("0xTotalMessages", lines++);
                    String[] step = line.split(">");
                    String[] allData = step[1].trim().split(" ");
                    for (String word : allData) {
                        wordCounter.merge(word, 1, Integer::sum);
                    }
                }
            }
            return wordCounter;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void run() throws IOException, NullPointerException {
        long startTime = System.nanoTime();
        File[] files = LoadFiles.loadFiles(Constants.rawYear());
        HashMap<String, Integer> words = new HashMap<>();
        int lines = 1;
        for (File file : files) {
            Map<String, Integer> fileData = getChatterWords(file, this.targetUser);
            assert fileData != null;
            lines += fileData.getOrDefault("0xTotalMessages", 0);
            fileData.remove("0xTotalMessages");
            fileData.forEach((word, count) -> words.merge(word, count, Integer::sum));
        }
        ArrayList<String> outMessages = new ArrayList<>();
        LinkedHashMap<String, Integer> sortedWordCounter = new LinkedHashMap<>();
        words.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> sortedWordCounter.put(x.getKey(), x.getValue()));

        int uniqueWords = sortedWordCounter.size();
        int totalWords = sortedWordCounter.values().stream().mapToInt(Integer::intValue).sum();
        outMessages.add(String.format("%d messages with %d unique words with %d words total\n", lines-1, uniqueWords, totalWords));
        int i = 1;
        for (Map.Entry<String, Integer> entry : sortedWordCounter.entrySet()) {
            int wordCount = entry.getValue();
            String word = entry.getKey();
            if (word.isBlank()) {
                word = "<message deleted> (timeout by moderator)";
            }
            outMessages.add(String.format("%d: %d uses of %s", i++, wordCount, word));
        }

        File outfile = new File(Constants.chatterWordDest() + this.targetUser + " words.log");
        WriteFile.writeFile(outfile, outMessages);

        double finalTime = (double) (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.printf(("[+] Log generated (%fs)"), finalTime);
    }
}
