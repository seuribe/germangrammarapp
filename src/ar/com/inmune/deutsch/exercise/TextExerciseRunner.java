package ar.com.inmune.deutsch.exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class TextExerciseRunner implements ExerciseRunner {

	private final PrintStream out;
	private final InputStream in;

	public TextExerciseRunner(PrintStream out, InputStream in) {
		this.out = out;
		this.in = in;
	}
	
	public boolean test(Exercise ex) {
		out.println(ex.getQuestion());
		boolean first = true;
		for (String opt : ex.options) {
			if (!first) {
				out.print(" / ");
			}
			out.print(opt);
			first = false;
		}
		out.println();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			line = reader.readLine();
			return ex.correctStr().equals(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
