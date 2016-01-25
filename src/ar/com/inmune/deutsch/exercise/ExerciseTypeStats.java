package ar.com.inmune.deutsch.exercise;

import java.util.HashMap;
import java.util.Map;

import android.util.Pair;

public class ExerciseTypeStats {
	public Exercise.Type type;
	public int total;
	public int correct;
	public Map<String, Pair<Integer, Integer>> extra = new HashMap<String, Pair<Integer, Integer>>();

	public ExerciseTypeStats(Exercise.Type type) {
		this.type = type;
	}
	
	public String toString() {
		return new StringBuilder(type.toString()).append(": ")
			.append(correct).append(" correct of ")
			.append(total).append(" total").toString();
	}
}
