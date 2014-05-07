package eu.vikev.android.inftable.db.entities;

public class Availability {
	private long id;
	private Course course;
	private int year;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "Availability [id=" + id + ", course=" + course.getAcronym()
				+ ", year=" + year + "]";
	}


}
