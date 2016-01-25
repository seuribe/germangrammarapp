package ar.com.inmune;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import ar.com.inmune.deutsch.Word;

public class BrowserActivity extends Activity {
	private static final String URL_WIKTIONARY = "http://de.wiktionary.org/wiki/";
	private static final String EXTRA_KEY_URL = "url";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		savedInstanceState = intent.getExtras();
		String url = (String)intent.getSerializableExtra(EXTRA_KEY_URL);
		if (url != null) {
			WebView webview = new WebView(this);
			webview.loadUrl(url);
			setContentView(webview);
		}
	}
	
	public static void showExternalWordReference(Context ctx, Word word) {
		Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(URL_WIKTIONARY + word.toString()));
		ctx.startActivity(intent);
	}
}
