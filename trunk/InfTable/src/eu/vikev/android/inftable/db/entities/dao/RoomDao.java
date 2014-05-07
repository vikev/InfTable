package eu.vikev.android.inftable.db.entities.dao;

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

	private void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	private void close() {
		dbHelper.close();
	}

	/**
	 * Insert new room
	 * 
	 * @param name
	 *            Room name
	 * @param description
	 *            Room description
	 * @return The Room with id assigned.
	 */
	public Room insert(String name, String description) {
		Room newRoom = null;
		try {
			this.open();
			ContentValues values = new ContentValues();
			values.put(RoomsTable.COLUMN_NAME, name);
			values.put(RoomsTable.COLUMN_DESCRIPTION, description);

			long insertId = database
					.insert(RoomsTable.TABLE_NAME, null, values);

			Cursor cursor = database.query(RoomsTable.TABLE_NAME,
					RoomsTable.ALL_COLUMNS, RoomsTable.COLUMN_ID + " = '"
							+ insertId + "'", null, null, null, null);

			if (cursor.moveToFirst()) {
				newRoom = cursorToRoom(cursor);
			}
			cursor.close();
			this.close();
		} catch (SQLException e) {
			Log.e(RoomDao.class.getName(), "Couldn't insert room.", e);
			this.close();
		}
		return newRoom;
	}

	/**
	 * Insert new room
	 * 
	 * @param room
	 *            Room entity
	 * @return The Room with id assigned
	 * @throws SQLException
	 */
	public Room insert(Room room) throws SQLException {
		return this.insert(room.getName(), room.getDescription());
	}

	/**
	 * Get a room.
	 * 
	 * @param name
	 *            Room name
	 * @return Room entity
	 */
	public Room getRoomByName(String name) {
		Room room = null;
		try {
			this.open();
			Cursor cursor = database.query(RoomsTable.TABLE_NAME,
					RoomsTable.ALL_COLUMNS, RoomsTable.COLUMN_NAME + " = '"
							+ name + "'", null, null, null, null);

			if (cursor.moveToFirst()) {
				room = cursorToRoom(cursor);
			}
			cursor.close();
			this.close();
		} catch (SQLException e) {
			Log.e(RoomDao.class.getName(), "Couldn't get the room.", e);
			this.close();
		}
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
