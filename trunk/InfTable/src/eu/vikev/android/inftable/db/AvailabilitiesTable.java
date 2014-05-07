package eu.vikev.android.inftable.db;

public class AvailabilitiesTable {

	public static final String TABLE_NAME = "availabilities";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_COURSE = "course";
	public static final String COLUMN_YEAR = "year";

	public static final String[] ALL_COLUMNS = { COLUMN_ID, COLUMN_COURSE,
			COLUMN_YEAR };

	protected static final String TRIGGER_FKI_COURSE = "CREATE TRIGGER fki_availabilities_course_courses_acronym BEFORE INSERT ON "
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
			+ " text REFERENCES " + CoursesTable.TABLE_NAME + "("
			+ CoursesTable.COLUMN_ACRONYM + "), " + COLUMN_YEAR
			+ " integer, UNIQUE (" + COLUMN_COURSE + ", " + COLUMN_YEAR
			+ ") ON CONFLICT REPLACE, PRIMARY KEY (" + COLUMN_ID + "));";

}