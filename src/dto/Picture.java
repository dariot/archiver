package dto;

public class Picture {

	private long id = 0;
	private String entityId = "";
	private byte[] data;
	
	public Picture() {}

	public byte[] getData() {
		return data;
	}

	public String getEntityId() {
		return entityId;
	}

	public long getId() {
		return id;
	}

	public String getObjectId() {
		return entityId;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public void setId(int id) {
		this.id = id;
	}

}
