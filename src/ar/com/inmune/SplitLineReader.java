package ar.com.inmune;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SplitLineReader {
	private BufferedReader br;
	private FileReader fr;
	private String line;
	
	public SplitLineReader(InputStream is) throws IOException {
		br = new BufferedReader(new InputStreamReader(is));
		line = br.readLine();
	}

	public boolean hasMore() {
		return line != null;
	}
	
	public String[] next() {
		try {
			if (line == null) {
				line = br.readLine();
			}
			while (line.startsWith("#")) {
				line = br.readLine();
			}
			String[] ret = line.trim().split(",");
			line = br.readLine();
			return ret;
		} catch (IOException e) {
			return null;
		}
	}

	public void close() throws IOException {
		if (br != null) {
			br.close();
			br = null;
		}
		if (fr != null) {
			fr.close();
			fr = null;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		close();
	}
}
