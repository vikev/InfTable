package eu.vikev.android.inftable.activities;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import eu.vikev.android.inftable.MenuActivity;
import eu.vikev.android.inftable.R;
import eu.vikev.android.inftable.db.entities.Course;
import eu.vikev.android.inftable.db.entities.dao.CourseDao;
import eu.vikev.android.inftable.db.entities.dao.MyCourseDao;

public class MyCoursesActivity extends MenuActivity {

	private CourseDao courseDao;
	private MyCourseDao myCourseDao;
	private LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(AllCoursesActivity.class.getName(), "Opening activity ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_courses);

		courseDao = new CourseDao(this);
		myCourseDao = new MyCourseDao(this);
		ll = (LinearLayout) findViewById(R.id.linearLayout_my_courses);
		getCourses();
	}

	/** Get a list with the courses */
	private void getCourses() {
		List<Course> courses = courseDao.getAllMyCourses();
		for (Course course : courses) {
			displayCourse(course);
		}
		if (courses.size() == 0) {
			TextView msg = new TextView(this);
			msg.setText("There are no courses added to my courses!");
			msg.setTextSize(20);
			msg.setGravity(Gravity.CENTER);
			ll.addView(msg);
		}
	}

	/**
	 * Puts the course in the list at bottom position.
	 * 
	 * @param course
	 *            Course to add.
	 */
	private void displayCourse(Course course) {
		Log.i(AllCoursesActivity.class.getName(),
				"Adding course " + course.getAcronym()
						+ " to the bottom of the list.");

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.courses_list_entry, null);

		TextView lbl = (TextView) view.findViewById(R.id.textView_course_info);
		lbl.setText(course.getAcronym() + " - " + course.getName());

		ImageButton btn = (ImageButton) view.findViewById(R.id.btnAddRemove);

		final String acronym = course.getAcronym();
		// On click listener for the label
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyCoursesActivity.this,
						CourseActivity.class);
				intent.putExtra("acronym", acronym);
				startActivity(intent);
			}

		};

		lbl.setOnClickListener(listener);
		view.setOnClickListener(listener);
		// set button image and onClickListener for it
		if (myCourseDao.isMyCourse(acronym)) {
			btn.setImageResource(R.drawable.remove);
			listener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					myCourseDao.delete(acronym);
					update(v);
				}
			};
		} else {
			btn.setImageResource(R.drawable.add);
			listener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					myCourseDao.insert(acronym);
					update(v);
				}
			};
		}
		btn.setOnClickListener(listener);

		ll.addView(view);
	}

	public void update(View v) {
		Log.i(AllCoursesActivity.class.getName(),
				"Updating the list with courses.");

		LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout_my_courses);
		ll.removeAllViews();
		getCourses();
	}

}
