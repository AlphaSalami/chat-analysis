import java.io.*;
import java.util.ArrayList;

public class CheckWordContains {
    private final String targetUser;
    private final String targetWord;

    public CheckWordContains(String targetUser, String targetWord) {
        this.targetUser = targetUser;
        this.targetWord = targetWord;
    }

    public ArrayList<String> getMessages(File file) throws IOException {
        String line;
        ArrayList<String> userMessages = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            if (line.contains(String.format("<%s>", this.targetUser))) {
                if (line.contains(targetWord)) {
                    userMessages.add(line);
                }
            }
        }
        userMessages.add(0, file.getName());
        return userMessages;
    }

    public void run() throws IOException {
        long startTime = System.nanoTime();
        File[] files = LoadFiles.loadFiles(Constants.rawYear());
        ArrayList<ArrayList<String>> totalMessagesArr = new ArrayList<>();
        ArrayList<String> outMessages = new ArrayList<>();
        for (File file: files) {
            ArrayList<String> temp = this.getMessages(file);
            totalMessagesArr.add(temp);
        }

        File outfileDir = new File(String.format("%s/%s/", Constants.userWordsDest(), this.targetUser));
        if (!outfileDir.exists()) {
            if (!outfileDir.mkdir()) {
                System.out.println("Error creating new folder");
            }
        }

        String outfileName = String.format("%s/%s/%s chats.log", Constants.userWordsDest(), this.targetUser, this.targetWord);
        File outfile = new File(outfileName);
        for (ArrayList<String> fileMessage: totalMessagesArr) {
            outMessages.addAll(fileMessage);
        }
        int uses = outMessages.size() - files.length;
        String firstLine = String.format("%d messages containing %s by %s", uses, this.targetWord, this.targetUser);
        outMessages.add(0, firstLine);

        WriteFile.writeFile(outfile, outMessages);

        double finalTime = (double) (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.printf(("[+] Log generated (%fs)"), finalTime);
    }

}
