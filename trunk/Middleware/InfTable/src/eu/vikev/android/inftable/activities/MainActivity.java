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
	private SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(MainActivity.class.getName(), "MainActivity is opening!");
		super.onCreate(savedInstanceState);

		pref = this.getSharedPreferences(
				"eu.vikev.android.inftable", Context.MODE_PRIVATE);

		checkFirstTime();

		setContentView(R.layout.activity_main);
		Log.i(MainActivity.class.getName(), "MainActivity opened!");
	}

	@Override
	protected void onResume() {
		checkFirstTime();
	}

	private void checkFirstTime() {
		if (pref.getBoolean("firstRun", true)) {
			Log.i(MainActivity.class.getName(),
					"This is the first run. Bringing update activity to the front.");
			Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * On click open AllCourses activity.
	 * 
	 * @param v
	 */
	public void showAllCoursesActivity(View v) {
		Intent intent = new Intent(MainActivity.this, AllCoursesActivity.class);
		startActivity(intent);
	}

	/**
	 * On click open Timatable activity.
	 * 
	 * @param v
	 */
	public void showTimetableActivity(View v) {
		Intent intent = new Intent(MainActivity.this, TimetableActivity.class);
		startActivity(intent);
	}

	/**
	 * On click open MyCourses activity.
	 * 
	 * @param v
	 */
	public void showMyCoursesActivity(View v) {
		Intent intent = new Intent(MainActivity.this, MyCoursesActivity.class);
		startActivity(intent);
	}

	/**
	 * On click open Update activity.
	 * 
	 * @param v
	 */
	public void showUpdateActivity(View v) {
		Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
		startActivity(intent);
	}
}
