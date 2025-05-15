import java.io.File;

public class FilesystemApp {
    public static FilesystemComponent buildFromDirectory(File dir) {
        DirectoryComponent root = new DirectoryComponent(dir.getName());
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    root.add(new FileComponent(file.getName(), file.length()));
                } else if (file.isDirectory()) {
                    root.add(buildFromDirectory(file));
                }
            }
        }
        return root;
    }

    public static void main(String[] args) {
        File currentDir = new File(".");
        FilesystemComponent current = buildFromDirectory(currentDir);

        System.out.println("[Original File Tree]");
        current.display("");

        String serialized = current.serialize();
        System.out.println("\n[Serialized Data]\n" + serialized);

        FilesystemComponent copy = new DirectoryComponent(currentDir.getName());
        copy.deserialize(serialized);

        System.out.println("\n[Deserialized File Tree]");
        copy.display("");
    }
}
