package ar.com.inmune.deutsch.exercise;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Pair;
import ar.com.inmune.GermanPractice;
import ar.com.inmune.deutsch.exercise.Exercise.Type;

public class ExerciseLogger {

	private List<LogEntry> entries;
	private Map<Exercise.Type, List<LogEntry>> typeIndex;
	
	public ExerciseLogger() {
		entries = new ArrayList<LogEntry>();
		typeIndex = new HashMap<Exercise.Type, List<LogEntry>>();
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject stats = new JSONObject();
		JSONArray entArray = new JSONArray();
		for (LogEntry entry : entries) {
			entArray.put(entry.toJSON());
		}
		stats.put("entries", entArray);
		return stats;
	}
	
	public static ExerciseLogger fromJSON(JSONObject json) throws JSONException, ParseException {
		ExerciseLogger logger = new ExerciseLogger();
		JSONArray entArray = json.getJSONArray("entries");
		for (int i = 0 ; i < entArray.length() ; i++) {
			JSONObject entry = entArray.getJSONObject(i);
			LogEntry le = LogEntry.fromJSON(entry);
			logger.log(le);
		}
		return logger;
	}

	public void log(LogEntry entry) {
		entries.add(entry);
		getTypeList(entry.type).add(entry);
		
		getTypeStats(entry.type).correct += (entry.correct) ? 1 : 0;
		getTypeStats(entry.type).total++;
	}

	private ExerciseTypeStats getTypeStats(Type type) {
		ExerciseTypeStats ts = GermanPractice.sessionStats.get(type);
		if (ts == null) {
			ts = new ExerciseTypeStats(type);
			GermanPractice.sessionStats.put(type, ts);
		}
		return ts;
	}
	
	private List<LogEntry> getTypeList(Type type) {
		List<LogEntry> list = typeIndex.get(type);
		if (list == null) {
			list = new ArrayList<LogEntry>();
			typeIndex.put(type, list);
		}
		return list;
	}
	
	public ExerciseTypeStats collectStats(Exercise.Type type) {
		List<LogEntry> entries = getTypeList(type);
		ExerciseTypeStats stats = new ExerciseTypeStats(type);
		stats.total = entries.size();
		for (LogEntry entry : entries) {
			if (entry.correct) {
				stats.correct++;
			}
			for (String extraKey : entry.extra.keySet()) {
				statAdd(stats, extraKey, entry.correct);
			}
		}
		return stats;
	}

	private void statAdd(ExerciseTypeStats stats, String extraKey, boolean correct) {
		Pair<Integer, Integer> pair = stats.extra.get(extraKey);
		if (pair == null) {
			pair = new Pair<Integer, Integer>(0, 0);
			stats.extra.put(extraKey, pair);
		}
		stats.extra.put(extraKey,  new Pair<Integer, Integer>(pair.first + 1, pair.second + (correct ? 1 : 0)));
	}
	
}
