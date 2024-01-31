package de.glowman554.farmworld.utils;

import java.io.*;

public class FileUtils {
    public static String readFile(InputStream inputStream) throws IOException {
        StringWriter out = new StringWriter();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        int read;
        char[] buf = new char[4096];

        while ((read = br.read(buf)) != -1) {
            out.write(buf, 0, read);
        }

        br.close();
        return out.toString();
    }
}
