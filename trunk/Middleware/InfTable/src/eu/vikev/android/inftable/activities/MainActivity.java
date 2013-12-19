package eu.vikev.android.inftable.activities;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import eu.vikev.android.inftable.R;
import eu.vikev.android.inftable.custom.MyTime;
import eu.vikev.android.inftable.db.entities.Building;
import eu.vikev.android.inftable.db.entities.Room;
import eu.vikev.android.inftable.db.entities.TimetableEntry;
import eu.vikev.android.inftable.db.entities.dao.TimetableDao;

public class MainActivity extends Activity {
	private SharedPreferences pref;
	private TimetableDao timetableDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(MainActivity.class.getName(), "MainActivity is opening!");
		super.onCreate(savedInstanceState);

		pref = this.getSharedPreferences("eu.vikev.android.inftable",
				Context.MODE_PRIVATE);

		checkFirstTime();

		setContentView(R.layout.activity_main);
		Log.i(MainActivity.class.getName(), "MainActivity opened!");
		timetableDao = new TimetableDao(this);
		getNextLecture();
	}

	private void getNextLecture() {
		int day = MyTime.CURRENT_DAY();
		TextView tv = (TextView) findViewById(R.id.textView_next_class);
		if (day != Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
			/* It's weekend! */
			tv.setText("No classes in the weekend!");
		} else {
		List<TimetableEntry> entries = timetableDao.getMyTimetableForDay(
					MyTime.CURRENT_SEMESTER(), day,
				MyTime.CURRENT_TIME());

		if (entries.size() == 0) {
			tv.setText("No more classes today!");
		} else {
			TimetableEntry nextClass = entries.get(0);

			String courseAndLocation = "<b>" + nextClass.getCourse().getName()
					+ "</b>";
			courseAndLocation += "<br/>&nbsp;&nbsp;&nbsp;Building: ";
			Building building = nextClass.getBuilding();
			if (building != null) {
				courseAndLocation += "<a href=\"" + building.getMap() + "\">";
				courseAndLocation += building.getDescription();
				courseAndLocation += "</a>";
			} else {
				courseAndLocation += nextClass.getBuildingName();
			}

			Room room = nextClass.getRoom();
			courseAndLocation += "<br/>&nbsp;&nbsp;&nbsp;Room: ";
			if (room != null) {
				courseAndLocation += room.getDescription();
			} else {
				courseAndLocation += nextClass.getRoomName();
			}

			if (!"".equals(nextClass.getComment())) {
				courseAndLocation += "<br/>&nbsp;&nbsp;&nbsp;"
						+ nextClass.getComment();
			}
			tv.setText(Html.fromHtml(courseAndLocation));
			tv.setMovementMethod(LinkMovementMethod.getInstance());
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		checkFirstTime();
		getNextLecture();
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
