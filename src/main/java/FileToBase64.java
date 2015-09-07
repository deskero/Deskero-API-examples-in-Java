import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileToBase64 {

    public static String encodeFileToBase64Binary(String path){

        String content = "";
        try {
            List<String> lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());

            for(String line : lines)
            {
                content+=line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(Base64.encodeBase64(content.getBytes(StandardCharsets.UTF_8)));
    }

}
