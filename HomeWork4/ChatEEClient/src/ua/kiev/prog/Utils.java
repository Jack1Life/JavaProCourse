package ua.kiev.prog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    private static final String URL = "http://127.0.0.1";
    private static final int PORT = 8081;

    public static String getURL() {
        return URL + ":" + PORT;
    }

    public static byte[] inputStreamToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;
        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }
}
