package eu.vikev.android.inftable.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "inftable.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {

		try {
			database.execSQL(CoursesTable.TABLE_CREATE);
			Log.i(CoursesTable.class.getName(), "Table courses created.");
			database.execSQL(BuildingsTable.TABLE_CREATE);
			Log.i(CoursesTable.class.getName(), "Table buildings created.");
			database.execSQL(RoomsTable.TABLE_CREATE);
			Log.i(CoursesTable.class.getName(), "Table rooms created.");
			database.execSQL(AvailabilitiesTable.TABLE_CREATE);
			Log.i(CoursesTable.class.getName(), "Table availabilities created.");
			database.execSQL(TimetableTable.TABLE_CREATE);
			Log.i(CoursesTable.class.getName(), "Table timetable created.");
			database.execSQL(AvailabilitiesTable.TRIGGER_FKI_COURSE);
			Log.i(CoursesTable.class.getName(),
					"FK trigger availabilities.course->courses.acronym created.");
			database.execSQL(TimetableTable.TRIGGER_FKI_COURSE);
			Log.i(CoursesTable.class.getName(),
					"FK trigger timetable.course->courses.acronym created.");

			database.execSQL(MyCoursesTable.TABLE_CREATE);
			Log.i(CoursesTable.class.getName(), "Table mycourses created.");
			database.execSQL(MyCoursesTable.TRIGGER_FKI_COURSE);
			Log.i(CoursesTable.class.getName(),
					"FK trigger mycourses.course->courses.acronym created.");

		} catch (Exception e) {
			Log.e(CoursesTable.class.getName(),
					"Couldn't create the database. Error: ", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(CoursesTable.class.getName(),
				"Upgrading database from version "
						+ oldVersion
						+ " to "
						+ newVersion
						+ ", which will destroy all data and would have the same effect as fresh install of the app.");
		db.execSQL("DROP TABLE IF EXISTS " + CoursesTable.TABLE_NAME);
		onCreate(db);
	}
}
