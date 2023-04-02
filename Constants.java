public record Constants() {
    public static String parentDir() {
        return "path/to/chats/folder";
    }
    public static String procYear() {
        return String.format("%s/%s/proc/2023/", Constants.parentDir(), Main.path);
    }
    public static String procMonth() {
        return String.format("%s/%s/proc/2023.04/", Constants.parentDir(), Main.path);
    }
    public static String rawYear() {
        return String.format("%s/%s/raw/2023/", Constants.parentDir(), Main.path);
    }
    public static String rawMonth() {
        return String.format("%s/%s/raw/2023.04/", Constants.parentDir(), Main.path);
    }

    public static String chatterWordDest() {
        return String.format("%s/%s/word count chatter/", Constants.parentDir(), Main.path);
    }

    public static String userWordsDest() {
        return String.format("%s/%s/user words/", Constants.parentDir(), Main.path);
    }
    public static String wordCountDest() {
        return String.format("%s/%s/word count specific/", Constants.parentDir(), Main.path);
    }
    public static String pathFolder() {
        return String.format("%s/%s/", Constants.parentDir(), Main.path);
    }
}
