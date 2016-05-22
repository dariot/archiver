package antics;

public class Document {

	private int id = 0;
	private int objectId = 0;
	private byte[] content;

	public byte[] getContent() {
		return content;
	}

	public int getId() {
		return id;
	}

	public int getObjectId() {
		return objectId;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

}
