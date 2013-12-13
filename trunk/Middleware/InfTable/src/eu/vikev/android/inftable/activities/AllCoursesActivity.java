package eu.vikev.android.inftable.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import eu.vikev.android.inftable.MenuActivity;
import eu.vikev.android.inftable.R;
import eu.vikev.android.inftable.db.entities.Course;
import eu.vikev.android.inftable.db.entities.dao.CourseDao;

public class AllCoursesActivity extends MenuActivity {

	CourseDao courseDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_courses);
		courseDao = new CourseDao(this);
		getAllCourses();
	}

	/** Get a list with all courses */
	private void getAllCourses() {
		List<Course> courses = courseDao.getAllCourses();
		LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout_all_courses);
		
		for (Course course : courses) {
			TextView tv = new TextView(this);
			tv.setText(course.getName());
			tv.setTextSize(26);
			tv.setHeight(60);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			final String acronym = course.getAcronym();
			
            OnClickListener listener = new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(AllCoursesActivity.this,
							CourseActivity.class);
					intent.putExtra("acronym", acronym);
					startActivity(intent);
				}
            	
			};

			tv.setOnClickListener(listener);

			ll.addView(tv);
		}
	}


}
