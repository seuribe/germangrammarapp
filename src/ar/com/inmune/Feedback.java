package ar.com.inmune;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import ar.com.inmune.deutsch.exercise.Exercise;

public class Feedback {

	// Removed before making project public
	private static final String REPORT_URL = null; 
	
	private static class AsyncReporter extends AsyncTask<String, Void, Boolean> {

		private Context ctx;

		protected void setContext(Context ctx) {
			this.ctx = ctx;
		}
		
		@Override
		protected Boolean doInBackground(String... params) {

			if (REPORT_URL == null) {
				return false;
			}

			String type = params[0];
			String question = params[1];
			String correct = params[2];

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("type", type));
		    nameValuePairs.add(new BasicNameValuePair("question", question));
		    nameValuePairs.add(new BasicNameValuePair("correct", correct));

			try {
		        HttpClient httpClient = new DefaultHttpClient();
				HttpPost post = new HttpPost(REPORT_URL);
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpClient.execute(post);
				return response.getStatusLine().getStatusCode() == 200;
			} catch (Exception e) {
				Log.w("Could not send feedback report", e);
			}
			
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean ok) {
			Toast.makeText(ctx,
					ctx.getString(ok ? R.string.post_feedback_ok : R.string.post_feedback_error),
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void report(Context context, String type, String question, String correct) {
		AsyncReporter ar = new Feedback.AsyncReporter();
		ar.setContext(context);
		ar.execute(type, question, correct);
	}

	public static void report(Context context, Exercise ex) {
		String type = ex.type.toString();
		String question = ex.getQuestion();
		String correct = ex.correctStr();

		report(context, type, question, correct);
	}

}
