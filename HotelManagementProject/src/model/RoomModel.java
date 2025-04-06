package model;

import javax.swing.table.AbstractTableModel;

import entity.Guest;
import entity.Room;
import entity.Worker;
import manage.AdministratorManager;
import manage.ReceptionistManager;
import manage.RoomManager;

import java.time.LocalDate;

public class RoomModel extends AbstractTableModel {
	private static final long serialVersionUID = 173122351138550735L;
	private RoomManager roomManager;
	private String[] columnNames = {  "broj sobe", "tip sobe", "klima", "tv", "balkon", "pusenje", "status" };

	public RoomModel(RoomManager roomManager) {
		this.roomManager = roomManager;
	}

	@Override
	public int getRowCount() {
		return roomManager.getRooms().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		System.out.println("Row index: " + rowIndex + " Column index: " + columnIndex);
    if (roomManager.getRooms().size() == 0) {
      return "";
    }
		Room s = roomManager.getRooms().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return s.getBrojSobe();
		case 1:
			return s.getTip().getTypeName();
		case 2:
        if (s.isHasAc()) {
          return "Da";
        } else {
          return "Ne";
        }

        case 3:
                	if (s.isHasTv()) {
          return "Da";
        } else {
          return "Ne";
                	}
        case 4:
        if (s.isHasBalcony()) {
          return "Da";
        } else {
          return "Ne";
        }
        case 5:
        if (s.isSmoking()) {
          return "Dozvoljeno";
        } else {
          return "Ne";
        }
        case 6:
        	return s.getStatusSobe();
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
