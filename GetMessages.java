import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class GetMessages {
    private String target = "alphasalami";

    private ArrayList<String> filterMessages(File file) throws IOException {
        ArrayList<String> messages = ReadFile.readFile(file);
        ArrayList<String> newMessages = new ArrayList<>();
        for (String line: messages) {
            if (line.contains(String.format("<%s>", target))) {
                newMessages.add(line);
            }
        }
        return newMessages;
    }

    public void run() throws IOException {
        File[] files = LoadFiles.loadFiles(Constants.parentDir());
        if (files == null) {
            return;
        }
        File outfileDirectoryRaw = new File(String.format("%s/", Constants.rawMonth()));
        if (!outfileDirectoryRaw.exists()) {
            if (!outfileDirectoryRaw.mkdir()) {
                System.out.println("Error creating new folder");
            }
        }
        File outfileDirectoryProc = new File(String.format("%s/", Constants.procMonth()));
        if (!outfileDirectoryProc.exists()) {
            if (!outfileDirectoryProc.mkdir()) {
                System.out.println("Error creating new folder");
            }
        }
        File outfileDirectoryMonth = new File(String.format("%s/%s/", Constants.procMonth(), target));
        if (!outfileDirectoryMonth.exists()) {
            if (!outfileDirectoryMonth.mkdir()) {
                System.out.println("Error creating new folder");
            }
        }
        File outfileDirectoryYear = new File(String.format("%s/%s/", Constants.procYear(), target));
        if (!outfileDirectoryYear.exists()) {
            if (!outfileDirectoryYear.mkdir()) {
                System.out.println("Error creating new folder");
            }
        }
        for (File file: files) {
            String newFilename = "alphasalami- " + file.getName();
            File outfileMonth = new File(String.format("%s/%s", outfileDirectoryMonth, newFilename));
            File outfileYear = new File(String.format("%s/%s", outfileDirectoryYear, newFilename));
            if (!outfileMonth.exists()) {
                ArrayList<String> messages = filterMessages(file);
                System.out.printf("%d messages", messages.size());
                messages.add(0, String.format("%d messages", messages.size()));
                WriteFile.writeFile(outfileMonth, messages);
                Files.copy(outfileMonth.toPath(), outfileYear.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            Files.copy(file.toPath(), Paths.get(Constants.rawMonth(), file.getName()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(file.toPath(), Paths.get(Constants.rawYear(), file.getName()), StandardCopyOption.REPLACE_EXISTING);
        }
        for (File file: files) {
            if (!file.delete()) {
                System.out.println("Error removing original file");
            }
        }
    }
}
