package eu.vikev.android.inftable;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import eu.vikev.android.inftable.activities.AllCoursesActivity;
import eu.vikev.android.inftable.activities.MainActivity;
import eu.vikev.android.inftable.activities.MyCoursesActivity;
import eu.vikev.android.inftable.activities.TimetableActivity;

/**
 * Same menu for all activities.
 */
public abstract class MenuActivity extends Activity {
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_home:
			openHome();
			break;
		case R.id.menu_all_courses:
			openAllCourses();
			break;
		case R.id.menu_timetable:
			openTimetable();
			break;
		case R.id.menu_my_courses:
			openMyCourses();
		}
		return true;
	}

	private void openMyCourses() {
		Intent intent = new Intent(this, MyCoursesActivity.class);
		startActivity(intent);
	}

	private void openHome() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	private void openAllCourses() {
		Intent intent = new Intent(this, AllCoursesActivity.class);
		startActivity(intent);
	}

	private void openTimetable() {
		Intent intent = new Intent(this, TimetableActivity.class);
		startActivity(intent);
	}
}
