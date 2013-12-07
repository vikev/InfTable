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

	protected static final String TRIGGER_FKI_COURSE = "CREATE TRIGGER fki_timetable_course_courses_acronym BEFORE INSERT ON "
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

	protected static final String TRIGGER_FKI_BUILDING = "CREATE TRIGGER fki_timetable_building_buildings_name BEFORE INSERT ON "
			+ TABLE_NAME
			+ " FOR EACH ROW BEGIN SELECT RAISE(ROLLBACK, 'insert on table \""
			+ TABLE_NAME
			+ "\" violates foreign key constraint') WHERE  (SELECT "
			+ BuildingsTable.COLUMN_NAME
			+ " FROM "
			+ BuildingsTable.TABLE_NAME
			+ " WHERE "
			+ BuildingsTable.COLUMN_NAME
			+ " = NEW."
			+ COLUMN_BUILDING + ") IS NULL; END;";

	protected static final String TRIGGER_FKI_ROOM = "CREATE TRIGGER fki_timetable_room_rooms_name BEFORE INSERT ON "
			+ TABLE_NAME
			+ " FOR EACH ROW BEGIN SELECT RAISE(ROLLBACK, 'insert on table \""
			+ TABLE_NAME
			+ "\" violates foreign key constraint') WHERE  (SELECT "
			+ RoomsTable.COLUMN_NAME
			+ " FROM "
			+ RoomsTable.TABLE_NAME
			+ " WHERE "
			+ RoomsTable.COLUMN_NAME
			+ " = NEW."
			+ COLUMN_ROOM
			+ ") IS NULL; END;";

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
