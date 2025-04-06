package view.addEdit;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

import entity.Room;
import entity.RoomType;
import manage.RoomManager;
import manage.RoomTypeManager;
import model.RoomModel;

public class AddEditRoom extends JDialog {
	private static final long serialVersionUID = -5247231764310200252L;
	private RoomManager roomManager;
	private Room editR;
	private JFrame parent;
	private RoomTypeManager rtm;
	private JTable table;

	// Jedan isti dijalog za Add i Edit
	public AddEditRoom(JFrame parent, RoomManager roomManager, RoomTypeManager roomTypeManager, Room editStudent, JTable table) {
		super(parent, true);

		this.table = table;
		this.parent = parent;
		this.rtm = roomTypeManager;

		if (editStudent != null) {
			setTitle("Izmena soba");
		} else {
			setTitle("Dodavanje soba");
		}

		this.roomManager = roomManager;
		this.editR = editStudent;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		initGUI();
		pack();
	}

	private void initGUI() {
		// 2. način sa GridBagLayout
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);

		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.HORIZONTAL;
		con.insets = new Insets(10, 10, 10, 10);

		List<RoomType> roomTypes = rtm.getRoomTypes();
		JComboBox<RoomType> roomTypeDropdown = new JComboBox<>(roomTypes.toArray(new RoomType[0]));
		con.gridx = 0;
		con.gridy = 1;
		con.gridwidth = 1;
		add(roomTypeDropdown, con);

		// Panel with checkboxes
		JPanel featuresPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		JCheckBox acCheckbox = new JCheckBox("AC");
		JCheckBox tvCheckbox = new JCheckBox("TV");
		JCheckBox balconyCheckbox = new JCheckBox("Balcony");
		JCheckBox smokingCheckbox = new JCheckBox("Smoking");
		featuresPanel.add(acCheckbox);
		featuresPanel.add(tvCheckbox);
		featuresPanel.add(balconyCheckbox);
		featuresPanel.add(smokingCheckbox);
		con.gridx = 0;
		con.gridy = 2;
		con.gridwidth = 2;
		add(featuresPanel, con);

		JButton btnCancel = new JButton("Cancel");
		con.gridx = 0;
		con.gridy = 3;
		con.gridwidth = 1;
		add(btnCancel, con);

		JButton btnOK = new JButton("SAVE");
		con.gridx = 1;
		con.gridy = 3;
		con.gridwidth = 1;
		add(btnOK, con);
    if (editR != null) {

      RoomType selectedRoomType = editR.getTip();
      boolean hasAc = editR.isHasAc();
      boolean hasTv = editR.isHasTv();
      boolean hasBalcony = editR.isHasBalcony();
      boolean smoking = editR.isSmoking();
      roomTypeDropdown.setSelectedItem(selectedRoomType);
      con.gridx = 0;
      con.gridy = 1;
      con.gridwidth = 1;
      add(roomTypeDropdown, con);

      // Panel with checkboxes
       featuresPanel = new JPanel(new GridLayout(2, 2, 10, 10));
      acCheckbox.setSelected(hasAc);
      tvCheckbox.setSelected(hasTv);
      balconyCheckbox.setSelected(hasBalcony);
      smokingCheckbox.setSelected(smoking);
      featuresPanel.add(acCheckbox);
      featuresPanel.add(tvCheckbox);
      featuresPanel.add(balconyCheckbox);
      featuresPanel.add(smokingCheckbox);
      con.gridx = 0;
      con.gridy = 2;
      con.gridwidth = 2;
      add(featuresPanel, con);
    } else {
      roomTypeDropdown.setSelectedIndex(0);
      acCheckbox.setSelected(false);
      tvCheckbox.setSelected(false);
      balconyCheckbox.setSelected(false);
      smokingCheckbox.setSelected(false);
    }

    btnOK.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            RoomType selectedRoomType = (RoomType) roomTypeDropdown.getSelectedItem();
            boolean hasAc = acCheckbox.isSelected();
            boolean hasTv = tvCheckbox.isSelected();
            boolean hasBalcony = balconyCheckbox.isSelected();
            boolean smoking = smokingCheckbox.isSelected();
            if (editR != null) {
              editR.setTip(selectedRoomType);
              editR.setHasAc(hasAc);
              editR.setHasTv(hasTv);
              editR.setHasBalcony(hasBalcony);
              editR.setSmoking(smoking);
              roomManager.saveData();
              ((RoomModel) table.getModel()).fireTableDataChanged();

            } else {
              Room newRoom = new Room(
                      roomManager.getRooms().size() + 1,
                      selectedRoomType,
                      hasAc,
                      hasBalcony,
                      hasTv,
                      smoking);
              roomManager.addRoom(newRoom);
              ((RoomModel) table.getModel()).fireTableDataChanged();
            }
            AddEditRoom.this.dispose();
          }
        });

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddEditRoom.this.dispose();
			}
		});
	}
}
