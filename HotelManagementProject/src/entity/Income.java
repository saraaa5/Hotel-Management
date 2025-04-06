package entity;

import java.time.LocalDate;

public class Income {
	private double amount;
	private int roomNumber;
	private LocalDate date;
	private String roomTypeId;

  public Income(double amount, int roomNumber, LocalDate date, String roomTypeId) {
    this.amount = amount;
    this.roomNumber = roomNumber;
    this.date = date;
    this.roomTypeId = roomTypeId;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public int getRoomNumber() {
    return roomNumber;
  }

  public void setRoomNumber(int roomNumber) {
    this.roomNumber = roomNumber;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getRoomTypeId() {
    return roomTypeId;
  }

  public void setRoomTypeId(String roomTypeId) {
    this.roomTypeId = roomTypeId;
  }

  @Override
  public String toString() {
    return String.valueOf(amount)
        + ","
        + date.toString()
        + ","
        + String.valueOf(roomNumber)
        + ","
        + roomTypeId;
  }
}
