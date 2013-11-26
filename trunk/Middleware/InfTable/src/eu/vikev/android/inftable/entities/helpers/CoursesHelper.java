package eu.vikev.android.inftable.entities.helpers;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CoursesHelper extends SQLiteOpenHelper {

	public static final String TABLE_COURSES = "courses";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_EUCLID = "euclid";
	public static final String COLUMN_ACRONYM = "acronym";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_URL = "url";
	public static final String COLUMN_DRPS = "drps";
	public static final String COLUMN_AI = "ai";
	public static final String COLUMN_CG = "cg";
	public static final String COLUMN_CS = "cs";
	public static final String COLUMN_SE = "se";
	public static final String COLUMN_LEVEL = "level";
	public static final String COLUMN_POINTS = "points";
	public static final String COLUMN_YEAR = "year";
	public static final String COLUMN_LECTURER = "lecturer";
	public static final String COLUMN_DELIVERYPERIOD = "deliveryperiod";

	private static final String DATABASE_NAME = "inftable.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_COURSES + " (" + COLUMN_ID + " integer, " + COLUMN_EUCLID
			+ " text, " + COLUMN_ACRONYM + " text NOT NULL, " + COLUMN_NAME
			+ " text, " + COLUMN_URL + " text, " + COLUMN_DRPS + " text, "
			+ COLUMN_AI + " integer DEFAULT NULL, " + COLUMN_CG
			+ " integer DEFAULT NULL, " + COLUMN_CS + " integer DEFAULT NULL, "
			+ COLUMN_SE + " integer DEFAULT NULL, " + COLUMN_LEVEL
			+ " integer DEFAULT NULL, " + COLUMN_POINTS
			+ " integer DEFAULT NULL, " + COLUMN_YEAR
			+ " integer DEFAULT NULL, " + COLUMN_LECTURER + " text, "
			+ COLUMN_DELIVERYPERIOD + " text, PRIMARY KEY (" + COLUMN_ID
			+ "), UNIQUE (" + COLUMN_ACRONYM + "));";

	public CoursesHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		try {
			database.execSQL(DATABASE_CREATE);
			Log.i(CoursesHelper.class.getName(), "Courses table created.");
		} catch (SQLException e) {
			Log.e(CoursesHelper.class.getName(),
					"Couldn't create table courses. Exception: ", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(CoursesHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
		onCreate(db);
	}

}