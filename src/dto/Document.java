package dto;


public class Document {

	private int id = 0;
	private String objectId = "";
	private byte[] content;

	public byte[] getContent() {
		return content;
	}

	public int getId() {
		return id;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

}
