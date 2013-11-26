package eu.vikev.android.inftable.entities;

public class Course {
	private long id;
	private String euclid;
	private String acronym;
	private String name;
	private String url;
	private String drps;
	private int ai;
	private int cg;
	private int cs;
	private int se;
	private int level;
	private int points;
	private int year;
	private String lecturer;
	private String deliveryPeriod;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEuclid() {
		return euclid;
	}

	public void setEuclid(String euclid) {
		this.euclid = euclid;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDrps() {
		return drps;
	}

	public void setDrps(String drps) {
		this.drps = drps;
	}

	public int getAi() {
		return ai;
	}

	public void setAi(int ai) {
		this.ai = ai;
	}

	public int getCg() {
		return cg;
	}

	public void setCg(int cg) {
		this.cg = cg;
	}

	public int getCs() {
		return cs;
	}

	public void setCs(int cs) {
		this.cs = cs;
	}

	public int getSe() {
		return se;
	}

	public void setSe(int se) {
		this.se = se;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getLecturer() {
		return lecturer;
	}

	public void setLecturer(String lecturer) {
		this.lecturer = lecturer;
	}

	public String getDeliveryPeriod() {
		return deliveryPeriod;
	}

	public void setDeliveryPeriod(String deliveryPeriod) {
		this.deliveryPeriod = deliveryPeriod;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", euclid=" + euclid + ", acronym="
				+ acronym + ", name=" + name + ", url=" + url + ", drps="
				+ drps + ", ai=" + ai + ", cg=" + cg + ", cs=" + cs + ", se="
				+ se + ", level=" + level + ", points=" + points + ", year="
				+ year + ", lecturer=" + lecturer + ", deliveryPeriod="
				+ deliveryPeriod + "]";
	}
}
