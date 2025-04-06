package model;

import javax.swing.table.AbstractTableModel;

import entity.Guest;
import entity.Worker;
import manage.AdministratorManager;
import manage.ReceptionistManager;

import java.time.LocalDate;

public class GuestModel extends AbstractTableModel {
	private static final long serialVersionUID = 173122351138550735L;
	private ReceptionistManager receptionistManager;
	private String[] columnNames = {  "ime", "prezime", "pol", "adresa", "korisničko ime",
            "lozinka", "datum rođenja", "telefon"};

	public GuestModel(ReceptionistManager mng) {
		this.receptionistManager = mng;
	}

	@Override
	public int getRowCount() {
		return receptionistManager.getGuests().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Guest s = receptionistManager.getGuests().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return s.getIme();
		case 1:
			return s.getPrezime();
		case 2:
			return s.getPol();
        case 3:
            return s.getAdresa();
        case 4:
            return s.getKorisnickoIme();
        case 5:
            return s.getLozinka();
        case 6:
            return s.getDatumRodjenja();
        case 7:
            return s.getTelefon();
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
