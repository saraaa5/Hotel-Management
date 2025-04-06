package entity;

import java.time.LocalDate;
import java.util.UUID;

public class CleanupAssignment {

	private String id;
	private String  maidId;
	private int roomId;
	private String reservationId;
	private LocalDate date;
	private Boolean isCompleted;

  public CleanupAssignment(String maidId, int roomId, String reservationId, LocalDate date) {
    this.maidId = maidId;
    this.roomId = roomId;
    this.reservationId = reservationId;
    this.date = date;
    this.isCompleted = false;
    generateUUID();
  }

  private void generateUUID() {
      this.id =  UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMaidId() {
    return maidId;
  }

  public void setMaidId(String maidId) {
    this.maidId = maidId;
  }

  public int getRoomId() {
    return roomId;
  }

  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }

  public String getReservationId() {
    return reservationId;
  }

  public void setReservationId(String reservationId) {
    this.reservationId = reservationId;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Boolean getIsCompleted() {
    return isCompleted;
  }

  public void setIsCompleted(Boolean isCompleted) {
    this.isCompleted = isCompleted;
  }

  @Override
  public String toString() {
    return id
        + ","
        + maidId
        + ","
        + String.valueOf(roomId)
        + ","
        + reservationId
        + ","
        + date.toString()
        + ","
        + isCompleted.toString();
  }
}
