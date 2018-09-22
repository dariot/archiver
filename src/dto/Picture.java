package dto;

public class Picture {

	private long id = -1;
	private String isMainPic = "N";
	private long entityId = -1;
	private byte[] data;
	private String name = "";

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

	public void setIsMainPic(String isMainPic) {
		this.isMainPic = isMainPic;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
