package eu.vikev.android.inftable.db.entities.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import eu.vikev.android.inftable.db.BuildingsTable;
import eu.vikev.android.inftable.db.DBHelper;
import eu.vikev.android.inftable.db.entities.Building;

public class BuildingDao {
	private SQLiteDatabase database;
	private DBHelper dbHelper;


	public BuildingDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Insert a new building in the database.
	 * 
	 * @param euclid
	 *            EUCLID code.
	 * @param acronym
	 *            Building's acronym.
	 * @param name
	 *            Building's name.
	 * @param url
	 *            URL to the building's webpage.
	 * @param drps
	 *            URL to drps.
	 * @param ai
	 *            Is ai building?
	 * @param cg
	 *            Is cg building?
	 * @param cs
	 *            Is cs building?
	 * @param se
	 *            Is se building?
	 * @param level
	 *            Building level.
	 * @param points
	 *            Building points.
	 * @param year
	 *            Typical delivery year.
	 * @param lecturer
	 *            Lecturer's name.
	 * @param deliveryPeriod
	 *            In which semester is the building delivered? S1 - Semester 1;
	 *            S2 - Semester 2; Other options would be disregarded.
	 * @return The building with assigned id.
	 */
	public Building insert(String name, String description, String map) {

		ContentValues values = new ContentValues();
		values.put(BuildingsTable.COLUMN_NAME, name);
		values.put(BuildingsTable.COLUMN_DESCRIPTION, description);
		values.put(BuildingsTable.COLUMN_MAP, map);

		long insertId = database
				.insert(BuildingsTable.TABLE_NAME, null,
				values);

		Cursor cursor = database.query(BuildingsTable.TABLE_NAME,
				BuildingsTable.ALL_COLUMNS, BuildingsTable.COLUMN_ID + " = "
						+ insertId, null,
				null, null, null);

		cursor.moveToFirst();
		Building newBuilding = cursorToBuilding(cursor);
		cursor.close();
		return newBuilding;
	}

	/**
	 * Delete given building.
	 * 
	 * @param building
	 *            building instance.
	 */
	public void delete(Building building) {
		Log.i(BuildingDao.class.getName(), "Deleting building...");
		long id = building.getId();

		database.delete(BuildingsTable.TABLE_NAME,
				BuildingsTable.COLUMN_ID + " = " + id, null);
	}

	/**
	 * @return All buildings.
	 */
	public List<Building> getAllBuildings() {
		List<Building> buildings = new ArrayList<Building>();

		Cursor cursor = database.query(BuildingsTable.TABLE_NAME,
				BuildingsTable.ALL_COLUMNS, null, null, null, null, null);

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			Building building = cursorToBuilding(cursor);
			buildings.add(building);
			cursor.moveToNext();
		}

		cursor.close();
		return buildings;
	}

	public Building getBuildingById(long id) {

		Cursor cursor = database.query(BuildingsTable.TABLE_NAME,
				BuildingsTable.ALL_COLUMNS,
				BuildingsTable.COLUMN_ID + "=" + id, null, null,
				null, null);

		cursor.moveToFirst();
		Building building = cursorToBuilding(cursor);
		cursor.close();

		return building;
	}

	public Building getBuildingByName(String name) {

		Cursor cursor = database
				.query(BuildingsTable.TABLE_NAME,
				BuildingsTable.ALL_COLUMNS, BuildingsTable.COLUMN_NAME + "="
						+ name, null,
				null, null, null);

		cursor.moveToFirst();
		Building building = cursorToBuilding(cursor);
		cursor.close();

		return building;
	}

	/** Turn a cursor to a Building entity */
	private Building cursorToBuilding(Cursor cursor) {
		Building building = new Building();

		building.setId(cursor.getLong(0));
		building.setName(cursor.getString(1));
		building.setDescription(cursor.getString(2));
		building.setMap(cursor.getString(3));

		return building;
	}

	public void insert(Building building) {
		this.insert(building.getName(), building.getDescription(),
				building.getMap());
	}
}
