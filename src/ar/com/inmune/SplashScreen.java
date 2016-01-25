package ar.com.inmune;

import ar.com.inmune.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
	private boolean mIsBackButtonPressed;
	private static final int SPLASH_DURATION = 2000; // 2 seconds

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash_screen);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				finish();

				if (!mIsBackButtonPressed) {
					Intent intent = new Intent(SplashScreen.this, GermanPractice.class);
					SplashScreen.this.startActivity(intent);
				}
			}
		}, SPLASH_DURATION);
	}

	@Override
	public void onBackPressed() {
		mIsBackButtonPressed = true;
		super.onBackPressed();
	}
}