package eu.vikev.android.inftable.xmlparsers;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import eu.vikev.android.inftable.db.entities.Building;
import eu.vikev.android.inftable.db.entities.Room;
import eu.vikev.android.inftable.db.entities.dao.BuildingDao;
import eu.vikev.android.inftable.db.entities.dao.RoomDao;

public class VenuesParser extends AsyncTask<String, Void, Boolean> {

	Context context;

	public VenuesParser(Context context) {
		this.context = context;
	}

	// private Exception exception;

	protected Boolean doInBackground(String... urls) {
		try {
			Log.i(VenuesParser.class.getName(), "Getting venues");

			BuildingDao buildingDao = new BuildingDao(context);
			RoomDao roomDao = new RoomDao(context);

			URL url = new URL(urls[0]);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();

			NodeList buildingsList = doc.getElementsByTagName("building");
			NodeList roomsList = doc.getElementsByTagName("room");

			/* insert buildings */
			buildingDao.open();
			for (int i = 0; i < buildingsList.getLength(); i++) {
				Node node = buildingsList.item(i);
				NodeList buildingInfo = node.getChildNodes();

				Building newBuilding = new Building();

				for (int j = 0; j < buildingInfo.getLength(); j++) {
					Node curr = buildingInfo.item(j);
					if ("name".equals(curr.getNodeName())) {
						newBuilding.setName(curr.getTextContent());
					} else if ("description".equals(curr.getNodeName())) {
						newBuilding.setDescription(curr.getTextContent());
					} else if ("map".equals(curr.getNodeName())) {
						newBuilding.setMap(curr.getTextContent());
					}
				}

				buildingDao.insert(newBuilding);
			}
			buildingDao.close();

			/* insert rooms */
			roomDao.open();
			for (int i = 0; i < roomsList.getLength(); i++) {
				Node node = roomsList.item(i);
				NodeList roomInfo = node.getChildNodes();

				Room newRoom = new Room();

				for (int j = 0; j < roomInfo.getLength(); j++) {
					Node curr = roomInfo.item(j);
					if ("name".equals(curr.getNodeName())) {
						newRoom.setName(curr.getTextContent());
					} else if ("description".equals(curr.getNodeName())) {
						newRoom.setDescription(curr.getTextContent());
					}
				}

				roomDao.insert(newRoom);
			}
			roomDao.close();

			return true;
		} catch (Exception e) {
			Log.e(VenuesParser.class.getName(), "Download error: " + e);
		}

		return false;
	}
}
