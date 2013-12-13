package eu.vikev.android.inftable.db;


public class MyCoursesTable {

	public static final String TABLE_NAME = "mycourses";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_COURSE = "course";


	public static final String[] ALL_COLUMNS = { COLUMN_ID, COLUMN_COURSE };

	protected static final String TRIGGER_FKI_COURSE = "CREATE TRIGGER fki_mycourses_course_courses_acronym BEFORE INSERT ON "
			+ TABLE_NAME
			+ " FOR EACH ROW BEGIN SELECT RAISE(ROLLBACK, 'insert on table \""
			+ TABLE_NAME
			+ "\" violates foreign key constraint') WHERE  (SELECT "
			+ CoursesTable.COLUMN_ACRONYM
			+ " FROM "
			+ CoursesTable.TABLE_NAME
			+ " WHERE "
			+ CoursesTable.COLUMN_ACRONYM
			+ " = NEW."
			+ COLUMN_COURSE + ") IS NULL; END;";

	// Database creation sql statement
	protected static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " (" + COLUMN_ID + " integer, " + COLUMN_COURSE
			+ " text NOT NULL REFERENCES " + CoursesTable.TABLE_NAME + "("
			+ CoursesTable.COLUMN_ACRONYM + "), PRIMARY KEY (" + COLUMN_ID
			+ "), UNIQUE ("
			+ COLUMN_COURSE
			+ ") ON CONFLICT REPLACE);";


}