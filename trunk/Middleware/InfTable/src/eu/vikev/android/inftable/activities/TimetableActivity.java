package eu.vikev.android.inftable.activities;

import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import eu.vikev.android.inftable.MenuActivity;
import eu.vikev.android.inftable.R;
import eu.vikev.android.inftable.db.entities.Building;
import eu.vikev.android.inftable.db.entities.Room;
import eu.vikev.android.inftable.db.entities.TimetableEntry;
import eu.vikev.android.inftable.db.entities.dao.TimetableDao;

public class TimetableActivity extends MenuActivity {
	private int day;
	private int semester;
	private int selectedDay;

	private TimetableDao timetableDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timetable);

		timetableDao = new TimetableDao(this);
		// Create tabs with the days of the week
		final TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("Monday");
		spec1.setContent(R.id.content);
		spec1.setIndicator("Monday");

		TabSpec spec2 = tabHost.newTabSpec("Tuesday");
		spec2.setContent(R.id.content);
		spec2.setIndicator("Tuesday");

		TabSpec spec3 = tabHost.newTabSpec("Wednesday");
		spec3.setContent(R.id.content);
		spec3.setIndicator("Wednesday");

		TabSpec spec4 = tabHost.newTabSpec("Thursday");
		spec4.setContent(R.id.content);
		spec4.setIndicator("Thursday");

		TabSpec spec5 = tabHost.newTabSpec("Friday");
		spec5.setContent(R.id.content);
		spec5.setIndicator("Friday");

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
		tabHost.addTab(spec4);
		tabHost.addTab(spec5);

		// Calendar magic
		Calendar calendar = Calendar.getInstance();
		day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day > Calendar.FRIDAY) {
			day = Calendar.MONDAY;
		}
		selectedDay = day;

		tabHost.setCurrentTab(day - Calendar.MONDAY);

		int month = calendar.get(Calendar.MONTH);
		if (month >= Calendar.JULY) {
			semester = 1;
			RadioButton btn = (RadioButton) findViewById(R.id.radioButton_semester1);
			btn.setChecked(true);
		} else {
			semester = 2;
			RadioButton btn = (RadioButton) findViewById(R.id.radioButton_semester2);
			btn.setChecked(true);
		}

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tab) {
				if ("Monday".equals(tab)) {
					selectedDay = Calendar.MONDAY;
				} else if ("Tuesday".equals(tab)) {
					selectedDay = Calendar.TUESDAY;
				} else if ("Wednesday".equals(tab)) {
					selectedDay = Calendar.WEDNESDAY;
				} else if ("Thursday".equals(tab)) {
					selectedDay = Calendar.THURSDAY;
				} else if ("Friday".equals(tab)) {
					selectedDay = Calendar.FRIDAY;
				}

				update();
			}
		});

		update();
	}

	/**
	 * Handle radio button changes
	 * 
	 * @param v
	 */
	public void onRadioButtonClicked(View v) {
		RadioButton btn = (RadioButton) v;
		switch (v.getId()) {
		case R.id.radioButton_semester1:
			if (btn.isChecked()) {
				semester = 1;
			} else {
				semester = 2;
			}
			break;
		case R.id.radioButton_semester2:
			if (btn.isChecked()) {
				semester = 2;
			} else {
				semester = 1;
			}
		}
		update();
	}

	/**
	 * Repopulate the content view.
	 */
	private void update() {
		List<TimetableEntry> entries = timetableDao.getMyTimetableForDay(
				semester, selectedDay);

		LinearLayout ll = (LinearLayout) findViewById(R.id.content);
		ll.removeAllViews();
		String time = "";
		LinearLayout.LayoutParams timeLP = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		timeLP.setMargins(10, 0, 10, 0);

		LinearLayout.LayoutParams courseLP = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		courseLP.setMargins(20, 5, 10, 5);

		for (TimetableEntry entry : entries) {
			String newTime = entry.getStart().toString() + " - "
					+ entry.getEnd().toString();
			if (!time.equalsIgnoreCase(newTime)) {
				time = newTime;
				TextView dayView = new TextView(this);
				dayView.setText(time);
				dayView.setTextSize(25);
				dayView.setLayoutParams(timeLP);
				ll.addView(dayView);
			}

			String courseAndLocation = "<b>" + entry.getCourse().getName()
					+ "</b>";
			courseAndLocation += "<br/>Building: ";
			Building building = entry.getBuilding();
			if (building != null) {
				courseAndLocation += "<a href=\"" + building.getMap() + "\">";
				courseAndLocation += building.getDescription();
				courseAndLocation += "</a>";
			} else {
				courseAndLocation += entry.getBuildingName();
			}

			Room room = entry.getRoom();
			courseAndLocation += " Room: ";
			if (room != null) {
				courseAndLocation += room.getDescription();
			} else {
				courseAndLocation += entry.getRoomName();
			}



			TextView lecture = new TextView(this);
			lecture.setText(Html.fromHtml(courseAndLocation));
			lecture.setTextSize(20);
			lecture.setLayoutParams(courseLP);
			lecture.setMovementMethod(LinkMovementMethod.getInstance());
			ll.addView(lecture);
		}

		if (entries.size() == 0) {
			TextView msg = new TextView(this);
			msg.setText("No lectures today!");
			msg.setTextSize(40);
			msg.setGravity(Gravity.CENTER);
			ll.addView(msg);
		}
	}
}
