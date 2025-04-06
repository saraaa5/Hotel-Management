package manage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.RoomType;

public class RoomTypeManager {

	private String roomTypeFile;
    private List<RoomType> roomTypes = new ArrayList<RoomType>();

    public RoomTypeManager(String roomTypeFile) {
        this.roomTypeFile = roomTypeFile;
    }


     public List<RoomType> getRoomTypes() {


    	 return roomTypes;
     }


     public boolean loadData() {

    	 BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(this.roomTypeFile));
      String line = null;
      while ((line = br.readLine()) != null) {
        String[] tokens = line.split(",");
        String id = tokens[0];
        String num = tokens[1];
        String name = tokens[2];

        RoomType roomType = new RoomType(num , name);
        roomType.setUUID(id);
        this.roomTypes.add(roomType);
      }
      br.close();
    } catch (IOException e) {
      System.out.println("Greška prilikom čitanja podataka!");
      return false;
    }
    return true;
  }




    public boolean saveData() {
        BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(this.roomTypeFile));
      for (RoomType rt : roomTypes) {
        bw.write(rt.getId() + "," + rt.getNumberOfBeds() + "," + rt.getTypeName());
        bw.newLine();
      }
      bw.close();
    } catch (IOException e) {
      System.out.println("Greška prilikom čuvanja podataka!");
      return false;
    }
    return true;
    }

    public RoomType findRoomTypeById(String id) {
    	for (RoomType roomType : roomTypes) {
    		if (roomType.getId().equals(id)) {
    			return roomType;
    		}
    	}
    	return null;
    }

    public String findRoomTypeNameById(String id) {
    	RoomType roomType = findRoomTypeById(id);
    	if (roomType != null) {
    		return roomType.getTypeName();
    	} else {
    		return null;
    	}
    }

    public String findRoomTypeNumberOfBedsById(String id) {
    	RoomType roomType = findRoomTypeById(id);
    	if (roomType != null) {
    		return roomType.getNumberOfBeds();
    	} else {
    		return null;
    	}
    }

    public void addRoomType(RoomType roomType) {
        roomTypes.add(roomType);
        saveData();
    }

    public void removeRoomType(RoomType roomType) {
        roomTypes.remove(roomType);
        saveData();
    }

    public void editRoomType(RoomType roomType, String num, String name) {
        roomType.setNumberOfBeds(num);
        roomType.setTypeName(name);
        saveData();
    }


    }
