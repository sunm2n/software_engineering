/**
 * Java 표준 라이브러리의 java.io.File 클래스와의 충돌을 피하기 위해
 * PPT의 'File' 클래스는 'FileComponent' 라는 이름으로 구현하였습니다.
 */

public class FileComponent extends FilesystemComponent {
    long size;

    public FileComponent(String name, long size) {
        super(name);
        this.size = size;
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + name + " (" + size + " B)");
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public String serialize() {
        String encodedName = name.replace(":", "\\:");
        return "FILE:" + encodedName + ":" + size;
    }

    @Override
    public void deserialize(String serialized) {
        // FileComponent는 deserialize() 호출 없이 DirectoryComponent 내부에서 생성자만으로 복원됩니다.
        // 따라서 이 메서드는 구현되지 않으며 추상 메서드 구현 요건만 만족합니다.
    }
}
