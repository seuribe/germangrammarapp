package ar.com.inmune;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import ar.com.inmune.deutsch.Word;
import ar.com.inmune.deutsch.exercise.DataProvider;

public class HelpActivity extends Activity {

	private static final String EXTRA_KEY_TEXT = "word";
	private static final String EXTRA_KEY_URL = "url";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		savedInstanceState = intent.getExtras();
		
		String text = (String)intent.getSerializableExtra(EXTRA_KEY_TEXT);
		if (text != null) {
			WebView webview = new WebView(this);
			webview.loadData(text, "text/html; charset=utf-8", "UTF-8");
			setContentView(webview);
		}

		String url = (String)intent.getSerializableExtra(EXTRA_KEY_URL);
		if (url != null) {
			WebView webview = new WebView(this);
			webview.loadUrl(url);
			setContentView(webview);
		}
	}
	
	public static void showWordReference(final Context ctx, final Word word) {
		showStaticHTML(ctx, word.toString(), HelpMaker.getHelp(ctx, word));
	}

	public static void showStaticHTML(Context ctx, String title, String html) {
		AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        alert.setTitle(title);
        WebView webview = new WebView(ctx);
		webview.loadData(html, "text/html; charset=utf-8", "UTF-8");
		alert.setView(webview);
        alert.setNegativeButton(R.string.close_button, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
            }
        });
        alert.show();
	}

	public static void showStaticFile(Context ctx, String title, String file) {
		try {
			showStaticHTML(ctx, title, DataProvider.readFile(ctx.getAssets().open(file)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
