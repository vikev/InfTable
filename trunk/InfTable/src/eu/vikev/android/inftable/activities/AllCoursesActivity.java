package eu.vikev.android.inftable.activities;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import eu.vikev.android.inftable.MenuActivity;
import eu.vikev.android.inftable.R;
import eu.vikev.android.inftable.db.entities.Course;
import eu.vikev.android.inftable.db.entities.dao.CourseDao;
import eu.vikev.android.inftable.db.entities.dao.MyCourseDao;

public class AllCoursesActivity extends MenuActivity {

	private CourseDao courseDao;
	private MyCourseDao myCourseDao;
	private EditText searchBox;

	private String search = "";
	private boolean sem1 = true;
	private boolean sem2 = true;
	private boolean year1 = true;
	private boolean year2 = true;
	private boolean year3 = true;
	private boolean year4 = true;
	private boolean year5 = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(AllCoursesActivity.class.getName(), "Opening activity "
				+ AllCoursesActivity.class.getName());
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_all_courses);

		courseDao = new CourseDao(this);
		myCourseDao = new MyCourseDao(this);

		// load all courses on activity create
		getCourses();

		// handler for the search bar
		searchBox = (EditText) findViewById(R.id.editText_search);
		searchBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// nothing to do here
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// nothing to do here
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				update(findViewById(R.id.editText_search));

			}

		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		update(null);
	}

	/** Get a list with the courses */
	private void getCourses() {
		List<Course> courses = courseDao.getFilteredCourses(search, sem1, sem2,
				year1, year2, year3, year4, year5);
		for (Course course : courses) {
			displayCourse(course);
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

		LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout_all_courses);

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
				Intent intent = new Intent(AllCoursesActivity.this,
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
		if (v != null) {
			switch (v.getId()) {
			case R.id.editText_search:
				search = ((EditText) v).getText().toString();
				break;
			case R.id.toggleButton_sem1:
				sem1 = !sem1;
				break;
			case R.id.toggleButton_sem2:
				sem2 = !sem2;
				break;
			case R.id.toggleButton_year1:
				year1 = !year1;
				break;
			case R.id.toggleButton_year2:
				year2 = !year2;
				break;
			case R.id.toggleButton_year3:
				year3 = !year3;
				break;
			case R.id.toggleButton_year4:
				year4 = !year4;
				break;
			case R.id.toggleButton_year5:
				year5 = !year5;
				break;
			}
		}

		LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout_all_courses);
		ll.removeAllViews();
		getCourses();
	}

	/**
	 * Clears all filters by reloading the activity
	 * 
	 * @param v
	 */
	public void clearSearch(View v) {
		Intent intent = new Intent(this, AllCoursesActivity.class);
		startActivity(intent);
	}
}
