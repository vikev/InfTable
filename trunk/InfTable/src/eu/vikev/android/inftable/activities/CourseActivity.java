package eu.vikev.android.inftable.activities;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import eu.vikev.android.inftable.MenuActivity;
import eu.vikev.android.inftable.R;
import eu.vikev.android.inftable.db.entities.Building;
import eu.vikev.android.inftable.db.entities.Course;
import eu.vikev.android.inftable.db.entities.Room;
import eu.vikev.android.inftable.db.entities.TimetableEntry;
import eu.vikev.android.inftable.db.entities.dao.CourseDao;
import eu.vikev.android.inftable.db.entities.dao.MyCourseDao;
import eu.vikev.android.inftable.db.entities.dao.TimetableDao;

public class CourseActivity extends MenuActivity {

	private Course course;
	private MyCourseDao myCourseDao;
	private CourseDao courseDao;
	private TimetableDao timetableDao;
	private String acronym;
	private List<TimetableEntry> timetableEntries;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(CourseActivity.class.getName(), "Starting activity.");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);
		Intent intent = getIntent();
		acronym = intent.getStringExtra("acronym");
		courseDao = new CourseDao(this);
		myCourseDao = new MyCourseDao(this);
		timetableDao = new TimetableDao(this);
		course = courseDao.getCourseByAcronym(acronym);
		timetableEntries = timetableDao
				.getTimetableEntriesByCourseAcronym(acronym);
		this.setTitle(course.getAcronym() + " - " + course.getName());

		// Inflate the activity
		setTitle();
		setLecturer();
		setYear();
		setLevel();
		setPoints();
		setDegrees();
		setWebPage();
		setDrps();
		setTimes();
	}

	private void setLecturer() {
		TextView lecturer = (TextView) findViewById(R.id.textView_lecturer);
		lecturer.setText("Lecturer: " + course.getLecturer());
	}

	private void setYear() {
		TextView year = (TextView) findViewById(R.id.textView_year);
		year.setText("Year: " + course.getYear());
	}

	private void setLevel() {
		TextView level = (TextView) findViewById(R.id.textView_level);
		level.setText("Level: " + course.getLevel());
	}

	private void setPoints() {
		TextView points = (TextView) findViewById(R.id.textView_points);
		points.setText("Points: " + course.getPoints());
	}

	private void setDegrees() {
		TextView ai = (TextView) findViewById(R.id.textView_ai);
		if (course.getAi() == 1) {
			ai.setBackgroundColor(Color.GREEN);
		} else {
			ai.setBackgroundColor(Color.RED);
		}

		TextView cg = (TextView) findViewById(R.id.textView_cg);
		if (course.getCg() == 1) {
			cg.setBackgroundColor(Color.GREEN);
		} else {
			cg.setBackgroundColor(Color.RED);
		}
		TextView cs = (TextView) findViewById(R.id.textView_cs);
		if (course.getCs() == 1) {
			cs.setBackgroundColor(Color.GREEN);
		} else {
			cs.setBackgroundColor(Color.RED);
		}
		TextView se = (TextView) findViewById(R.id.textView_se);
		if (course.getSe() == 1) {
			se.setBackgroundColor(Color.GREEN);
		} else {
			se.setBackgroundColor(Color.RED);
		}
	}

	private void setWebPage() {
		TextView webPage = (TextView) findViewById(R.id.textView_webpage);
		webPage.setText(course.getUrl());
		Linkify.addLinks(webPage, Linkify.ALL);
	}

	private void setDrps() {
		TextView drpsLbl = (TextView) findViewById(R.id.textView_drps_lbl);
		drpsLbl.setText("Euclid: " + course.getEuclid() + " DRPS:");
		TextView drps = (TextView) findViewById(R.id.textView_drps);
		drps.setText(course.getDrps());
		Linkify.addLinks(drps, Linkify.ALL);
	}

	private void setTitle() {

		TextView lbl = (TextView) findViewById(R.id.textView_course_title);

		lbl.setText(course.getAcronym() + " - " + course.getName());

		ImageButton btn = (ImageButton) findViewById(R.id.btnAddRemove);

		// set button image and onClickListener for it
		if (myCourseDao.isMyCourse(acronym)) {
			btn.setOnClickListener(deleteListener);
			btn.setImageResource(R.drawable.remove);
		} else {
			btn.setOnClickListener(addListener);
			btn.setImageResource(R.drawable.add);
		}

	}

	private void setTimes() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout_course_container);
		String day = "";
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		for (TimetableEntry entry : timetableEntries) {
			if (!day.equalsIgnoreCase(entry.getDay())) {
				day = entry.getDay();
				TextView dayView = new TextView(this);
				dayView.setText(day);
				dayView.setTextSize(20);
				llp.setMargins(10, 0, 0, 0);
				dayView.setLayoutParams(llp);
				ll.addView(dayView);
			}

			String timeAndLocation = entry.getStart().toString();
			timeAndLocation += " - ";
			timeAndLocation += entry.getEnd().toString();
			timeAndLocation += "<br/>&nbsp;&nbsp;&nbsp;Building: ";
			Building building = entry.getBuilding();
			if (building != null) {
				timeAndLocation += "<a href=\"" + building.getMap()
						+ "\">";
				timeAndLocation += building.getDescription();
				timeAndLocation += "</a>";
			} else {
				timeAndLocation += entry.getBuildingName();
			}

			Room room = entry.getRoom();
			timeAndLocation += "<br/>&nbsp;&nbsp;&nbsp;Room: ";
			if (room != null) {
				timeAndLocation += room.getDescription();
			} else {
				timeAndLocation += entry.getRoomName();
			}
			if (!"".equals(entry.getComment())) {
				timeAndLocation += "<br/>&nbsp;&nbsp;&nbsp;"
						+ entry.getComment();
			}

			TextView slot = new TextView(this);
			slot.setText(Html.fromHtml(timeAndLocation));
			slot.setTextSize(15);
		    llp.setMargins(20, 0, 0, 0);
			slot.setLayoutParams(llp);
			slot.setMovementMethod(LinkMovementMethod.getInstance());
			ll.addView(slot);
		}

		if (timetableEntries.size() == 0) {
			TextView slot = new TextView(this);
			slot.setText("No information found.");
			slot.setTextSize(15);
			llp.setMargins(20, 0, 0, 0);
			slot.setLayoutParams(llp);
			ll.addView(slot);
		}

	}

	/**
	 * Removes course from my courses on click.
	 */
	private OnClickListener deleteListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			myCourseDao.delete(acronym);
			((ImageButton) v).setImageResource(R.drawable.add);
			v.setOnClickListener(addListener);
		}
	};

	/**
	 * Adds course to my course on click
	 */
	private OnClickListener addListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			myCourseDao.insert(acronym);
			((ImageButton) v).setImageResource(R.drawable.remove);
			v.setOnClickListener(deleteListener);
		}
	};
}
