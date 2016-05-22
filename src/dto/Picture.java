package dto;

public class Picture {

	private int id = 0;
	private int entityId = 0;
	private byte[] data;

	public byte[] getData() {
		return data;
	}

	public int getEntityId() {
		return entityId;
	}

	public int getId() {
		return id;
	}

	public int getObjectId() {
		return entityId;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public void setId(int id) {
		this.id = id;
	}

}
