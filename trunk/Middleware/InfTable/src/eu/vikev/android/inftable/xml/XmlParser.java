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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import eu.vikev.android.inftable.R;
import eu.vikev.android.inftable.activities.MainActivity;
import eu.vikev.android.inftable.custom.Time;
import eu.vikev.android.inftable.db.DBHelper;
import eu.vikev.android.inftable.db.entities.Building;
import eu.vikev.android.inftable.db.entities.Course;
import eu.vikev.android.inftable.db.entities.Room;
import eu.vikev.android.inftable.db.entities.dao.AvailabilityDao;
import eu.vikev.android.inftable.db.entities.dao.BuildingDao;
import eu.vikev.android.inftable.db.entities.dao.CourseDao;
import eu.vikev.android.inftable.db.entities.dao.RoomDao;
import eu.vikev.android.inftable.db.entities.dao.TimetableDao;

public class XmlParser extends AsyncTask<String, Void, Boolean> {
	private String errorMsg = "";

	private CourseDao courseDao;
	private BuildingDao buildingDao;
	private RoomDao roomDao;
	private TimetableDao timetableDao;
	private AvailabilityDao availabilityDao;

	private Context context;
	private ProgressDialog dialog;

	public XmlParser(Context context) {
		this.context = context;
		courseDao = new CourseDao(context);
		buildingDao = new BuildingDao(context);
		roomDao = new RoomDao(context);
		timetableDao = new TimetableDao(context);
		availabilityDao = new AvailabilityDao(context);
		dialog = new ProgressDialog(context);
	}

	protected void onPreExecute() {
		dialog.setCancelable(false);
		dialog.setMessage("Downloading data... Please wait.");
		dialog.show();
	}

	protected Boolean doInBackground(String... urls) {
		if (getVenues(urls[0]) && getCourses(urls[1]) && getTimetable(urls[2])) {
			SharedPreferences pref = context.getSharedPreferences(
					"eu.vikev.android.inftable", Context.MODE_PRIVATE);
			Editor editor = pref.edit();
			editor.putBoolean("firstRun", false);
			editor.commit();
			dialog.dismiss();
			context.startActivity(new Intent(context, MainActivity.class));
			((Activity) context).finish();
			return true;
		}
		Log.e(XmlParser.class.getName(), "Couldn't get the necessary data.");
		context.deleteDatabase(DBHelper.DATABASE_NAME);
		dialog.dismiss();
		return false;
	}

	protected void onPostExecute(Boolean success) {
		if (!success) {
			showError();
		}
	}

	private void showError() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Couldn't get the necessary data");
		builder.setMessage(errorMsg);
		builder.setPositiveButton("OK", null);
		builder.create().show();
		System.out.println("Open error dialog....");
	}

	/**
	 * Downloads venues.xml, parses it and populates the database.
	 * 
	 * @param path
	 *            URL to venues.xml
	 */
	private Boolean getVenues(String path) {
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

		} catch (IOException e) {
			errorMsg = "Couldn't download venues.xml. No connection or wrong URL.";
			Log.e(XmlParser.class.getName(),
					"Couldn't download venues.xml. No connection or wrong URL. "
							+ e);
			return false;
		} catch (NullPointerException e) {
			errorMsg = "Parsing venues.xml error. Couldn't validate venues.xml.";
			Log.e(XmlParser.class.getName(), "Parsing venues.xml error: " + e);
			return false;
		} catch (Exception e) {
			errorMsg = "Error occured while parsing venues.xml.";
			Log.e(XmlParser.class.getName(), "Getting venues error: " + e);
			return false;
		}

		return true;
	}

	/**
	 * Downloads courses.xml, parses it and populates the database.
	 * 
	 * @param path
	 *            URL to courses.xml
	 */
	private Boolean getCourses(String path) {
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
		} catch (IOException e) {
			errorMsg = "Couldn't download courses.xml. No connection or wrong URL.";
			Log.e(XmlParser.class.getName(),
					"Couldn't download courses.xml. No connection or wrong URL. "
							+ e);
			return false;
		} catch (NullPointerException e) {
			errorMsg = "Parsing venues.xml error. Couldn't validate courses.xml.";
			Log.e(XmlParser.class.getName(), "Parsing courses.xml error: " + e);
			return false;
		} catch (Exception e) {
			errorMsg = "Error occured while parsing courses.xml.";
			Log.e(XmlParser.class.getName(), "Getting courses error: " + e);
			return false;
		}
		return true;
	}

	/**
	 * Downloads timetable.xml, parses it and populates the database.
	 * 
	 * @param path
	 *            URL to timetable.xml
	 */
	private Boolean getTimetable(String path) {
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
				Node dayNode = time.getParentNode();
				Node semester = dayNode.getParentNode().getParentNode();

				/* Get day */
				String day = dayNode.getAttributes().getNamedItem("name")
						.getTextContent();
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

				/* Get venue */
				Element venue = (Element) lecture.getElementsByTagName("venue")
						.item(0);

				String room = venue.getElementsByTagName("room").item(0)
						.getTextContent();
				String building = venue.getElementsByTagName("building")
						.item(0).getTextContent();

				/* Get comment */
				String comment = lecture.getElementsByTagName("comment")
						.item(0).getTextContent();

				/* Get the years */
				NodeList yearsNode = lecture.getElementsByTagName("year");

				for (int j = 0; j < yearsNode.getLength(); j++) {
					String value = yearsNode.item(j).getTextContent();
					int year = Integer.parseInt(value);

					availabilityDao.insert(acronym, year);

				}

				/* Insert this entry */
				timetableDao.insert(acronym, semesterNum, day,
						new Time(start).toInt(), new Time(finish).toInt(),
						building, room, comment);
			}

		} catch (IOException e) {
			errorMsg = "Couldn't download timetable.xml. No connection or wrong URL.";
			Log.e(XmlParser.class.getName(),
					"Couldn't download timetable.xml. No connection or wrong URL. "
							+ e);
			return false;
		} catch (NullPointerException e) {
			errorMsg = "Parsing venues.xml error. Couldn't validate timetable.xml.";
			Log.e(XmlParser.class.getName(), "Parsing timetable.xml error: "
					+ e);
			return false;
		} catch (Exception e) {
			errorMsg = "Error occured while parsing timetable.xml.";
			Log.e(XmlParser.class.getName(), "Getting timetable error: " + e);
			return false;
		}
		return true;
	}

}
