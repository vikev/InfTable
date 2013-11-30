package eu.vikev.android.inftable.db.entities.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import eu.vikev.android.inftable.db.DBHelper;
import eu.vikev.android.inftable.db.RoomsTable;
import eu.vikev.android.inftable.db.entities.Room;

public class RoomDao {
	private SQLiteDatabase database;
	private DBHelper dbHelper;

	public RoomDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Room insert(String name, String description) {

		ContentValues values = new ContentValues();
		values.put(RoomsTable.COLUMN_NAME, name);
		values.put(RoomsTable.COLUMN_DESCRIPTION, description);

		long insertId = database.insert(RoomsTable.TABLE_NAME, null, values);

		Cursor cursor = database
				.query(RoomsTable.TABLE_NAME, RoomsTable.ALL_COLUMNS,
						RoomsTable.COLUMN_ID + " = " + insertId, null, null,
						null, null);

		cursor.moveToFirst();
		Room newRoom = cursorToRoom(cursor);
		cursor.close();
		return newRoom;
	}

	public void insert(Room room) {
		room = this.insert(room.getName(), room.getDescription());
	}

	/**
	 * Delete given room.
	 * 
	 * @param room
	 *            room instance.
	 */
	public void delete(Room room) {
		Log.i(RoomDao.class.getName(), "Deleting room...");
		long id = room.getId();

		database.delete(RoomsTable.TABLE_NAME, RoomsTable.COLUMN_ID + " = "
				+ id, null);
	}

	/**
	 * @return All rooms.
	 */
	public List<Room> getAllRooms() {
		List<Room> rooms = new ArrayList<Room>();

		Cursor cursor = database.query(RoomsTable.TABLE_NAME,
				RoomsTable.ALL_COLUMNS, null, null, null, null, null);

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			Room room = cursorToRoom(cursor);
			rooms.add(room);
			cursor.moveToNext();
		}

		cursor.close();
		return rooms;
	}

	public Room getRoomById(long id) {

		Cursor cursor = database.query(RoomsTable.TABLE_NAME,
				RoomsTable.ALL_COLUMNS, RoomsTable.COLUMN_ID + "=" + id, null,
				null, null, null);

		cursor.moveToFirst();
		Room room = cursorToRoom(cursor);
		cursor.close();

		return room;
	}

	public Room getRoomByName(String name) {

		Cursor cursor = database.query(RoomsTable.TABLE_NAME,
				RoomsTable.ALL_COLUMNS, RoomsTable.COLUMN_NAME + "=" + name,
				null, null, null, null);

		cursor.moveToFirst();
		Room room = cursorToRoom(cursor);
		cursor.close();

		return room;
	}

	/** Turn a cursor to a Room entity */
	private Room cursorToRoom(Cursor cursor) {
		Room room = new Room();

		room.setId(cursor.getLong(0));
		room.setName(cursor.getString(1));
		room.setDescription(cursor.getString(2));

		return room;
	}
}
