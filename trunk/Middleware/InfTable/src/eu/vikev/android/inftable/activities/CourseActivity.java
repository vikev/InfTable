package eu.vikev.android.inftable.activities;

import android.content.Intent;
import android.os.Bundle;
import eu.vikev.android.inftable.MenuActivity;
import eu.vikev.android.inftable.R;
import eu.vikev.android.inftable.db.entities.Course;
import eu.vikev.android.inftable.db.entities.dao.CourseDao;

public class CourseActivity extends MenuActivity {
	Course course;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);
		Intent intent = getIntent();
		String acronym = intent.getStringExtra("acronym");
		CourseDao courseDao = new CourseDao(this);
		course = courseDao.getCourseByAcronym(acronym);

		this.setTitle(course.getAcronym() + " - " + course.getName());
	}


}
