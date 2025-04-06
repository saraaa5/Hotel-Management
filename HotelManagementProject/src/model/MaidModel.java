package model;

import javax.swing.table.AbstractTableModel;

import entity.CleanupAssignment;
import entity.Guest;
import entity.Room;
import entity.Worker;
import manage.AdministratorManager;
import manage.AssignmentManager;
import manage.ReceptionistManager;
import manage.RoomManager;

import java.time.LocalDate;
import java.util.List;

public class MaidModel extends AbstractTableModel {
	private static final long serialVersionUID = 173122351138550735L;

	private AssignmentManager assignmentManger;
	private String[] columnNames = {  "ROOM", "HOUSEKEEPER", "DATE"};
    private List<CleanupAssignment> cleanups;


	public MaidModel(List<CleanupAssignment> cleanups) {

		setAssignments(cleanups);
	}


	@Override
	public int getRowCount() {
		return this.cleanups.size();
	}

    public void setAssignments(List<CleanupAssignment> cleanups) {
    	this.cleanups = cleanups;
        fireTableDataChanged();
    }
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (cleanups.size() == 0) return "";
		CleanupAssignment ca = cleanups.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return String.valueOf(ca.getRoomId());
		case 1:
			return ca.getMaidId();
		case 2:
			return ca.getDate().toString();

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
