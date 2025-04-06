package entity;

import java.util.UUID;

public class RoomType {

	private String bedsNotation;
	private String typeName;
	private String id;


	public RoomType(String numberNotation, String name) {
		generateUUID();
		this.bedsNotation = numberNotation;
		typeName = name;

	}


    private void generateUUID() {
        this.id =  UUID.randomUUID().toString();
    }

	public void setUUID(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

  public void setNumberOfBeds(String num) {
    this.bedsNotation = num;
  }

  public String getNumberOfBeds() {
    return this.bedsNotation;
  }

  public String getTypeName() {
    return this.typeName;
  }

  public void setTypeName(String name) {
    this.typeName = name;
  }

  public String toString() {
    return "name: " + this.typeName + " beds: " + this.bedsNotation;
  }

}
