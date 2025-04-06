package model;

import javax.swing.table.AbstractTableModel;

import entity.Guest;
import entity.Reservation;
import manage.ReservationManager;

import java.util.List;


public class ResGuestModel extends AbstractTableModel {
	private static final long serialVersionUID = 173122351138550735L;
	private ReservationManager reservationManager;
	private String[] columnNames = {  "START", "END", "STATUS", "ROOM", "ROOM TYPE", "SERVICES", "PRICE"};
	private String username;
	public ResGuestModel(ReservationManager reservationManager, String username) {
		this.reservationManager = reservationManager;
            this.username = username;
		}


	@Override
	public int getRowCount() {
		return this.reservationManager.getReservationsForGuest(this.username).size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
    if (getRowCount() == 0) {
      return "";
    }
		Reservation s = this.reservationManager.getReservationsForGuest(this.username).get(rowIndex);
		switch (columnIndex) {
		case 0:
			return s.getDatumDolaska();
		case 1:
			return s.getDatumOdlaska();
		case 2:
			return s.getStatusRezervacije();
        case 3:
            return s.getSoba().getBrojSobe();
        case 4:
            return s.getSoba().getTip();

        case 5:
            return s.getDodatneUsluge() != null ? s.getDodatneUsluge() : "";

          case 6:
        	  if(s.getStatusRezervacije().equals(Reservation.RezervacijaStatus.ODBIJENA)) {
        		  return "0.0";
        	  }
        	return s.getPrice().toString();
		default:
			return null;
		}

	}

	@Override
	public String getColumnName(int column) {
		return this.columnNames[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return this.getValueAt(0, columnIndex).getClass();
	}



}
