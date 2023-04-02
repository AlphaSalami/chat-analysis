import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class GenerateLeaderboard {

    private final int topNumber;
    private final String specificUsername;
    private final int specificPosNum;

    public GenerateLeaderboard(int topNumber, String specificUsername, int specificPosNum) {
        this.topNumber = topNumber;
        this.specificUsername = specificUsername;
        this.specificPosNum = specificPosNum;
    }

    public static HashMap<String, Integer> messagesPerUserMultiThreaded(File[] files) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        HashMap<String, Integer> results = new HashMap<>();
        for (File file : files) {
            executor.execute(() -> {
                try {
                    HashMap<String, Integer> fileResults = MessagePerUser.messagesPerUser(file);
                    synchronized (results) {
                        for (String user : fileResults.keySet()) {
                            results.put(user, results.getOrDefault(user, 0) + fileResults.get(user));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
            System.out.println("Something broke");
        }
        return results;
    }

    public void run() throws InterruptedException {
        long startTime = System.nanoTime();
        File[] files = LoadFiles.loadFiles(Constants.rawYear());
        HashMap<String, Integer> data = messagesPerUserMultiThreaded(files);
        LinkedHashMap<String, Integer> sortedData = new LinkedHashMap<>();
        data.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> sortedData.put(x.getKey(), x.getValue()));
        int totalMessages = 0;
        for (int i: data.values()) {
            totalMessages += i;
        }
        int numChatters = data.size();
        System.out.printf(("%d total messages from %d unique chatters\n\n"), totalMessages, numChatters);
        GetResults Results = new GetResults(sortedData, this.topNumber, this.specificUsername, this.specificPosNum);
        Results.getTop();
        Results.getSpecific();
        Results.getByPosition();

        double finalTime = (double) (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.printf(("Time taken: (%fs)"), finalTime);
    }
}
