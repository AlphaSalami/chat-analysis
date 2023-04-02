import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompareWordCount {

    public final String targetWord;

    public CompareWordCount(String targetWord) {
        this.targetWord = targetWord;
    }
    public static HashMap<String, Integer> getWordCounts(File file, String targetWord) {
        try {
            ArrayList<String> data = ReadFile.readFile(file);
            HashMap<String, Integer> wordCounter = new HashMap<>();
            String reg = "(] <\\w+>)";
            Pattern pattern = Pattern.compile(reg);

            for (String line : data) {
                String[] step = line.split(">");
                Matcher matcher = pattern.matcher(line);
                ArrayList<String> chatterNames = new ArrayList<>();
                while (matcher.find()) {
                    String user = matcher.group().substring(3, matcher.group().length() - 1);
                    chatterNames.add(user);
                }
                String[] allWords = step[1].strip().split(" ");
                for (String word : allWords) {
                    if (word.equals(targetWord)) {
                        if (!chatterNames.isEmpty()) {
                            String chatterName = chatterNames.get(0);
                            wordCounter.merge(chatterName, 1, Integer::sum);
                        }
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
        for (File file : files) {
            Map<String, Integer> fileData = getWordCounts(file, this.targetWord);
            assert fileData != null;
            fileData.forEach((word, count) -> words.merge(word, count, Integer::sum));
        }
        ArrayList<String> outMessages = new ArrayList<>();
        LinkedHashMap<String, Integer> sortedWordCounter = new LinkedHashMap<>();
        words.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> sortedWordCounter.put(x.getKey(), x.getValue()));

        int uniqueUsers = sortedWordCounter.size();
        int totalUsers = sortedWordCounter.values().stream().mapToInt(Integer::intValue).sum();
        outMessages.add(String.format("Total of %d uses of %s from %d unique users\n", totalUsers, this.targetWord, uniqueUsers));
        int i = 1;
        for (Map.Entry<String, Integer> entry : sortedWordCounter.entrySet()) {
            int wordCount = entry.getValue();
            String user = entry.getKey();
            outMessages.add(String.format("%d: %s used %s %d times", i++, user, this.targetWord, wordCount));
        }

        File outfile = new File(Constants.wordCountDest() + this.targetWord + " count leaderboard.log");
        WriteFile.writeFile(outfile, outMessages);

        double finalTime = (double) (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.printf(("[+] Log generated (%fs)"), finalTime);
    }
}
