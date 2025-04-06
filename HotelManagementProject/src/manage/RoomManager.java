package manage;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.Guest;
import entity.Housekeeper;
import entity.Reservation;
import entity.Room;
import entity.Room.SobaStatus;
import entity.RoomType;

public class RoomManager {
	private List<Room>sobe = new ArrayList<>();
	private String sobeFile;
	ArrayList<Reservation> rezervacije;	//promeniti u Reservation
	private RoomTypeManager rtm;

	public RoomManager(String sobeFile, RoomTypeManager roomTypeManager) {
		this.sobe = new ArrayList<Room>();
		this.sobeFile = sobeFile;
		this.rezervacije = new ArrayList<>();
		this.rtm = roomTypeManager;
	}

  public Room findRoom(int brojSobe) {
    for (Room s : sobe) {
      if (s.getBrojSobe() == brojSobe) {
        return s;
      }
    }
    return null;
  }

	public List<Room> getRooms() {
		return sobe;
	}


	public void addRoom(Room soba) {
		this.sobe.add(soba);
		this.saveData();
	}


	public void changeOccupyRoom(Room room) {
		for (Room s : sobe) {
            if (s.getBrojSobe() == room.getBrojSobe()) {
                s.setStatusSobe(Room.SobaStatus.ZAUZETO);
                break;
            }
        }
		saveData();
	}


	public void setForCleanup(Room room) {
		for (Room s : sobe) {
            if (s.getBrojSobe() == room.getBrojSobe()) {
                s.setStatusSobe(Room.SobaStatus.SPREMANJE);
                break;
            }
        }

        saveData();


	}





	public boolean saveData() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(this.sobeFile, false));
			for (Room g : sobe) {
				pw.println(g.toString());
			}
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public boolean loadData() {
		try (BufferedReader br = new BufferedReader(new FileReader(this.sobeFile))) {
            String linija;
            List<RoomType> roomTypes = rtm.getRoomTypes();
            while ((linija = br.readLine()) != null) {
                String[] tokeni = linija.split(",");
                int brojSobe = Integer.parseInt(tokeni[0]);
                String tip = tokeni[1];
                System.out.println("tip sobe: " + tip + " "+ roomTypes.size());
                boolean hasAc = Boolean.parseBoolean(tokeni[2]);
                boolean hasBalcony = Boolean.parseBoolean(tokeni[3]);
                boolean hasTv = Boolean.parseBoolean(tokeni[4]);
                boolean smoking = Boolean.parseBoolean(tokeni[5]);
                SobaStatus statusSobe = parseStatus(tokeni[6]);
                RoomType tipSobe = null;
		        for (RoomType rt : roomTypes) {
		        	System.out.println("rt: " + rt.toString());
		          if (rt.getId().equals(tip)) {
		        	  System.out.println("nasao tip sobe");
		            tipSobe = rt;
		            break;
		          }
		        }
                Room soba = new Room(brojSobe, tipSobe, hasAc, hasBalcony, hasTv, smoking);
                soba.setStatusSobe(statusSobe);
                this.sobe.add(soba);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}

	private Room.SobaStatus parseStatus(String status) {
    if (status.equals(Room.SobaStatus.SLOBODNA.toString())) {
        return Room.SobaStatus.SLOBODNA;
    } else if (status.equals(Room.SobaStatus.ZAUZETO.toString())) {
        return Room.SobaStatus.ZAUZETO;
    } else if (status.equals(Room.SobaStatus.SPREMANJE.toString())) {
        return Room.SobaStatus.SPREMANJE;
    } else {
        return Room.SobaStatus.SLOBODNA;
    }
}

}
