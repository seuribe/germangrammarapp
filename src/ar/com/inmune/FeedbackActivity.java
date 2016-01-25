package ar.com.inmune;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class FeedbackActivity extends Activity {

	public static final String EXTRA_TYPE = "type";
	public static final String EXTRA_QUESTION = "question";
	public static final String EXTRA_CORRECT = "correct";
	
	private String type;
	private String question;
	private String correct;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_dialog);
		
		if (savedInstanceState == null) {
			savedInstanceState = getIntent().getExtras();
		}
		type = savedInstanceState.getString(EXTRA_TYPE);
		question = savedInstanceState.getString(EXTRA_QUESTION);
		correct = savedInstanceState.getString(EXTRA_CORRECT);
	}

	public void onFeedbackBack(View v) {
		finish();
	}
	
	public void onFeedbackReport(View v) {
		Feedback.report(this, type, question, correct);
		finish();
	}
/*
	public void cancelFeedback(View v) {
		finish();
	}
	
	public void sendFeedback(View v) {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/html");
		intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ getString(R.string.mail_feedback_email) });
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject));

		StringBuilder text = new StringBuilder();
		text.append("name: ").append(
				((EditText)findViewById(R.id.feedback_name)).getText().toString()).append("\n");
		text.append("email: ").append(
				((EditText)findViewById(R.id.feedback_email)).getText().toString()).append("\n");
		text.append("reason: ").append(
				((Spinner)findViewById(R.id.feedback_reason)).getSelectedItem().toString()).append("\n");
		text.append("comments: ").append(
				((EditText)findViewById(R.id.feedback_comments)).getText().toString()).append("\n");
		TextView questionView = (TextView) findViewById(R.id.questionView);
		text.append("exercise: ").append(questionView.getText());

		intent.putExtra(android.content.Intent.EXTRA_TEXT, text.toString());

		startActivity(Intent.createChooser(intent, getString(R.string.title_send_feedback)));
		finish();
	}
	*/
}
