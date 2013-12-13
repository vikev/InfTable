package eu.vikev.android.inftable.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import eu.vikev.android.inftable.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(MainActivity.class.getName(), "MainActivity is opening!");
		super.onCreate(savedInstanceState);

		SharedPreferences pref = this.getSharedPreferences(
				"eu.vikev.android.inftable", Context.MODE_PRIVATE);

		if (pref.getBoolean("firstRun", true)) {
			Log.i(MainActivity.class.getName(),
					"This is the first run. Bringing update activity to the front.");
			Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
			startActivity(intent);
			finish();
		}

		setContentView(R.layout.activity_main);
		Log.i(MainActivity.class.getName(), "MainActivity opened!");
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */

	/**
	 * On click open AllCourses activity.
	 * 
	 * @param v
	 */
	public void showAllCoursesActivity(View v) {
		Intent intent = new Intent(MainActivity.this, AllCoursesActivity.class);
		startActivity(intent);
	}


}
