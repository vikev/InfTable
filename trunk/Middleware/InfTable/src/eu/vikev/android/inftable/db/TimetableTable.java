package eu.vikev.android.inftable.db;

public class TimetableTable {
	public static final String TABLE_NAME = "timetable";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_COURSE = "course";
	public static final String COLUMN_SEMESTER = "semester";
	public static final String COLUMN_DAY = "day";
	public static final String COLUMN_START = "start";
	public static final String COLUMN_FINISH = "finish";
	public static final String COLUMN_BUILDING = "building";
	public static final String COLUMN_ROOM = "room";
	public static final String COLUMN_COMMENT = "comment";

	public static final String[] ALL_COLUMNS = { COLUMN_ID, COLUMN_COURSE,
			COLUMN_SEMESTER, COLUMN_DAY, COLUMN_START, COLUMN_FINISH,
			COLUMN_BUILDING, COLUMN_ROOM, COLUMN_COMMENT };

	// Database creation sql statement
	protected static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " (" + COLUMN_ID + " integer, " + COLUMN_COURSE
			+ " text REFERENCES " + CoursesTable.TABLE_NAME + "("
			+ CoursesTable.COLUMN_ACRONYM + "), " + COLUMN_SEMESTER + " text, "
			+ COLUMN_DAY + " text NOT NULL, " + COLUMN_START
			+ " integer NOT NULL, " + COLUMN_FINISH + " integer NOT NULL, "
			+ COLUMN_BUILDING + " text REFERENCES " + BuildingsTable.TABLE_NAME
			+ "(" + BuildingsTable.COLUMN_NAME + "), " + COLUMN_ROOM
			+ " text REFERENCES " + RoomsTable.TABLE_NAME + "("
			+ RoomsTable.COLUMN_NAME + "), " + COLUMN_COMMENT
			+ " text, PRIMARY KEY (" + COLUMN_ID + "));";
}
