package eu.vikev.android.inftable.db.entities;

public class Building {
	private long id;
	private String name;
	private String description;
	private String map;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	@Override
	public String toString() {
		return "Building [id=" + id + ", name=" + name + ", description="
				+ description + ", map=" + map + "]";
	}

}
