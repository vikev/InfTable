package eu.vikev.android.inftable.db.entities.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import eu.vikev.android.inftable.custom.Time;
import eu.vikev.android.inftable.db.DBHelper;
import eu.vikev.android.inftable.db.TimetableTable;
import eu.vikev.android.inftable.db.entities.Building;
import eu.vikev.android.inftable.db.entities.Room;
import eu.vikev.android.inftable.db.entities.TimetableEntry;

public class TimetableDao {
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	private Context context;

	public TimetableDao(Context context) {
		this.context = context;
		dbHelper = new DBHelper(context);
	}

	private void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	private void close() {
		dbHelper.close();
	}

	private TimetableEntry cursorToTimetableEntry(Cursor cursor) {
		TimetableEntry timetableEntry = new TimetableEntry();
		CourseDao courseDao = new CourseDao(context);
		BuildingDao buildingDao = new BuildingDao(context);
		RoomDao roomDao = new RoomDao(context);

		timetableEntry.setId(cursor.getLong(0));
		String courseAcronym = cursor.getString(1);
		timetableEntry.setCourse(courseDao.getCourseByAcronym(courseAcronym));
		timetableEntry.setSemester(cursor.getString(2));
		timetableEntry.setDay(cursor.getString(3));

		int startTime = cursor.getInt(4);
		Time start = new Time(startTime / 100, startTime % 100);
		timetableEntry.setStart(start);

		int endTime = cursor.getInt(5);
		Time end = new Time(endTime / 100, endTime % 100);
		timetableEntry.setEnd(end);

		String buildingName = cursor.getString(6);
		Building building = buildingDao.getBuildingByName(buildingName);
		timetableEntry.setBuilding(building);

		String roomName = cursor.getString(7);
		Room room = roomDao.getRoomByName(roomName);
		timetableEntry.setRoom(room);
		timetableEntry.setComment(cursor.getString(8));

		return timetableEntry;
	}

	/** Inserts a new record in timetable. */
	public TimetableEntry insert(String course, String semester, String day,
			int start, int finish, String buildingName, String roomName,
			String comment) throws SQLException {
		open();
		ContentValues values = new ContentValues();
		values.put(TimetableTable.COLUMN_COURSE, course);
		values.put(TimetableTable.COLUMN_SEMESTER, semester);
		values.put(TimetableTable.COLUMN_DAY, day);
		values.put(TimetableTable.COLUMN_START, start);
		values.put(TimetableTable.COLUMN_FINISH, finish);
		values.put(TimetableTable.COLUMN_BUILDING, buildingName);
		values.put(TimetableTable.COLUMN_ROOM, roomName);
		values.put(TimetableTable.COLUMN_COMMENT, comment);

		long insertId = database
				.insert(TimetableTable.TABLE_NAME, null, values);

		Cursor cursor = database.query(TimetableTable.TABLE_NAME,
				TimetableTable.ALL_COLUMNS, TimetableTable.COLUMN_ID + "="
						+ insertId, null, null, null, null);
		TimetableEntry timetableEntry = cursorToTimetableEntry(cursor);
		cursor.close();
		close();
		return timetableEntry;

	}

	/**
	 * Inserts the given TimetableEntry entity and changes the instance to the
	 * new entity taken from the database.
	 */
	public void insert(TimetableEntry timetableEntry) throws SQLException {
		timetableEntry = insert(timetableEntry.getCourse().getAcronym(),
				timetableEntry.getSemester(), timetableEntry.getDay(),
				timetableEntry.getStart().toInt(), timetableEntry.getEnd()
						.toInt(), timetableEntry.getBuilding().getName(),
				timetableEntry.getRoom().getName(), timetableEntry.getComment());
	}

}
