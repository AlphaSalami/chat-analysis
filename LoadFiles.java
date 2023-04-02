import java.io.*;

public class LoadFiles {

    public static File[] loadFiles(String filesLocation) {

        FilenameFilter filter = (dir, name) -> name.endsWith(".irc");
        File directory = new File(filesLocation);
        return directory.listFiles(filter);

    }
}
