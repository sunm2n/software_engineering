import java.util.*;

public class DirectoryComponent extends FilesystemComponent {
    List<FilesystemComponent> children = new ArrayList<>();

    public DirectoryComponent(String name) {
        super(name);
    }

    public void add(FilesystemComponent comp) {
        children.add(comp);
    }

    @Override
    public void display(String indent) {
        long totalSize = getSize();
        System.out.println(indent + name + "/ (total: " + totalSize + " B)");
        for (FilesystemComponent child : children) {
            child.display(indent + "  ");
        }
    }

    @Override
    public long getSize() {
        long sum = 0;
        for (FilesystemComponent child : children) {
            sum += child.getSize();
        }
        return sum;
    }

    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        String encodedName = name.replace(":", "\\:");
        sb.append("DIR:").append(encodedName).append(System.lineSeparator());
        for (FilesystemComponent child : children) {
            String childSerialized = child.serialize();
            for (String line : childSerialized.split("\n")) {
                sb.append("  ").append(line).append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }

    @Override
    public void deserialize(String serialized) {
        children.clear();
        String[] lines = serialized.split("\n");
        Deque<String> queue = new ArrayDeque<>(Arrays.asList(lines));
        parseLines(queue, 0);
    }

    private void parseLines(Deque<String> lines, int level) {
        while (!lines.isEmpty()) {
            String raw = lines.peek();
            if (raw == null) break;

            int indent = countIndent(raw);
            if (indent < level) break;
            if (indent > level) throw new RuntimeException("Invalid indentation level");

            raw = lines.poll();
            if (raw == null) break;
            String line = raw.trim();

            if (line.startsWith("FILE:")) {
                List<String> parts = safeSplit(line);
                String name = parts.get(1).replace("\\:", ":");
                long size = Long.parseLong(parts.get(2));
                add(new FileComponent(name, size));
            } else if (line.startsWith("DIR:")) {
                List<String> parts = safeSplit(line);
                DirectoryComponent dir = new DirectoryComponent(parts.get(1).replace("\\:", ":"));
                dir.parseLines(lines, level + 1);
                add(dir);
            }
        }
    }

    private int countIndent(String line) {
        int count = 0;
        while (line.startsWith("  ")) {
            count++;
            line = line.substring(2);
        }
        return count;
    }

    private List<String> safeSplit(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (escape) {
                sb.append(ch);
                escape = false;
            } else if (ch == '\\') {
                escape = true;
            } else if (ch == ':') {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(ch);
            }
        }
        result.add(sb.toString());
        return result;
    }
}
