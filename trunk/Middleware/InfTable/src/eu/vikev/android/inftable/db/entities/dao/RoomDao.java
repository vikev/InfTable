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

	public Room insert(Room room) throws SQLException {
		return this.insert(room.getName(), room.getDescription());
	}

	// /**
	// * Delete given room.
	// *
	// * @param room
	// * room instance.
	// */
	// public void delete(Room room) throws SQLException {
	// Log.i(RoomDao.class.getName(), "Deleting room...");
	// try {
	// this.open();
	// long id = room.getId();
	//
	// database.delete(RoomsTable.TABLE_NAME, RoomsTable.COLUMN_ID
	// + " = '" + id + "'", null);
	// this.close();
	// } catch (SQLException e) {
	// this.close();
	// throw e;
	// }
	// }

	// TODO: delete
	// /**
	// * @return All rooms.
	// */
	// public List<Room> getAllRooms() throws SQLException {
	// try {
	// this.open();
	// List<Room> rooms = new ArrayList<Room>();
	//
	// Cursor cursor = database.query(RoomsTable.TABLE_NAME,
	// RoomsTable.ALL_COLUMNS, null, null, null, null, null);
	//
	// cursor.moveToFirst();
	//
	// while (!cursor.isAfterLast()) {
	// Room room = cursorToRoom(cursor);
	// rooms.add(room);
	// cursor.moveToNext();
	// }
	//
	// cursor.close();
	// this.close();
	// return rooms;
	// } catch (SQLException e) {
	// this.close();
	// throw e;
	// }
	// }

	// /**
	// * @return Get rooms corresponding to certain conditions.
	// */
	// public List<Room> getRooms(String selection) {
	// try {
	// this.open();
	// List<Room> rooms = new ArrayList<Room>();
	//
	// Cursor cursor = database.query(RoomsTable.TABLE_NAME,
	// RoomsTable.ALL_COLUMNS, selection, null, null, null, null);
	//
	// cursor.moveToFirst();
	//
	// while (!cursor.isAfterLast()) {
	// Room room = cursorToRoom(cursor);
	// rooms.add(room);
	// cursor.moveToNext();
	// }
	//
	// cursor.close();
	// this.close();
	// return rooms;
	// } catch (SQLException e) {
	// Log.e(RoomDao.class.getName(), "Error in getRooms query: " + e);
	// this.close();
	// }
	// return null;
	// }

	

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
