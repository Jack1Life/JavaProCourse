package task2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@SaveTo(path="./hw3_task2_content.txt")
public class TextContainer {
    private String content;

    public TextContainer(String content) {
        this.content = content;
    }

    @SaveMethod
    public void saveContent(String path) {
        File outFile = new File(path);
        try(OutputStream os = new FileOutputStream(outFile)) {
            os.write(this.content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
