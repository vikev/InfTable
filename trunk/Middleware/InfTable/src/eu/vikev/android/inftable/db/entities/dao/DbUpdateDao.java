package eu.vikev.android.inftable.db.entities.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import eu.vikev.android.inftable.db.AvailabilitiesTable;
import eu.vikev.android.inftable.db.BuildingsTable;
import eu.vikev.android.inftable.db.CoursesTable;
import eu.vikev.android.inftable.db.DBHelper;
import eu.vikev.android.inftable.db.MyCoursesTable;
import eu.vikev.android.inftable.db.RoomsTable;
import eu.vikev.android.inftable.db.TimetableTable;

public class DbUpdateDao {
	private SQLiteDatabase database;
	private DBHelper dbHelper;

	public DbUpdateDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	private void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	private void close() {
		dbHelper.close();
	}

	/**
	 * Deletes everything but my courses.
	 */
	public void deleteDB() {
		Log.i(DbUpdateDao.class.getName(), "Dropping the db...");
		try {
			this.open();
			database.delete(AvailabilitiesTable.TABLE_NAME, null, null);
			database.delete(RoomsTable.TABLE_NAME, null, null);
			database.delete(BuildingsTable.TABLE_NAME, null, null);
			database.delete(TimetableTable.TABLE_NAME, null, null);
			database.delete(CoursesTable.TABLE_NAME, null, null);
			this.close();
		} catch (SQLException e) {
			Log.e(DbUpdateDao.class.getName(), "Couldn't drop the table.", e);
			this.close();
		}
	}

	public void removeObsolateMyCourses() {
		Log.i(DbUpdateDao.class.getName(), "Removing obsolate my courses...");
		try {
			this.open();
			String where = MyCoursesTable.COLUMN_COURSE + "NOT IN (SELECT "
					+ CoursesTable.COLUMN_ACRONYM + " AS "
					+ MyCoursesTable.COLUMN_COURSE + " FROM "
					+ CoursesTable.TABLE_NAME + ")";
			database.delete(MyCoursesTable.TABLE_NAME, where, null);
			this.close();
		} catch (SQLException e) {
			Log.e(DbUpdateDao.class.getName(), "Couldn't drop the table.", e);
			this.close();
		}
	}
}
