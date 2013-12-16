package eu.vikev.android.inftable.db.entities.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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

	private void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	private void close() {
		dbHelper.close();
	}

	public Availability insert(String courseAcronym, int year) {
		Availability newAvailability = null;
		try {
			ContentValues values = new ContentValues();
			values.put(AvailabilitiesTable.COLUMN_COURSE, courseAcronym);
			values.put(AvailabilitiesTable.COLUMN_YEAR, year);
			this.close();
			open();

			long insertId = database.insertWithOnConflict(
					AvailabilitiesTable.TABLE_NAME, null, values,
					SQLiteDatabase.CONFLICT_FAIL);


			if (insertId > 0) {
				Cursor cursor = database
						.query(AvailabilitiesTable.TABLE_NAME,
								AvailabilitiesTable.ALL_COLUMNS,
								AvailabilitiesTable.COLUMN_ID + " = '"
										+ insertId + "'", null, null, null,
								null);

				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					newAvailability = cursorToAvailability(cursor);
				}
				cursor.close();
			}
			this.close();

		} catch (SQLException e) {
			this.close();
			Log.e(AvailabilityDao.class.getName(),
					"Couldn't insert availability.", e);
		} catch (Exception e) {
			this.close();
			Log.e(AvailabilityDao.class.getName(),
					"Error while inserting availability.", e);
		}
		return newAvailability;
	}

	public Availability insert(Course course, int year) throws SQLException {
		return this.insert(course.getAcronym(), year);
	}

	public Availability insert(Availability availability) throws SQLException {
		return insert(availability.getCourse().getAcronym(),
				availability.getYear());
	}

	public Availability getAvailabilityByAcronym(String acronym) {
		Availability availability = null;
		try {
			open();
			Cursor cursor = database.query(AvailabilitiesTable.TABLE_NAME,
					AvailabilitiesTable.ALL_COLUMNS,
					AvailabilitiesTable.COLUMN_COURSE + " = '" + acronym + "'",
					null,
					null, null, null);

			cursor.moveToFirst();
			availability = cursorToAvailability(cursor);
			cursor.close();
			close();
		} catch (SQLException e) {
			Log.e(AvailabilityDao.class.getName(), "Couldn't get availability",
					e);
			close();
			
		}
		return availability;
	}

	/** Turn a cursor to an Availability entity */
	private Availability cursorToAvailability(Cursor cursor) {
		CourseDao courseDao = new CourseDao(context);
		Availability availability = new Availability();

		availability.setId(cursor.getLong(0));
		availability
				.setCourse(courseDao.getCourseByAcronym(cursor.getString(1)));
		availability.setYear(cursor.getInt((2)));
		return availability;
	}
}
