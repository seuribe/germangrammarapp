package ar.com.inmune.deutsch.exercise;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

public class LogEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	public final Date when;
	public final boolean correct;
	public final Exercise.Type type;
	public final Map<String, String> extra;
	
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public LogEntry(Exercise.Type type, boolean correct, Date when, Map<String, String> extra) {
		this.type = type;
		this.correct = correct;
		this.when = when;
		this.extra = extra;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject logEntry = new JSONObject();
		logEntry.put("type", type.name());
		logEntry.put("correct", correct);
		logEntry.put("when", sdf.format(when));
		JSONObject ex = new JSONObject();
		for (String key : extra.keySet()) {
			ex.put(key, extra.get(key));
		}
		logEntry.put("extra", ex);
//		Log.d("LOGENTRY", "Saving: " + logEntry.toString());

		return logEntry;
	}
	
	public static LogEntry fromJSON(JSONObject obj) throws JSONException, ParseException {
//		Log.d("LOGENTRY", "Reading: " + obj.toString());
		Exercise.Type type = Exercise.Type.valueOf(obj.getString("type"));
		boolean correct = obj.getBoolean("correct");
		Date when = null;
		try {
			when = sdf.parse(obj.getString("when"));
		} catch (Exception e) {
			when = new Date();
		}
		Map<String, String> extra = new HashMap<String, String>();
		JSONObject ex = obj.getJSONObject("extra");
		for (@SuppressWarnings("unchecked")
		Iterator<String> iter = ex.keys() ; iter.hasNext() ; ) {
			String key = iter.next();
			extra.put(key, ex.getString(key));
		}
		
		return new LogEntry(type, correct, when, extra);
	}
}
 