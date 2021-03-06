package dto;

public class Category {
	private long id = -1;
	private String name = "";
	
	public Category() {}

	public Category(String s) {
		if (s != null) {
			String[] splitted = s.split("\\|");
			this.id = Long.parseLong(splitted[0]);
			this.name = splitted[1].trim();
		}
	}
	
	public Category(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Category(String id, String name) {
		this.id = Long.parseLong(id);
		this.name = name;
	}

	public boolean equals(Category c) {
		return this.id == c.getId() && this.name == c.getName();
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		String res = "";
		res += this.id + "|" + this.name;
		return res;
	}
}
