package dto;

public class Document {

	private long id = 0;
	private long entityId = 0;
	private String name = "";
	private byte[] content;

	public byte[] getContent() {
		return content;
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

	public void setContent(byte[] content) {
		this.content = content;
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
