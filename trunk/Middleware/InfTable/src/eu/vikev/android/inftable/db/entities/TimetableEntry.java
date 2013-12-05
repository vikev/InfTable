package eu.vikev.android.inftable.db.entities;

import eu.vikev.android.inftable.custom.Time;

public class TimetableEntry {
	private Long id;
	private Course course;
	private String semester;
	private String day;
	private Time start;
	private Time end;
	private Building building;
	private Room room;
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

	public Time getStart() {
		return start;
	}

	public void setStart(Time start) {
		this.start = start;
	}

	public Time getEnd() {
		return end;
	}

	public void setEnd(Time end) {
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

}
