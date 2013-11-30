package eu.vikev.android.inftable.db.entities.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import eu.vikev.android.inftable.db.AvailabilitiesTable;
import eu.vikev.android.inftable.db.DBHelper;
import eu.vikev.android.inftable.db.entities.Availability;
import eu.vikev.android.inftable.db.entities.Course;

public class AvailabilityDao {
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	private Context context;

	public AvailabilityDao(Context context) {
		this.context = context;
		dbHelper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Availability insert(String courseAcronym, int year) {

		ContentValues values = new ContentValues();
		values.put(AvailabilitiesTable.COLUMN_COURSE, courseAcronym);
		values.put(AvailabilitiesTable.COLUMN_YEAR, year);

		long insertId = database.insert(AvailabilitiesTable.TABLE_NAME, null,
				values);

		Cursor cursor = database.query(AvailabilitiesTable.TABLE_NAME,
				AvailabilitiesTable.ALL_COLUMNS, AvailabilitiesTable.COLUMN_ID
						+ " = " + insertId, null, null, null, null);

		cursor.moveToFirst();
		Availability newAvailability = cursorToRoom(cursor);
		cursor.close();
		return newAvailability;
	}

	public Availability insert(Course course, int year) {
		return this.insert(course.getAcronym(), year);
	}

	public Availability getAvailabilityById(long id) {

		Cursor cursor = database.query(AvailabilitiesTable.TABLE_NAME,
				AvailabilitiesTable.ALL_COLUMNS, AvailabilitiesTable.COLUMN_ID
						+ "=" + id, null, null, null, null);

		cursor.moveToFirst();
		Availability availability = cursorToRoom(cursor);
		cursor.close();

		return availability;
	}

	/** Turn a cursor to a Room entity */
	private Availability cursorToRoom(Cursor cursor) {
		CourseDao courseDao = new CourseDao(context);
		courseDao.open();
		Availability availability = new Availability();

		availability.setId(cursor.getLong(0));
		availability
				.setCourse(courseDao.getCourseByAcronym(cursor.getString(1)));
		availability.setYear(cursor.getInt((2)));
		courseDao.close();
		return availability;
	}
}
