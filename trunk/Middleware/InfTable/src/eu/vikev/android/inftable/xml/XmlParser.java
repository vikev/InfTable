package eu.vikev.android.inftable.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import eu.vikev.android.inftable.R;
import eu.vikev.android.inftable.db.entities.Building;
import eu.vikev.android.inftable.db.entities.Course;
import eu.vikev.android.inftable.db.entities.Room;
import eu.vikev.android.inftable.db.entities.dao.BuildingDao;
import eu.vikev.android.inftable.db.entities.dao.CourseDao;
import eu.vikev.android.inftable.db.entities.dao.RoomDao;

public class XmlParser extends AsyncTask<String, Void, Boolean> {

	private CourseDao courseDao;
	private BuildingDao buildingDao;
	private RoomDao roomDao;
	private Context context;

	public XmlParser(Context context) {
		this.context = context;
		courseDao = new CourseDao(context);
		buildingDao = new BuildingDao(context);
		roomDao = new RoomDao(context);
	}

	protected Boolean doInBackground(String... urls) {
		getVenues(urls[0]);
		getCourses(urls[1]);
		getTimetable(urls[2]);
		return true;
	}

	/**
	 * Downloads venues.xml, parses it and populates the database.
	 * 
	 * @param path
	 *            URL to venues.xml
	 */
	private void getVenues(String path) {
		try {
			Log.i(XmlParser.class.getName(), "Getting venues...");

			URL url = new URL(path);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream stream = url.openStream();

			Document doc = db.parse(new InputSource(stream));
			doc.getDocumentElement().normalize();

			Log.i(XmlParser.class.getName(), "Checking venues.xml validity...");
			if (!XSDValidator.isXmlValid(doc, context.getResources()
					.openRawResource(R.raw.venuesxsd))) {
				Exception e = new Exception("Invalid venues.xml structure!");
				throw e;
			}
			Log.i(XmlParser.class.getName(), "venues.xml valid!");

			NodeList buildingsList = doc.getElementsByTagName("building");
			NodeList roomsList = doc.getElementsByTagName("room");

			/* insert buildings */
			Log.i(XmlParser.class.getName(), "Getting buildings...");
			for (int i = 0; i < buildingsList.getLength(); i++) {
				Node node = buildingsList.item(i);
				NodeList buildingInfo = node.getChildNodes();

				Building newBuilding = new Building();

				for (int j = 0; j < buildingInfo.getLength(); j++) {
					Node curr = buildingInfo.item(j);
					if ("name".equalsIgnoreCase(curr.getNodeName())) {
						newBuilding.setName(curr.getTextContent());
					} else if ("description".equalsIgnoreCase(curr
							.getNodeName())) {
						newBuilding.setDescription(curr.getTextContent());
					} else if ("map".equalsIgnoreCase(curr.getNodeName())) {
						newBuilding.setMap(curr.getTextContent());
					}
				}

				buildingDao.insert(newBuilding);
			}
			Log.i(XmlParser.class.getName(), "Getting buildings done.");

			/* insert rooms */
			Log.i(XmlParser.class.getName(), "Getting rooms...");
			for (int i = 0; i < roomsList.getLength(); i++) {
				Node node = roomsList.item(i);
				NodeList roomInfo = node.getChildNodes();

				Room newRoom = new Room();

				for (int j = 0; j < roomInfo.getLength(); j++) {
					Node curr = roomInfo.item(j);
					if ("name".equalsIgnoreCase(curr.getNodeName())) {
						newRoom.setName(curr.getTextContent());
					} else if ("description".equalsIgnoreCase(curr
							.getNodeName())) {
						newRoom.setDescription(curr.getTextContent());
					}
				}

				roomDao.insert(newRoom);
			}
			Log.i(XmlParser.class.getName(), "Getting rooms done.");

		} catch (Exception e) {
			Log.e(XmlParser.class.getName(), "Getting venues error: " + e);
		}
	}

	/**
	 * Downloads courses.xml, parses it and populates the database.
	 * 
	 * @param path
	 *            URL to courses.xml
	 */
	private void getCourses(String path) {
		try {
			Log.i(XmlParser.class.getName(), "Getting courses...");

			URL url = new URL(path);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();

			Log.i(XmlParser.class.getName(), "Checking courses.xml validity...");
			if (!XSDValidator.isXmlValid(doc, context.getResources()
					.openRawResource(R.raw.coursesxsd))) {
				Exception e = new Exception("Invalid courses.xml structure!");
				throw e;
			}
			Log.i(XmlParser.class.getName(), "courses.xml valid!");

			NodeList coursesList = doc.getElementsByTagName("course");

			for (int i = 0; i < coursesList.getLength(); i++) {
				NodeList courseInfo = coursesList.item(i).getChildNodes();
				Course course = new Course();

				for (int j = 0; j < courseInfo.getLength(); j++) {
					Node entry = courseInfo.item(j);
					if ("url".equalsIgnoreCase(entry.getNodeName())) {
						course.setUrl(entry.getTextContent());
					} else if ("name".equalsIgnoreCase(entry.getNodeName())) {
						course.setName(entry.getTextContent());
					} else if ("drps".equalsIgnoreCase(entry.getNodeName())) {
						course.setDrps(entry.getTextContent());
					} else if ("euclid".equalsIgnoreCase(entry.getNodeName())) {
						course.setEuclid(entry.getTextContent());
					} else if ("acronym".equalsIgnoreCase(entry.getNodeName())) {
						course.setAcronym(entry.getTextContent());
					} else if ("ai".equalsIgnoreCase(entry.getNodeName())) {
						int val = "ai".equalsIgnoreCase(entry.getTextContent()) ? 1
								: 0;
						course.setAi(val);
					} else if ("cg".equalsIgnoreCase(entry.getNodeName())) {
						int val = "cg".equalsIgnoreCase(entry.getTextContent()) ? 1
								: 0;
						course.setCg(val);
					} else if ("cs".equalsIgnoreCase(entry.getNodeName())) {
						int val = "cs".equalsIgnoreCase(entry.getTextContent()) ? 1
								: 0;
						course.setCs(val);
					} else if ("se".equalsIgnoreCase(entry.getNodeName())) {
						int val = "se".equalsIgnoreCase(entry.getTextContent()) ? 1
								: 0;
						course.setSe(val);
					} else if ("level".equalsIgnoreCase(entry.getNodeName())) {
						course.setLevel(Integer.parseInt(entry.getTextContent()));
					} else if ("points".equalsIgnoreCase(entry.getNodeName())) {
						course.setPoints(Integer.parseInt(entry
								.getTextContent()));
					} else if ("year".equalsIgnoreCase(entry.getNodeName())) {
						course.setYear(Integer.parseInt(entry.getTextContent()));
					} else if ("deliveryperiod".equalsIgnoreCase(entry
							.getNodeName())) {
						course.setDeliveryPeriod(entry.getTextContent());
					} else if ("lecturer".equalsIgnoreCase(entry.getNodeName())) {
						course.setLecturer(entry.getTextContent());
					}
				}
				courseDao.insert(course);
			}
			Log.i(XmlParser.class.getName(), "Getting courses done.");
		} catch (Exception e) {
			Log.e(XmlParser.class.getName(), "Getting courses error: " + e);
		}
	}

	/**
	 * Downloads timetable.xml, parses it and populates the database.
	 * 
	 * @param path
	 *            URL to timetable.xml
	 */
	private void getTimetable(String path) {
		try {
			Log.i(XmlParser.class.getName(), "Getting timetable...");

			URL url = new URL(path);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();

			Log.i(XmlParser.class.getName(),
					"Checking timetable.xml validity...");
			if (!XSDValidator.isXmlValid(doc, context.getResources()
					.openRawResource(R.raw.timetablexsd))) {
				Exception e = new Exception("Invalid timetable.xml structure!");
				throw e;
			}
			Log.i(XmlParser.class.getName(), "timetable.xml valid!");

			NodeList timetableList = doc.getElementsByTagName("lecture");

			for (int i = 0; i < timetableList.getLength(); i++) {
				Element lecture = (Element) timetableList.item(i);
				Node time = lecture.getParentNode();
				Node day = time.getParentNode();
				Node semester = day.getParentNode().getParentNode();


				/* Get start and finish. */
				NamedNodeMap timeAttributes = time.getAttributes();
				String start = timeAttributes.getNamedItem("start")
						.getTextContent();

				String finish = timeAttributes.getNamedItem("finish")
						.getTextContent();

				/* Get semester number */
				String semesterNum = semester.getAttributes()
						.getNamedItem("number").getTextContent();

				/* Get course acronym */
				String acronym = lecture.getElementsByTagName("course").item(0)
						.getTextContent();


			}

		} catch (IOException e) {
			Log.e(XmlParser.class.getName(),
					"Couldn't download timetable.xml. No connection or wrong URL. "
							+ e);
		} catch (NullPointerException e) {
			Log.e(XmlParser.class.getName(), "Parsing timetable.xml error: "
					+ e);
		} catch (Exception e) {
			Log.e(XmlParser.class.getName(), "Getting timetable error: " + e);
		}
	}

}
