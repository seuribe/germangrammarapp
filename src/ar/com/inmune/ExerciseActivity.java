package ar.com.inmune;

import java.util.Date;

import ar.com.inmune.R;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import ar.com.inmune.deutsch.exercise.Exercise;
import ar.com.inmune.deutsch.exercise.ExerciseLogger;
import ar.com.inmune.deutsch.exercise.LogEntry;
import ar.com.inmune.deutsch.exercise.builder.ExerciseBuilder;

public class ExerciseActivity extends Activity implements OnClickListener {

	private static final int AUTO_NEXT_DELAY = 2500;

	public static final String EXTRA_KEY_TYPE = "type";

	private Exercise ex;
	private ExerciseBuilder builder;
	private Exercise.Type type;
	private TextView result;
	private ExerciseLogger logger;
	private boolean answered = false;
	private Runnable autoNext;

	private TextView questionView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null) {
			savedInstanceState = intent.getExtras();
		}

		setContentView(R.layout.exercise_layout);
		result = (TextView)findViewById(R.id.resultView);
		type = ((Exercise.Type)savedInstanceState.get(EXTRA_KEY_TYPE));
		builder = type.getBuilder();
		logger = GermanPractice.logger;
		setTitle(type.toString());

		setExercise(builder.getRandomExercise());

	}

	private void setExercise(final Exercise ex) {
		this.ex = ex;
		this.answered  = false;
		result.removeCallbacks(autoNext);

		questionView = (TextView) findViewById(R.id.questionView);
		questionView.setText(ex.getQuestion());

		((Button)findViewById(R.id.nextButton)).setText(R.string.skip);

		result.setText("");
		LinearLayout options = (LinearLayout) findViewById(R.id.optionsView);
		options.removeAllViews();
		boolean even = true;
		for (int i = 0 ; i < ex.options.size() ; i++) {
			View buttonLayout = getLayoutInflater().inflate(R.layout.option_layout, options);
			Button button = (Button) buttonLayout.findViewById(R.id.option_button);
			button.setId(i);
			button.setText(ex.options.get(i));
			button.setOnClickListener(this);
			even = !even;
		}
	}

	public void showHelp() {
		HelpActivity.showStaticHTML(this, getString(R.string.exercise_help_title), ex.type.help());
	}

	public void showCheat() {
		HelpActivity.showWordReference(this, ex.help);
	}
	
	public void showFeedback() {
		Intent intent = new Intent(this, FeedbackActivity.class);
		intent.putExtra(FeedbackActivity.EXTRA_TYPE, ex.type.toString());
		intent.putExtra(FeedbackActivity.EXTRA_QUESTION, ex.getQuestion());
		intent.putExtra(FeedbackActivity.EXTRA_CORRECT, ex.correctStr());
		startActivity(intent);
	}

	public void nextExercise(View v) {
		setExercise(builder.getRandomExercise());
	}
	
	@Override
	public void onClick(View v) {
		if (answered) {
			return;
		}
		int clicked = v.getId();
		boolean correct = clicked == ex.correct;
		
		questionView.setText(ex.getSentence());
		
		highlightAnswered(ex.correct, clicked);
		
		((Button)findViewById(R.id.nextButton)).setText(R.string.next);
		
		answered = true;
		String text = String.format(
				getString(correct ? R.string.answer_right : R.string.answer_not_right),
						ex.options.get(ex.correct));
		result.setText(text);
		logger.log(new LogEntry(type, correct, new Date(), ex.extra));

		if (correct) {
			autoNext = new Runnable() {
				@Override
				public void run() {
					nextExercise(null);
				}
			};
			result.postDelayed(autoNext, AUTO_NEXT_DELAY);
		}
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.exercise_menu, menu);
		return true;
	}
	
	@Override public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_cheat:
				showCheat();
				break;
			case R.id.action_help:
				showHelp();
				break;
			case R.id.action_error:
				showFeedback();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void highlightAnswered(int correctId, int clickedId) {
		LinearLayout options = (LinearLayout) findViewById(R.id.optionsView);
		int n = options.getChildCount();
		Resources res = getResources();
		for (int i = 0 ; i < n ; i++) {
			ViewGroup parent = (ViewGroup)options.getChildAt(i);
			Button b = (Button) parent.getChildAt(0);;
			int id = b.getId();
			if (id == correctId) { // correct
				b.setTextColor(res.getColor(R.color.right_answer));
			} else if (id == clickedId) { // chosen, but not correct
				b.setTextColor(res.getColor(R.color.wrong_answer));
			}
			b.setEnabled(false);
		}
	}
}
