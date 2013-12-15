package eu.vikev.android.inftable.db.entities.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import eu.vikev.android.inftable.db.DBHelper;
import eu.vikev.android.inftable.db.MyCoursesTable;
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

	/**
	 * Add new course to my courses.
	 * 
	 * @param acronym
	 *            Acronym of the course to add.
	 */
	public void insert(String acronym) {
		try {
			this.open();
			ContentValues values = new ContentValues();
			values.put(MyCoursesTable.COLUMN_COURSE, acronym);

			database.insert(MyCoursesTable.TABLE_NAME, null, values);

		} catch (SQLException e) {
			Log.e(MyCourseDao.class.getName(),
					"Couldn't add course to my courses.", e);
			this.close();
		}
	}

	/**
	 * Add new course to my courses.
	 * 
	 * @param acronym
	 *            Course instance to add.
	 */
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
			Log.e(MyCourseDao.class.getName(),
					"Couldn't delete course from my courses.", e);
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

	/**
	 * Check if the course is my course.
	 * 
	 * @param acronym
	 *            Acronym of the course.
	 * @return
	 */
	public boolean isMyCourse(String acronym) {
		try {
			this.open();
			Cursor cursor = database.query(MyCoursesTable.TABLE_NAME,
					MyCoursesTable.ALL_COLUMNS, MyCoursesTable.COLUMN_COURSE
							+ "='" + acronym + "'", null, null, null, null);

			boolean isMyCourse = cursor.moveToFirst();
			cursor.close();
			this.close();
			return isMyCourse;
		} catch (SQLException e) {
			Log.e(MyCourseDao.class.getName(),
					"Couldn't check if the course is in my courses.", e);
			this.close();
		}
		return false;
	}

}
