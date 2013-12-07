package eu.vikev.android.inftable.db.entities.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
			String comment) {
		TimetableEntry timetableEntry = null;
		try {
			this.close();
			this.open();

			ContentValues values = new ContentValues();
			values.put(TimetableTable.COLUMN_COURSE, course);
			values.put(TimetableTable.COLUMN_SEMESTER, semester);
			values.put(TimetableTable.COLUMN_DAY, day);
			values.put(TimetableTable.COLUMN_START, start);
			values.put(TimetableTable.COLUMN_FINISH, finish);
			values.put(TimetableTable.COLUMN_BUILDING, buildingName);
			values.put(TimetableTable.COLUMN_ROOM, roomName);
			values.put(TimetableTable.COLUMN_COMMENT, comment);

			long insertId = database.insertWithOnConflict(
					TimetableTable.TABLE_NAME, null, values,
					SQLiteDatabase.CONFLICT_FAIL);

			if (insertId > 0) {
				Cursor cursor = database.query(TimetableTable.TABLE_NAME,
						TimetableTable.ALL_COLUMNS, TimetableTable.COLUMN_ID
								+ " = '" + insertId + "'", null, null, null,
						null);
				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					timetableEntry = cursorToTimetableEntry(cursor);
				}
				cursor.close();
			}
			this.close();
		} catch (SQLException e) {
			this.close();
			Log.e(TimetableDao.class.getName(),
					"Couldn't open the database. Error: " + e);
		} catch (Exception e) {
			this.close();
			Log.e(TimetableDao.class.getName(),
					"Error while inserting to timetable.", e);
		}
		return timetableEntry;

	}

	/**
	 * Inserts the given TimetableEntry entity and changes the instance to the
	 * new entity taken from the database.
	 */
	public void insert(TimetableEntry timetableEntry) {
		timetableEntry = insert(timetableEntry.getCourse().getAcronym(),
				timetableEntry.getSemester(), timetableEntry.getDay(),
				timetableEntry.getStart().toInt(), timetableEntry.getEnd()
						.toInt(), timetableEntry.getBuilding().getName(),
				timetableEntry.getRoom().getName(), timetableEntry.getComment());
	}

	public List<TimetableEntry> getTimetableEntriesByCourseAcronym(
			String acronym) {
		List<TimetableEntry> entries = new ArrayList<TimetableEntry>();

		try {
			this.open();

			Cursor cursor = database.query(TimetableTable.TABLE_NAME,
					TimetableTable.ALL_COLUMNS, TimetableTable.COLUMN_COURSE
							+ "= '" + acronym + "'", null, null, null, null);

			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {
				TimetableEntry entry = cursorToTimetableEntry(cursor);
				entries.add(entry);
				cursor.moveToNext();
			}

			cursor.close();
			this.close();
			return entries;
		} catch (SQLException e) {
			this.close();
			throw e;
		}
	}

}
