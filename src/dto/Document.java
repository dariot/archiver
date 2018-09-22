package dto;

public class Document {

	private long id = -1;
	private long entityId = -1;
	private String name = "";
	private byte[] data;

	public byte[] getData() {
		return data;
	}

	public long getEntityId() {
		return entityId;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
