import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MakeZip {

    private final String targetUser;

    public MakeZip(String targetUser) {
        this.targetUser = targetUser;
    }

    public ArrayList<String> getMessages(File file) throws IOException {
        String line;
        ArrayList<String> userMessages = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            if (line.contains(String.format("<%s>", this.targetUser))) {
                userMessages.add(line);
            }
        }
        br.close();
        return userMessages;
    }

    public void run() throws IOException {
        long startTime = System.nanoTime();
        File outfileDir = new File(String.format("%s/%s/", Constants.procYear(), this.targetUser));
        if (!outfileDir.exists()) {
            if (!outfileDir.mkdir()) {
                System.out.println("Error creating new folder");
            }
        }

        String zipName = String.format("%s/%s - %s.zip", Constants.parentDir(), this.targetUser, Main.path);

        File[] files = LoadFiles.loadFiles(Constants.rawYear());
        if (files != null) {
            for (File file : files) {
                ArrayList<String> fileMessages = new ArrayList<>(getMessages(file));
                String outfileStringName = String.format("%s/%s- %s", outfileDir, this.targetUser, file.getName());
                File outfileName = new File(outfileStringName);
                fileMessages.add(0, String.format("%d messages", fileMessages.size()));
                WriteFile.writeFile(outfileName, fileMessages);
            }
        }
        File[] files1 = LoadFiles.loadFiles(outfileDir.toString());
        this.zipFolder(files1, zipName);

        double finalTime = (double) (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.printf(("[+] Zip generated (%fs)"), finalTime);
    }

    public void zipFolder(File[] files, String outputFileName) {
        if (files != null) {
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputFileName))) {
                for (File file : files) {
                    ZipEntry ze = new ZipEntry(file.getName());
                    zos.putNextEntry(ze);
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
