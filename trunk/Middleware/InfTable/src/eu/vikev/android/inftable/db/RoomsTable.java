package eu.vikev.android.inftable.db;


public class RoomsTable {

	public static final String TABLE_NAME = "rooms";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";

	public static final String[] ALL_COLUMNS = { COLUMN_ID, COLUMN_NAME,
			COLUMN_DESCRIPTION };

	// Database creation sql statement
	protected static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " (" + COLUMN_ID + " integer, " + COLUMN_NAME
			+ " text NOT NULL, " + COLUMN_DESCRIPTION + " text, PRIMARY KEY ("
			+ COLUMN_ID + "), UNIQUE (" + COLUMN_NAME
			+ ") ON CONFLICT REPLACE);";


}