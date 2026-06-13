public class DirectoryContainer {
    private static String directory;

    public static synchronized String getBaseDirectory() {
        return directory;
    }

    public static synchronized void setDir(String dir) {
        directory = dir;
    }
}
