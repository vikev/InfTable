package eu.vikev.android.inftable.db;

public class CoursesTable {

	public static final String TABLE_NAME = "courses";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_EUCLID = "euclid";
	public static final String COLUMN_ACRONYM = "acronym";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_URL = "url";
	public static final String COLUMN_DRPS = "drps";
	public static final String COLUMN_AI = "ai";
	public static final String COLUMN_CG = "cg";
	public static final String COLUMN_CS = "cs";
	public static final String COLUMN_SE = "se";
	public static final String COLUMN_LEVEL = "level";
	public static final String COLUMN_POINTS = "points";
	public static final String COLUMN_YEAR = "year";
	public static final String COLUMN_LECTURER = "lecturer";
	public static final String COLUMN_DELIVERYPERIOD = "deliveryperiod";

	public static final String[] ALL_COLUMNS = { COLUMN_ID, COLUMN_EUCLID,
			COLUMN_ACRONYM, COLUMN_NAME, COLUMN_URL, COLUMN_DRPS, COLUMN_AI,
			COLUMN_CG, COLUMN_CS, COLUMN_SE, COLUMN_LEVEL, COLUMN_POINTS,
			COLUMN_YEAR, COLUMN_LECTURER, COLUMN_DELIVERYPERIOD };

	// Database creation sql statement
	protected static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " (" + COLUMN_ID + " integer, " + COLUMN_EUCLID
			+ " text, " + COLUMN_ACRONYM + " text NOT NULL, " + COLUMN_NAME
			+ " text, " + COLUMN_URL + " text, " + COLUMN_DRPS + " text, "
			+ COLUMN_AI + " integer DEFAULT NULL, " + COLUMN_CG
			+ " integer DEFAULT NULL, " + COLUMN_CS + " integer DEFAULT NULL, "
			+ COLUMN_SE + " integer DEFAULT NULL, " + COLUMN_LEVEL
			+ " integer DEFAULT NULL, " + COLUMN_POINTS
			+ " integer DEFAULT NULL, " + COLUMN_YEAR
			+ " integer DEFAULT NULL, " + COLUMN_LECTURER + " text, "
			+ COLUMN_DELIVERYPERIOD + " text, PRIMARY KEY (" + COLUMN_ID
			+ "), UNIQUE (" + COLUMN_ACRONYM + ") ON CONFLICT REPLACE);";

}