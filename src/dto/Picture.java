package dto;

public class Picture {
	
	private int id = 0;
	private int objectId = 0;
	private byte[] img;
	private boolean isMainPic = false;
	
	public int getId() {
		return id;
	}

	public byte[] getImg() {
		return img;
	}
	public int getObjectId() {
		return objectId;
	}
	public boolean isMainPic() {
		return isMainPic;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setImg(byte[] img) {
		this.img = img;
	}
	public void setMainPic(boolean isMainPic) {
		this.isMainPic = isMainPic;
	}
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

}
