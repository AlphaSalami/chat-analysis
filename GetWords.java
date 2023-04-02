import java.io.*;
import java.util.*;

public class GetWords {

    public static HashMap<String, Integer> getWords(File file) {
        try {
            ArrayList<String> data = ReadFile.readFile(file);
            HashMap<String, Integer> wordCounter = new HashMap<>();
            for (String line : data) {
                String[] step = line.split(">");
                String[] allData = step[1].trim().split(" ");
                for (String word : allData) {
                    wordCounter.merge(word, 1, Integer::sum);
                }
            }
            return wordCounter;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void run() throws IOException, NullPointerException {
        long startTime = System.nanoTime();
        File[] files = LoadFiles.loadFiles(Constants.rawYear());
        HashMap<String, Integer> words = new HashMap<>();
        for (File file : files) {
            Map<String, Integer> fileData = getWords(file);
            assert fileData != null;
            fileData.forEach((word, count) -> words.merge(word, count, Integer::sum));
        }
        ArrayList<String> outMessages = new ArrayList<>();
        LinkedHashMap<String, Integer> sortedWordCounter = new LinkedHashMap<>();
        words.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> sortedWordCounter.put(x.getKey(), x.getValue()));

        int uniqueWords = sortedWordCounter.size();
        int totalWords = sortedWordCounter.values().stream().mapToInt(Integer::intValue).sum();
        outMessages.add(String.format("%d unique words with %d words total\n", uniqueWords, totalWords));
        int i = 1;
        for (Map.Entry<String, Integer> entry : sortedWordCounter.entrySet()) {
            int wordCount = entry.getValue();
            String word = entry.getKey();
            if (word.isBlank()) {
                word = "<message deleted> (timeout by moderator)";
            }
            outMessages.add(String.format("%d: %d uses of %s", i++, wordCount, word));
        }

        File outfile = new File(Constants.pathFolder() + "words.log");
        WriteFile.writeFile(outfile, outMessages);

        double finalTime = (double) (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.printf(("[+] Log generated (%fs)"), finalTime);
    }
}
