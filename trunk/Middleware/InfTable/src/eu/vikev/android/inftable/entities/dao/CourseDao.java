package eu.vikev.android.inftable.entities.dao;

import java.util.ArrayList;
import java.util.List;

import eu.vikev.android.inftable.entities.Course;
import eu.vikev.android.inftable.entities.helpers.CoursesHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CourseDao {
	private SQLiteDatabase database;
	private CoursesHelper coursesHelper;

	private String[] allColumns = { CoursesHelper.COLUMN_ID,
			CoursesHelper.COLUMN_EUCLID, CoursesHelper.COLUMN_ACRONYM,
			CoursesHelper.COLUMN_NAME, CoursesHelper.COLUMN_URL,
			CoursesHelper.COLUMN_DRPS, CoursesHelper.COLUMN_AI,
			CoursesHelper.COLUMN_CG, CoursesHelper.COLUMN_CS,
			CoursesHelper.COLUMN_SE, CoursesHelper.COLUMN_LEVEL,
			CoursesHelper.COLUMN_POINTS, CoursesHelper.COLUMN_YEAR,
			CoursesHelper.COLUMN_LECTURER, CoursesHelper.COLUMN_DELIVERYPERIOD };

	public CourseDao(Context context) {
		coursesHelper = new CoursesHelper(context);
	}

	public void open() throws SQLException {
		database = coursesHelper.getWritableDatabase();
	}

	public void close() {
		coursesHelper.close();
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
			String url, String drps, int ai, int cg, int cs,
			int se, int level, int points, int year, String lecturer,
			String deliveryPeriod) {

		ContentValues values = new ContentValues();
		values.put(CoursesHelper.COLUMN_EUCLID, euclid);
		values.put(CoursesHelper.COLUMN_ACRONYM, acronym);
		values.put(CoursesHelper.COLUMN_NAME, name);
		values.put(CoursesHelper.COLUMN_URL, url);
		values.put(CoursesHelper.COLUMN_DRPS, drps);
		values.put(CoursesHelper.COLUMN_AI, ai);
		values.put(CoursesHelper.COLUMN_CG, cg);
		values.put(CoursesHelper.COLUMN_CS, cs);
		values.put(CoursesHelper.COLUMN_SE, se);
		values.put(CoursesHelper.COLUMN_LEVEL, level);
		values.put(CoursesHelper.COLUMN_DELIVERYPERIOD, deliveryPeriod);
		values.put(CoursesHelper.COLUMN_POINTS, points);
		values.put(CoursesHelper.COLUMN_YEAR, lecturer);

		long insertId = database.insert(CoursesHelper.TABLE_COURSES, null,
				values);

		Cursor cursor = database.query(CoursesHelper.TABLE_COURSES, allColumns,
				CoursesHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);

		cursor.moveToFirst();
		Course newCourse = cursorToCourse(cursor);
		cursor.close();
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
		long id = course.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(CoursesHelper.TABLE_COURSES, CoursesHelper.COLUMN_ID
				+ " = " + id, null);
	}

	/**
	 * @return All courses.
	 */
	public List<Course> getAllCourses() {
		List<Course> courses = new ArrayList<Course>();

		Cursor cursor = database.query(CoursesHelper.TABLE_COURSES, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			Course course = cursorToCourse(cursor);
			courses.add(course);
			cursor.moveToNext();
		}

		cursor.close();
		return courses;
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

	public void insert(Course course) {
		this.insert(course.getEuclid(), course.getAcronym(), course.getName(),
				course.getUrl(), course.getDrps(), course.getAi(),
				course.getCg(), course.getCs(), course.getSe(),
				course.getLevel(), course.getPoints(), course.getYear(),
				course.getLecturer(), course.getDeliveryPeriod());

	}

}
