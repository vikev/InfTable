package eu.vikev.android.inftable.db.entities.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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

			cursor.moveToFirst();
			Course newCourse = cursorToCourse(cursor);
			cursor.close();
			close();
			return newCourse;
		} catch (SQLException e) {
			this.close();
			throw e;
		}
	}

	/**
	 * Delete given course.
	 * 
	 * @param course
	 *            course instance.
	 */
	public void delete(Course course) throws SQLException {
		Log.i(CourseDao.class.getName(), "Deleting course...");
		try {
			this.open();
			long id = course.getId();
			database.delete(CoursesTable.TABLE_NAME, CoursesTable.COLUMN_ID
					+ " = '" + id + "'", null);
			this.close();
		} catch (SQLException e) {
			this.close();
			throw e;
		}
	}

	/**
	 * @return All courses.
	 */
	public List<Course> getAllCourses() throws SQLException {
		try {
			this.open();
			List<Course> courses = new ArrayList<Course>();

			Cursor cursor = database.query(CoursesTable.TABLE_NAME,
					CoursesTable.ALL_COLUMNS, null, null, null, null, null);

			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {
				Course course = cursorToCourse(cursor);
				courses.add(course);
				cursor.moveToNext();
			}

			cursor.close();
			close();
			return courses;
		} catch (SQLException e) {
			this.close();
			throw e;
		}
	}

	public Course getCourseById(long id) throws SQLException {
		try {
			this.open();
			Cursor cursor = database.query(CoursesTable.TABLE_NAME,
					CoursesTable.ALL_COLUMNS, CoursesTable.COLUMN_ID + " = '"
							+ id + "'", null, null, null, null);

			cursor.moveToFirst();
			Course course = cursorToCourse(cursor);
			cursor.close();
			this.close();
			return course;
		} catch (SQLException e) {
			this.close();
			throw e;
		}
	}

	public Course getCourseByAcronym(String acronym) throws SQLException {
		try {
			this.open();
			Cursor cursor = database.query(CoursesTable.TABLE_NAME,
					CoursesTable.ALL_COLUMNS, CoursesTable.COLUMN_ACRONYM
							+ " = '" + acronym + "'", null, null, null, null);

			cursor.moveToFirst();
			Course course = cursorToCourse(cursor);
			cursor.close();
			this.close();
			return course;
		} catch (SQLException e) {
			this.close();
			throw e;
		}
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
	public void insert(Course course) throws SQLException {
		try {
			this.open();
			course = this.insert(course.getEuclid(), course.getAcronym(),
					course.getName(), course.getUrl(), course.getDrps(),
					course.getAi(), course.getCg(), course.getCs(),
					course.getSe(), course.getLevel(), course.getPoints(),
					course.getYear(), course.getLecturer(),
					course.getDeliveryPeriod());
			this.close();
		} catch (SQLException e) {
			this.close();
			throw e;
		}
	}

}
