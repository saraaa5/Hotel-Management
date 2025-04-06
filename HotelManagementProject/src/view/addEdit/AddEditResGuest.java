package view.addEdit;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;

import entity.*;
import manage.AdditionalServiceManager;
import manage.AdministratorManager;
import manage.IncomeManager;
import manage.ReservationManager;
import manage.RoomManager;
import manage.RoomTypeManager;
import model.ReservationModel;
import view.ReservationFrame;
import view.WorkersFrame;
import model.ResGuestModel;
public class AddEditResGuest extends JDialog {
	private static final long serialVersionUID = -5247231764310200252L;
	private ReservationManager reservationManager;
    private DefaultListModel<AdditionalService> selectedServicesModel;
    private JList<AdditionalService> selectedServicesList;
    private List<AdditionalService> selectedObjects = new ArrayList<AdditionalService>();
	private JFrame parent;
    private RoomManager rm;
    private RoomTypeManager rtm;
    private AdditionalServiceManager asm;
    private JTable table;
    private IncomeManager incomeManager;
    private Guest gost;

	public AddEditResGuest(JFrame parent, ReservationManager reservationManager, RoomTypeManager rtm, RoomManager roomManager, AdditionalServiceManager additionalServiceManager, JTable table, IncomeManager incomeManager, Guest gost) {
		super(parent, true);
		this.incomeManager = incomeManager;
		this.parent = parent;
		this.rm = roomManager;
		this.rtm = rtm;
		this.asm = additionalServiceManager;
		this.table = table;
		this.gost = gost;

		this.reservationManager = reservationManager;

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

        JLabel lblDatumDolaska = new JLabel("Datum dolaska");
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 1;
        add(lblDatumDolaska, con);

        JTextField txtDatumDolaska = new JTextField(20);
        con.gridx = 1;
        con.gridy = 0;
        con.gridwidth = 2;
        add(txtDatumDolaska, con);

        JLabel lblDatumOdlaska = new JLabel("Datum odlaska");
        con.gridx = 0;
        con.gridy = 1;
        con.gridwidth = 1;
        add(lblDatumOdlaska, con);

        JTextField txtDatumOdlaska = new JTextField(20);
        con.gridx = 1;
        con.gridy = 1;
        con.gridwidth = 2;
        add(txtDatumOdlaska, con);

        JLabel lblStatus = new JLabel("Status rezervacije");
        con.gridx = 0;
        con.gridy = 2;
        con.gridwidth = 1;
        add(lblStatus, con);

        JComboBox<Reservation.RezervacijaStatus> comboBoxStatus = new JComboBox<>(Reservation.RezervacijaStatus.values());
        comboBoxStatus.setSelectedItem(Reservation.RezervacijaStatus.NA_CEKANJU);

    	comboBoxStatus.setEnabled(false);

        con.gridx = 1;
        con.gridy = 2;
        con.gridwidth = 2;
        add(comboBoxStatus, con);




        JLabel lblKorisnickoIme = new JLabel("Korisničko ime");
        con.gridx = 0;
        con.gridy = 3;
        con.gridwidth = 1;
        add(lblKorisnickoIme, con);

        JTextField txtKorisnickoIme = new JTextField(20);
        con.gridx = 1;
        con.gridy = 3;
        con.gridwidth = 2;
        add(txtKorisnickoIme, con);


        List<RoomType> roomTypes = rtm.getRoomTypes();
		JComboBox<RoomType> roomTypeDropdown = new JComboBox<>(roomTypes.toArray(new RoomType[0]));
		con.gridx = 0;
		con.gridy = 4;
		con.gridwidth = 2;
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
		con.gridy = 5;
		con.gridwidth = 2;
		add(featuresPanel, con);


		con.gridx = 0;
		con.gridy = 6;
		con.gridwidth = 2;
		JComboBox<AdditionalService> servicesComboBox;
        JLabel servicesLabel = new JLabel("Additional Services:");
        servicesLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add(servicesLabel, con);

        con.gridx = 0;
        con.gridy = 7;
        con.gridwidth = 2;
        servicesComboBox = new JComboBox<>(asm.getServices().toArray(new AdditionalService[0]));
        servicesComboBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
        servicesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdditionalService selectedService = (AdditionalService) servicesComboBox.getSelectedItem();
                AdditionalService srv = null;
                for (AdditionalService service : asm.getServices()) {
                    if (service.toString().equals(selectedService.toString())) {
                        System.out.println(service);
                        srv = service;
                    }
                }
                if (!selectedServicesModel.contains(selectedService)) {
                    selectedServicesModel.addElement(selectedService);
                    if (srv != null) {
                        selectedObjects.add(srv);
                    }
                }
            }
        });
        add(servicesComboBox, con);

        con.gridx = 0;
        con.gridy = 8;
        con.gridwidth = 2;
        selectedServicesModel = new DefaultListModel<>();
        selectedServicesList = new JList<>(selectedServicesModel);
        selectedServicesList.setFont(new Font("Tahoma", Font.PLAIN, 11));
        add(new JScrollPane(selectedServicesList), con);


        txtKorisnickoIme.setText(this.gost.getKorisnickoIme());



        JButton btnCancel = new JButton("Cancel");
        con.gridx = 1;
        con.gridy = 16;
        con.gridwidth = 1;
        add(btnCancel, con);

        JButton btnOK = new JButton("OK");
        con.gridx = 2;
        con.gridy = 16;
        con.gridwidth = 1;
        add(btnOK, con);

    btnCancel.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
        	  AddEditResGuest.this.dispose();

          }
        });

    btnOK.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {

            LocalDate datumDolaska;
            LocalDate datumOdlaska;
            try {
              datumDolaska = LocalDate.parse(txtDatumDolaska.getText());
              datumOdlaska = LocalDate.parse(txtDatumOdlaska.getText());
            } catch (DateTimeParseException ex) {
              JOptionPane.showMessageDialog(
            		  AddEditResGuest.this,
                  "Datum nije u odgovarajućem formatu (yyyy-MM-dd)!",
                  "Greška",
                  JOptionPane.ERROR_MESSAGE);
              return;
            }
            if (datumDolaska.isAfter(datumOdlaska)) {
              JOptionPane.showMessageDialog(
            		  AddEditResGuest.this,
                  "Datum dolaska mora biti pre datuma odlaska!",
                  "Greška",
                  JOptionPane.ERROR_MESSAGE);
              return;
            }
            Reservation.RezervacijaStatus status =
                (Reservation.RezervacijaStatus) comboBoxStatus.getSelectedItem();
            String gost_username = txtKorisnickoIme.getText();
            RoomType soba = (RoomType) roomTypeDropdown.getSelectedItem();
            List<AdditionalService> selectedServices = selectedObjects;
            boolean ac = acCheckbox.isSelected();
            boolean tv = tvCheckbox.isSelected();
            boolean balcony = balconyCheckbox.isSelected();
            boolean smoking = smokingCheckbox.isSelected();

            Room room =
                reservationManager.checkAvailability(
                    datumDolaska, datumOdlaska, soba, ac, tv, balcony, smoking, null);

            if (room == null) {

              JOptionPane.showMessageDialog(
            		  AddEditResGuest.this,
                  "Nema slobodnih soba za izabrane kriterijume!",
                  "Greška",
                  JOptionPane.ERROR_MESSAGE);
              return;
            } else {
              Double price = 0.0;

                Reservation s =
                    new Reservation(datumDolaska, datumOdlaska, status, gost_username, room);
                s.setDodatneUsluge(selectedServices);
                price = reservationManager.addReservation(s);
                JOptionPane.showMessageDialog(
                		AddEditResGuest.this,
                		"Reservation successfully added! Price: " + price + " RSD",
                		"Success",
                		JOptionPane.INFORMATION_MESSAGE);
                ((ResGuestModel) table.getModel()).fireTableDataChanged();
                return;
              }

          }
        });
	}
}