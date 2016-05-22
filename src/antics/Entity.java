package antics;

public class Entity {

	private long id = 0;
	private long categoryId = 0;
	private String author = "";
	private String title = "";
	private String technique = "";
	private String measures = "";
	private String buyYear = "";
	private double price = 0.0;
	private String paymentType = "";
	private String originalPlace = "";
	private String actualPlace = "";
	private double currentValue = 0.0;
	private String notes = "";
	
	public Entity() {}

	public Entity(String s) {
		if (s != null) {
			String[] splitted = s.split("\\|");
			this.id = Long.parseLong(splitted[0]);
			this.categoryId = Long.parseLong(splitted[1]);
			this.author = splitted[2];
			this.title = splitted[3];
			this.technique = splitted[4];
			this.measures = splitted[5];
			this.buyYear = splitted[6];
			this.price = Double.parseDouble(splitted[7]);
			this.paymentType = splitted[8];
			this.originalPlace = splitted[9];
			this.actualPlace = splitted[10];
			this.currentValue = Double.parseDouble(splitted[11]);
			this.notes = splitted[12];
		}
	}

	public String getActualPlace() {
		return actualPlace;
	}

	public String getAuthor() {
		return author;
	}

	public String getBuyYear() {
		return buyYear;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public double getCurrentValue() {
		return currentValue;
	}

	public long getId() {
		return id;
	}

	public String getMeasures() {
		return measures;
	}

	public String getNotes() {
		return notes;
	}

	public String getOriginalPlace() {
		return originalPlace;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public double getPrice() {
		return price;
	}

	public String getTechnique() {
		return technique;
	}

	public String getTitle() {
		return title;
	}

	public void setActualPlace(String actualPlace) {
		this.actualPlace = actualPlace;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setBuyYear(String buyYear) {
		this.buyYear = buyYear;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMeasures(String measures) {
		this.measures = measures;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setOriginalPlace(String originalPlace) {
		this.originalPlace = originalPlace;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setTechnique(String technique) {
		this.technique = technique;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		String res = "";
		res += this.id + "|";
		res += this.categoryId + "|";
		res += this.author + "|";
		res += this.title + "|";
		res += this.technique + "|";
		res += this.measures + "|";
		res += this.buyYear + "|";
		res += this.price + "|";
		res += this.paymentType + "|";
		res += this.originalPlace + "|";
		res += this.actualPlace + "|";
		res += this.currentValue + "|";
		res += this.notes + "|";

		return res;
	}

}
