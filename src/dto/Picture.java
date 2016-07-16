package dto;

public class Picture {

	private long id = 0;
	private String isMainPic = "N";
	private long entityId = 0;
	private byte[] data;

	public Picture() {}

	public byte[] getData() {
		return data;
	}

	public long getEntityId() {
		return entityId;
	}

	public long getId() {
		return id;
	}

	public String getIsMainPic() {
		return isMainPic;
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

	public void setIsMainPic(String isMainPic) {
		this.isMainPic = isMainPic;
	}

}
