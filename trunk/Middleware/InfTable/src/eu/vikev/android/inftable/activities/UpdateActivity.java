package eu.vikev.android.inftable.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import eu.vikev.android.inftable.R;
import eu.vikev.android.inftable.xml.XmlParser;

public class UpdateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new XmlParser(this)
				.execute(
						"http://www.inf.ed.ac.uk/teaching/courses/selp/xml/venues.xml",
						"http://www.inf.ed.ac.uk/teaching/courses/selp/xml/courses.xml",
						"http://www.inf.ed.ac.uk/teaching/courses/selp/xml/timetable.xml");

		setContentView(R.layout.activity_update);

		SharedPreferences pref = this.getSharedPreferences(
				"eu.vikev.android.inftable", Context.MODE_PRIVATE);

		if (!pref.getBoolean("firstRun", false)) {
			Button cancle = (Button) findViewById(R.id.btn_cancle);
			cancle.setVisibility(View.GONE);
		}

	}
}
