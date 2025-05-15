public abstract class FilesystemComponent {
    String name;
    FilesystemComponent(String name) {
        this.name = name;
    }

    abstract void display(String indent);
    abstract long getSize();
    abstract String serialize();
    abstract void deserialize(String serialized);
}
