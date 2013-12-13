package eu.vikev.android.inftable.db.entities.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import eu.vikev.android.inftable.db.DBHelper;
import eu.vikev.android.inftable.db.MyCoursesTable;
import eu.vikev.android.inftable.db.RoomsTable;
import eu.vikev.android.inftable.db.entities.Course;

public class MyCourseDao {
	private SQLiteDatabase database;
	private DBHelper dbHelper;

	public MyCourseDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	private void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	private void close() {
		dbHelper.close();
	}

	public void insert(String acronym) throws SQLException {
		try {
			this.open();
			ContentValues values = new ContentValues();
			values.put(RoomsTable.COLUMN_NAME, acronym);

			database.insert(MyCoursesTable.TABLE_NAME, null, values);

		} catch (SQLException e) {
			this.close();
		}
	}

	public void insert(Course course) {
		this.insert(course.getAcronym());
	}

	/**
	 * Remove given course from mycourses..
	 * 
	 * @param course
	 *            course instance.
	 */
	public void delete(String acronym) {
		Log.i(MyCourseDao.class.getName(), "Deleting course from my courses...");
		try {
			this.open();

			database.delete(MyCoursesTable.TABLE_NAME,
					MyCoursesTable.COLUMN_COURSE + " = '" + acronym + "'", null);

			this.close();
		} catch (SQLException e) {
			this.close();
		}
	}

	/**
	 * Delete course from my courses.
	 * 
	 * @param course
	 */
	public void delete(Course course) {
		String acronym = course.getAcronym();
		delete(acronym);
	}


}
