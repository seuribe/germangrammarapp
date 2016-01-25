package ar.com.inmune;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import ar.com.inmune.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import ar.com.inmune.deutsch.exercise.DataProvider;
import ar.com.inmune.deutsch.exercise.Exercise;
import ar.com.inmune.deutsch.exercise.ExerciseLogger;
import ar.com.inmune.deutsch.exercise.ExerciseTypeStats;

@SuppressWarnings("deprecation")
public class GermanPractice extends TabActivity implements OnClickListener, OnTabChangeListener {

	private static final String BETA_NOTIFICATION_SHOWN = "beta-notification-shown";

	private static final String EXERCISE_LOG_JSON = "exercise_log.json";

	public static ExerciseLogger logger;

	public static DataProvider dataProvider;
	public static GermanPractice germanPractice;

	public static Map<Exercise.Type, ExerciseTypeStats> sessionStats;
	

	public static class ExercisesMenuActivity extends Activity {
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(makeExercisesMenu());
			
		}

		private ListView makeExercisesMenu( ) {
			ListView listView = new ListView(this);
			final Context ctx = this;
			String[] exTypeStrings = new String[Exercise.Type.values().length];

			for (int i = 0 ; i < exTypeStrings.length ; i++) {
				exTypeStrings[i] = Exercise.Type.values()[i].toString();
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, exTypeStrings);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(ctx, ExerciseActivity.class);
					intent.putExtra(ExerciseActivity.EXTRA_KEY_TYPE, Exercise.Type.values()[position]);
					startActivity(intent);
				}
			});
			return listView;
		}
	}

	public static class GrammarMenuActivity extends Activity {
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(makeGrammarMenu());
		}

		private ListView makeGrammarMenu() {
			ListView listView = new ListView(this);
			final Context ctx = this;
			String[] grammarOptions = new String[] {
					getResources().getString(R.string.verbs), 
					getResources().getString(R.string.nouns),
					getResources().getString(R.string.articles) };
			final String[] activityTypes = new String[] {
					WordListActivity.TYPE_VERB,
					WordListActivity.TYPE_NOUN,
					WordListActivity.TYPE_ARTICLE
			};

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, grammarOptions);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(ctx, WordListActivity.class);
					intent.putExtra(WordListActivity.EXTRA_TYPE, activityTypes[position]);
					startActivity(intent);
				}
			});
			return listView;
		}
	}
	
	public static class StatsMenuActivity extends Activity {
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(makeStatsMenu());
		}
		
		@Override
		protected void onResume() {
			super.onResume();
			
			setContentView(makeStatsMenu());
		}
		
		private ListView makeStatsMenu() {
			ListView listView = new ListView(this);
			final Context ctx = this;
			ExerciseTypeStats[] typeStats = new ExerciseTypeStats[Exercise.Type.values().length];

			int i = 0;
			for (Exercise.Type type : Exercise.Type.values()) {
				typeStats[i] = GermanPractice.sessionStats.get(type);
				i++;
			}
			
			ArrayAdapter<ExerciseTypeStats> adapter = new ArrayAdapter<ExerciseTypeStats>(this, android.R.layout.simple_list_item_1, typeStats);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(ctx, ExerciseActivity.class);
					intent.putExtra(ExerciseActivity.EXTRA_KEY_TYPE, Exercise.Type.values()[position]);
					startActivity(intent);
				}
			});
			return listView;
		}
	}
	
	protected void saveExerciseLogger() {
		try {
			FileOutputStream fos = openFileOutput(EXERCISE_LOG_JSON, Context.MODE_PRIVATE);
			JSONObject exJSON = logger.toJSON();
			String str = exJSON.toString();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			bw.write(str);
			bw.flush();
			bw.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Try to read previous log, and if unavailable, create new
	 * @return
	 */
	protected ExerciseLogger getExerciseLogger() {
		try {
			FileInputStream fis = openFileInput(EXERCISE_LOG_JSON);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return ExerciseLogger.fromJSON(new JSONObject(sb.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			// if there was a problem reading the log, delete it, it will anyway be of no use
			deleteFile(EXERCISE_LOG_JSON);
		}
		return new ExerciseLogger();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GermanPractice.germanPractice = this;
		GermanPractice.dataProvider = new DataProvider(this);
		GermanPractice.sessionStats = new HashMap<Exercise.Type, ExerciseTypeStats>();
		for (Exercise.Type type : Exercise.Type.values()) {
			GermanPractice.sessionStats.put(type, new ExerciseTypeStats(type));
		}

		logger = getExerciseLogger();
		
		setContentView(R.layout.activity_german_practice);

		TabHost tabhost = getTabHost();

		addTab(tabhost, getString(R.string.grammar_tab_title), GrammarMenuActivity.class);
		addTab(tabhost, getString(R.string.exercises_tab_title), ExercisesMenuActivity.class);
		addTab(tabhost, getString(R.string.statistics_tab_title), StatsMenuActivity.class);
		tabhost.setOnTabChangedListener(this);
		
		if (!startNotificationShown()) {
			showStartNotification();
		}
	}
	
	private boolean startNotificationShown() {
		SharedPreferences sp = getSharedPreferences("notifications", 0);
		return sp.getBoolean(BETA_NOTIFICATION_SHOWN, false);
	}
	
	private void showStartNotification() {
		new AlertDialog.Builder(this)
			.setTitle(R.string.news_notification_title)
			.setMessage(R.string.news_notification_text)
			.setNegativeButton("Close", new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.show();
		getSharedPreferences("notifications", 0).edit().putBoolean(BETA_NOTIFICATION_SHOWN, true).commit();
	}

	private void sendFeedback() {
		Intent intent = new Intent(Intent.ACTION_SEND)
        	.putExtra(Intent.EXTRA_EMAIL, new String[] { "suribe@gmx.de" })
        	.putExtra(Intent.EXTRA_SUBJECT, "Regarding German Grammar Practice...")
        	.setType("plain/text")
        	;
        	
		startActivity(intent);
	}
	
	private void addTab(TabHost tabhost, String id, Class<?> activityClass) {
		TabSpec tab = tabhost.newTabSpec(id);
		tab.setIndicator(id, getResources().getDrawable(R.drawable.ic_launcher));
		Intent statsIntent = new Intent(this, activityClass);
		tab.setContent(statsIntent);
		tabhost.addTab(tab);
	}

//	protected void doExercises(Type currType) {
//		Intent intent = new Intent(this, ExerciseActivity.class);
//		intent.putExtra(ExerciseActivity.EXTRA_KEY_TYPE, currType);
//		startActivityForResult(intent, 0);
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_german_practice, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_disclaimer:
				HelpActivity.showStaticFile(this, getString(R.string.disclaimer_title), "html/disclaimer.html");
				break;
			case R.id.menu_about:
				HelpActivity.showStaticFile(this, getString(R.string.about_title), "html/about.html");
				break;
			case R.id.menu_feedback:
				sendFeedback();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void showSessionLog(View v) {
		Intent intent = new Intent(this, StatsMenuActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveExerciseLogger();
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void onTabChanged(String tabId) {
//		if (tabId.equals(STATISTICS_TAB)) {
//			System.out.println("Refresh stats!");
////			new TestExerciseBuilders().test();
//		}
	}


}
