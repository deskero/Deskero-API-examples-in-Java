import java.io.*;

public class FileToHex {

    public static String convertToHex(String path) throws IOException {

        InputStream is = new FileInputStream(new File(path));

        int value = 0;
        StringBuilder sbHex = new StringBuilder();

        while ((value = is.read()) != -1) {
            sbHex.append(String.format("%02X", value));
        }
        is.close();

        return sbHex.toString();
    }
}
