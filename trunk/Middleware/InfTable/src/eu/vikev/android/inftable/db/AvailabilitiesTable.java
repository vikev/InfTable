package eu.vikev.android.inftable.db;


public class AvailabilitiesTable {

	public static final String TABLE_NAME = "availabilities";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_COURSE = "course";
	public static final String COLUMN_YEAR = "year";

	public static final String[] ALL_COLUMNS = { COLUMN_ID, COLUMN_COURSE,
			COLUMN_YEAR };

	// Database creation sql statement
	protected static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " (" + COLUMN_ID + " integer, " + COLUMN_COURSE
			+ " text REFERENCES " + CoursesTable.TABLE_NAME + "("
			+ CoursesTable.COLUMN_ACRONYM + "), "
			+ COLUMN_YEAR + " integer, PRIMARY KEY ("
			+ COLUMN_ID + "));";


}