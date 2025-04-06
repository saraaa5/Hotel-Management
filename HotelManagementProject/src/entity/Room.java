package entity;


import java.util.ArrayList;
import java.util.List;

public class Room {
	private int brojSobe;
	private RoomType tip;
	private SobaStatus statusSobe;
	private boolean hasAc;
	private boolean hasBalcony;
	private boolean hasTv;
	private boolean smoking;

	public Room(int brojSobe, RoomType tip, boolean hasAc, boolean hasBalcony, boolean hasTv, boolean smoking) {
		this.brojSobe = brojSobe;
		this.tip = tip;
		this.statusSobe = SobaStatus.SLOBODNA;
		this.hasAc = hasAc;
		this.hasBalcony = hasBalcony;
		this.hasTv = hasTv;
		this.smoking = smoking;
	}
	public int getBrojSobe() {
		return brojSobe;
	}
	public void setBrojSobe(int brojSobe) {
		this.brojSobe = brojSobe;
	}

  public RoomType getTip() {
    return tip;
  }

  public void setTip(RoomType tip) {
    this.tip = tip;
  }

	public String toFileString() {
		return brojSobe + "," + tip;
	}
	public SobaStatus getStatusSobe() {
		return statusSobe;
	}
	public void setStatusSobe(SobaStatus statusSobe) {
		this.statusSobe = statusSobe;
	}
	public boolean isHasAc() {
		return hasAc;
	}

	public void setHasAc(boolean hasAc) {
		this.hasAc = hasAc;
    }
	public boolean isHasBalcony() {
		return hasBalcony;

	}
	public void setHasBalcony(boolean hasBalcony) {
		this.hasBalcony = hasBalcony;

	}

	public boolean isHasTv() {
		return hasTv;
    }

	public void setHasTv(boolean hasTv) {
		this.hasTv = hasTv;
    }

	public boolean isSmoking() {
		return smoking;
    }

	public void setSmoking(boolean smoking) {
		this.smoking = smoking;
	}

  @Override
  public String toString() {
   return brojSobe + "," + tip.getId()  + "," + hasAc + "," + hasBalcony + "," + hasTv + "," + smoking + "," + statusSobe;
  }

	public enum SobaStatus {
		SLOBODNA,
		ZAUZETO,
		SPREMANJE,
	}
}
