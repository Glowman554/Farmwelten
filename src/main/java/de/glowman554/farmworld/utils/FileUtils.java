package de.glowman554.farmworld.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class FileUtils
{
	public static String readFile(InputStream inputStream) throws IOException
	{
		StringWriter out = new StringWriter();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

		int read;
		char[] buf = new char[4096];

		while ((read = br.read(buf)) != -1)
		{
			out.write(buf, 0, read);
		}

		br.close();
		return out.toString();
	}
}
