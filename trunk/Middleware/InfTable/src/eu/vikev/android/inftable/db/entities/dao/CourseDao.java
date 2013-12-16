package eu.vikev.android.inftable.db.entities.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import eu.vikev.android.inftable.db.AvailabilitiesTable;
import eu.vikev.android.inftable.db.CoursesTable;
import eu.vikev.android.inftable.db.DBHelper;
import eu.vikev.android.inftable.db.entities.Course;

public class CourseDao {
	private SQLiteDatabase database;
	private DBHelper dbHelper;

	public CourseDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	private void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	private void close() {
		dbHelper.close();
	}

	/**
	 * Insert a new course in the database.
	 * 
	 * @param euclid
	 *            EUCLID code.
	 * @param acronym
	 *            Course's acronym.
	 * @param name
	 *            Course's name.
	 * @param url
	 *            URL to the course's webpage.
	 * @param drps
	 *            URL to drps.
	 * @param ai
	 *            Is ai course?
	 * @param cg
	 *            Is cg course?
	 * @param cs
	 *            Is cs course?
	 * @param se
	 *            Is se course?
	 * @param level
	 *            Course level.
	 * @param points
	 *            Course points.
	 * @param year
	 *            Typical delivery year.
	 * @param lecturer
	 *            Lecturer's name.
	 * @param deliveryPeriod
	 *            In which semester is the course delivered? S1 - Semester 1; S2
	 *            - Semester 2; Other options would be disregarded.
	 * @return The course with assigned id.
	 */
	public Course insert(String euclid, String acronym, String name,
			String url, String drps, int ai, int cg, int cs, int se, int level,
			int points, int year, String lecturer, String deliveryPeriod)
			throws SQLException {
		Course newCourse = null;
		try {
			this.open();
			ContentValues values = new ContentValues();
			values.put(CoursesTable.COLUMN_EUCLID, euclid);
			values.put(CoursesTable.COLUMN_ACRONYM, acronym);
			values.put(CoursesTable.COLUMN_NAME, name);
			values.put(CoursesTable.COLUMN_URL, url);
			values.put(CoursesTable.COLUMN_DRPS, drps);
			values.put(CoursesTable.COLUMN_AI, ai);
			values.put(CoursesTable.COLUMN_CG, cg);
			values.put(CoursesTable.COLUMN_CS, cs);
			values.put(CoursesTable.COLUMN_SE, se);
			values.put(CoursesTable.COLUMN_LEVEL, level);
			values.put(CoursesTable.COLUMN_DELIVERYPERIOD, deliveryPeriod);
			values.put(CoursesTable.COLUMN_POINTS, points);
			values.put(CoursesTable.COLUMN_YEAR, year);
			values.put(CoursesTable.COLUMN_LECTURER, lecturer);

			long insertId = database.insert(CoursesTable.TABLE_NAME, null,
					values);

			Cursor cursor = database.query(CoursesTable.TABLE_NAME,
					CoursesTable.ALL_COLUMNS, CoursesTable.COLUMN_ID + " = '"
							+ insertId + "'", null, null, null, null);

			if (cursor.moveToFirst()) {
				newCourse = cursorToCourse(cursor);
			}
			cursor.close();
			close();
		} catch (SQLException e) {
			Log.e(CourseDao.class.getName(), "Couldn't insert course.", e);
			this.close();
		}
		return newCourse;
	}

	/**
	 * Delete given course.
	 * 
	 * @param course
	 *            course instance.
	 */
	public void delete(Course course) {
		Log.i(CourseDao.class.getName(), "Deleting course...");
		try {
			this.open();
			long id = course.getId();
			database.delete(CoursesTable.TABLE_NAME, CoursesTable.COLUMN_ID
					+ " = '" + id + "'", null);
			this.close();
		} catch (SQLException e) {
			Log.e(CourseDao.class.getName(), "Couldn't delete course.", e);
			this.close();
		}
	}

	/**
	 * @return All courses.
	 */
	public List<Course> getAllCourses() {
		List<Course> courses = new ArrayList<Course>();
		try {
			this.open();

			Cursor cursor = database.query(CoursesTable.TABLE_NAME,
					CoursesTable.ALL_COLUMNS, null, null, null, null,
					CoursesTable.COLUMN_NAME);

			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {
					Course course = cursorToCourse(cursor);
					courses.add(course);
					cursor.moveToNext();
				}
			}
			cursor.close();
			close();
		} catch (SQLException e) {
			Log.e(CourseDao.class.getName(),
					"Error executing get all courses query. ", e);
			this.close();
		}
		return courses;
	}

	/**
	 * Get course by acronym.
	 * 
	 * @param acronym
	 *            Acronym of the course.
	 * @return Course entity or null if not found.
	 */
	public Course getCourseByAcronym(String acronym) {
		Course course = null;
		try {
			this.open();
			Cursor cursor = database.query(CoursesTable.TABLE_NAME,
					CoursesTable.ALL_COLUMNS, CoursesTable.COLUMN_ACRONYM
							+ " = '" + acronym + "'", null, null, null, null);

			if (cursor.moveToFirst()) {
				course = cursorToCourse(cursor);
			}
			cursor.close();
			this.close();
		} catch (SQLException e) {
			Log.e(CourseDao.class.getName(),
					"Error executing get course query.", e);
			this.close();
		}
		return course;
	}

	/** Turn a cursor to a Course entity */
	private Course cursorToCourse(Cursor cursor) {
		Course course = new Course();

		course.setId(cursor.getLong(0));
		course.setEuclid(cursor.getString(1));
		course.setAcronym(cursor.getString(2));
		course.setName(cursor.getString(3));
		course.setUrl(cursor.getString(4));
		course.setDrps(cursor.getString(5));
		course.setAi(cursor.getInt(6));
		course.setCg(cursor.getInt(7));
		course.setCs(cursor.getInt(8));
		course.setSe(cursor.getInt(9));
		course.setLevel(cursor.getInt(10));
		course.setPoints(cursor.getInt(11));
		course.setYear(cursor.getInt(12));
		course.setLecturer(cursor.getString(13));
		course.setDeliveryPeriod(cursor.getString(14));

		return course;
	}

	/**
	 * Takes a new course and inserts it into the db. After that changes the
	 * input class instance to the one returned by the db after the insertion,
	 * i.e. assigns id
	 */
	public Course insert(Course course) {
		return this.insert(course.getEuclid(), course.getAcronym(),
					course.getName(), course.getUrl(), course.getDrps(),
					course.getAi(), course.getCg(), course.getCs(),
					course.getSe(), course.getLevel(), course.getPoints(),
					course.getYear(), course.getLecturer(),
					course.getDeliveryPeriod());
	}

	/**
	 * Gets courses meeting certain criteria.
	 * 
	 * @param sem1
	 *            Include semester 1?
	 * @param sem2
	 *            Include semester 2?
	 * @param year1
	 *            Include year 1?
	 * @param year2
	 *            Include year 2?
	 * @param year3
	 *            Include year 3?
	 * @param year4
	 *            Include year 4?
	 * @param year5
	 *            Include year 5?
	 * @return List of course entities or empty list on error or whenever no
	 *         courses meet the criteria.
	 */
	public List<Course> getFilteredCourses(String search, boolean sem1,
			boolean sem2, boolean year1, boolean year2, boolean year3,
			boolean year4, boolean year5) {
		List<Course> courses = new ArrayList<Course>();
		try {
			String query = "SELECT ct.* FROM " + CoursesTable.TABLE_NAME
					+ " AS ct WHERE 1";

			if (!"".equals(search)) {
				query += " AND (UPPER(ct." + CoursesTable.COLUMN_ACRONYM
						+ ") LIKE UPPER('%" + search + "%') OR UPPER(ct."
						+ CoursesTable.COLUMN_NAME + ") LIKE UPPER('%" + search
						+ "%') OR UPPER(ct." + CoursesTable.COLUMN_EUCLID
						+ ") LIKE UPPER('%" + search + "%') OR UPPER(ct."
						+ CoursesTable.COLUMN_LECTURER + ") LIKE UPPER('%"
						+ search + "%'))";
			}

			if (!sem1) {
				query += " AND ct." + CoursesTable.COLUMN_DELIVERYPERIOD
						+ "<>'S1'";
			}

			if (!sem2) {
				query += " AND ct." + CoursesTable.COLUMN_DELIVERYPERIOD
						+ "<>'S2'";
			}

			String subquery = "SELECT at." + AvailabilitiesTable.COLUMN_COURSE
					+ " AS " + CoursesTable.COLUMN_ACRONYM + " FROM "
					+ AvailabilitiesTable.TABLE_NAME + " as at WHERE ct."
					+ CoursesTable.COLUMN_ACRONYM + "=at."
					+ AvailabilitiesTable.COLUMN_COURSE + " AND (0";

			query += " AND ((0";
			if (year1) {
				query += " OR ct." + CoursesTable.COLUMN_YEAR + "=1";
				subquery += " OR at." + AvailabilitiesTable.COLUMN_YEAR + "=1";
			}

			if (year2) {
				query += " OR ct." + CoursesTable.COLUMN_YEAR + "=2";
				subquery += " OR at." + AvailabilitiesTable.COLUMN_YEAR + "=2";
			}

			if (year3) {
				query += " OR ct." + CoursesTable.COLUMN_YEAR + "=3";
				subquery += " OR at." + AvailabilitiesTable.COLUMN_YEAR + "=3";
			}

			if (year4) {
				query += " OR ct." + CoursesTable.COLUMN_YEAR + "=4";
				subquery += " OR at." + AvailabilitiesTable.COLUMN_YEAR + "=4";
			}

			if (year5) {
				query += " OR ct." + CoursesTable.COLUMN_YEAR + "=5";
				subquery += " OR at." + AvailabilitiesTable.COLUMN_YEAR + "=5";
			}

			subquery += ")";
			query += ") OR ct." + CoursesTable.COLUMN_ACRONYM + " IN ("
					+ subquery + ")) ORDER BY " + CoursesTable.COLUMN_NAME
					+ " ASC";

			this.open();
			Cursor cursor = database.rawQuery(query, null);

			if (cursor.moveToFirst()) {
				Log.i(CourseDao.class.getName(),
						"Filter query didn't return any results.");
				while (!cursor.isAfterLast()) {
					Course course = cursorToCourse(cursor);
					courses.add(course);
					cursor.moveToNext();
				}
			}
			cursor.close();
			this.close();
		} catch (SQLException e) {
			Log.e(CourseDao.class.getName(),
					"Error executing get filtered courses query.", e);
			this.close();
		}

		return courses;
	}

}
