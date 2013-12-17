package eu.vikev.android.inftable.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import eu.vikev.android.inftable.R;
import eu.vikev.android.inftable.xml.XmlParser;

public class UpdateActivity extends Activity {

	SharedPreferences pref;

	EditText editCoursesURL;
	EditText editVenuesURL;
	EditText editTimetableURL;
	private boolean firstRun;

	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_update);

		pref = this.getSharedPreferences("eu.vikev.android.inftable",
				Context.MODE_PRIVATE);

		editCoursesURL = (EditText) findViewById(R.id.input_path_to_courses);
		editVenuesURL = (EditText) findViewById(R.id.input_path_to_venues);
		editTimetableURL = (EditText) findViewById(R.id.input_path_to_timetable);

		String courses = pref
				.getString("courses.xml",
						"http://www.inf.ed.ac.uk/teaching/courses/selp/xml/courses.xml");
		editCoursesURL.setText(courses);

		String venues = pref.getString("venues.xml",
				"http://www.inf.ed.ac.uk/teaching/courses/selp/xml/venues.xml");
		editVenuesURL.setText(venues);

		String timetable = pref
				.getString("timetable.xml",
						"http://www.inf.ed.ac.uk/teaching/courses/selp/xml/timetable.xml");
		editTimetableURL.setText(timetable);

		firstRun = pref.getBoolean("firstRun", true);

		Button btnUpdate = (Button) findViewById(R.id.btn_update_download);

		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				openConfirmationDialog();
			}
		});

	}

	private void openConfirmationDialog() {
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Attention");
		String alert = "";
		if (!firstRun) {
			alert = " If you proceed all data would be deleted and the app would be unusable until successful download of the xml files. Relax! My courses would be preserved.";
		}
		builder.setMessage("To download the xml files an active internet connection is needed. Please bare in mind that charges may occur."
				+ alert);
		builder.setCancelable(true);
		builder.setNegativeButton("No, please exit",
				new CancelOnClickListener());
		builder.setPositiveButton("OK, continue", new OkOnClickListener());

		builder.setInverseBackgroundForced(true);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void getUrls() {
		String coursesUrl = UpdateActivity.this.editCoursesURL.getText()
				.toString();
		String venuesUrl = UpdateActivity.this.editVenuesURL.getText()
				.toString();
		String timetableUrl = UpdateActivity.this.editTimetableURL.getText()
				.toString();

		Editor editor = pref.edit();
		editor.putString("courses.xml", coursesUrl);
		editor.putString("venues.xml", venuesUrl);
		editor.putString("timetable.xml", timetableUrl);
		editor.commit();

		new XmlParser(this).execute(venuesUrl, coursesUrl, timetableUrl);
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			getUrls();

		}
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			UpdateActivity.this.finish();
		}
	}
}
