package model;

import javax.swing.table.AbstractTableModel;

import entity.Reservation;
import manage.ReservationManager;


public class ReservationModel extends AbstractTableModel {
	private static final long serialVersionUID = 173122351138550735L;
	private ReservationManager reservationManager;
	private String[] columnNames = {  "START", "END", "STATUS", "USERNAME", "ROOM", "ROOM TYPE","SERVICES", "PRICE"};

	public ReservationModel(ReservationManager mng) {
		this.reservationManager = mng;
	}

	@Override
	public int getRowCount() {
		return reservationManager.getReservations().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

    if (reservationManager.getReservations().size() == 0) {
      return "";
    }

		Reservation s = reservationManager.getReservations().get(rowIndex);


		switch (columnIndex) {
		case 0:
			return s.getDatumDolaska();
		case 1:
			return s.getDatumOdlaska();
		case 2:
			return s.getStatusRezervacije();
        case 3:
            return s.getGost_username();
        case 4:
        	return s.getSoba().getBrojSobe();
        case 5:
            return s.getSoba().getTip().getTypeName();
        case 6:
            return s.getDodatneUsluge() != null ? s.getDodatneUsluge() : "";
        case 7:
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
