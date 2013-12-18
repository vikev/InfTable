package eu.vikev.android.inftable.db.entities;

import eu.vikev.android.inftable.custom.MyTime;

public class TimetableEntry {
	private Long id;
	private Course course;
	private String semester;
	private String day;
	private MyTime start;
	private MyTime end;
	private Building building;
	private String buildingName;
	private Room room;
	private String roomName;
	private String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public MyTime getStart() {
		return start;
	}

	public void setStart(MyTime start) {
		this.start = start;
	}

	public MyTime getEnd() {
		return end;
	}

	public void setEnd(MyTime end) {
		this.end = end;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "TimetableEntry [id=" + id + ", course=" + course
				+ ", semester=" + semester + ", day=" + day + ", start="
				+ start.toString() + ", end=" + end.toString() + ", building="
				+ building + ", room="
				+ room + ", comment=" + comment + "]";
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

}
