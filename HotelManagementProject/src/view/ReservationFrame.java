package view;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.AdditionalService;
import entity.Guest;
import entity.Reservation;
import manage.ManagerFactory;
import manage.ReceptionistManager;
import manage.ReservationManager;
import model.ReservationModel;
import model.WorkerModel;
import view.addEdit.AddEditGuest;
import view.addEdit.AddEditReservation;
import view.addEdit.AddEditWorker;

public class ReservationFrame extends JFrame{
	private static final long serialVersionUID = -8026201049950423764L;
	private ReservationManager reservationManager;

	protected JToolBar mainToolbar = new JToolBar();
	protected JButton btnAdd = new JButton();
	protected JButton btnEdit = new JButton();
	protected JButton btnDelete = new JButton();

	protected JButton btnCheckIn = new JButton("CHECK IN");
	protected JButton btnCheckOut = new JButton("CHECK OUT");

	protected JTextField tfSearch = new JTextField(20);
	protected JTable table;
	protected TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
	private JFrame parentFrame;
	private ManagerFactory managers;


	public ReservationFrame(JFrame parent, ManagerFactory managers, boolean check) {
		this.reservationManager = managers.getReservationManager();
		this.parentFrame = parent;
		this.managers = managers;
		// podesavanje prozora
		setTitle("Rezervacije");
		setSize(800, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon("img/icon.png").getImage());

		// podesavanje toolbar-a
		ImageIcon addIcon = new ImageIcon("./img/vector-add-icon.jpg");
		ImageIcon scaled = new ImageIcon(addIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		addIcon = scaled;
		btnAdd.setIcon(addIcon);
		mainToolbar.add(btnAdd);
		ImageIcon editIcon = new ImageIcon("./img/edit.png");
		ImageIcon scaledEditIcon = new ImageIcon(editIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		editIcon = scaledEditIcon;
		btnEdit.setIcon(editIcon);
		mainToolbar.add(btnEdit);
		ImageIcon deleteIcon = new ImageIcon("./img/remove.png");
		ImageIcon scaledRemoveIcon = new ImageIcon(deleteIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		deleteIcon = scaledRemoveIcon;
		btnDelete.setIcon(deleteIcon);
		mainToolbar.add(btnDelete);
		mainToolbar.setFloatable(false);
		add(mainToolbar, BorderLayout.NORTH);

		// podesavanje tabele
		table = new JTable(new ReservationModel(reservationManager));
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		// podesavanje manuelnog sortera tabele, potrebno i za pretragu
		tableSorter.setModel((AbstractTableModel) table.getModel());
		table.setRowSorter(tableSorter);
		JScrollPane sc = new JScrollPane(table);
		add(sc, BorderLayout.CENTER);

		table.getTableHeader().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// preuzimanje indeksa kolone potrebnog za sortiranje
				int index = table.getTableHeader().columnAtPoint(arg0.getPoint());

				// call abstract sort method
				sort(index);
			}

		});
		// podesavanje pretrage


		JPanel pSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));


		pSearch.setBackground(Color.cyan);
		pSearch.add(new JLabel("Search:"));
		pSearch.add(tfSearch);
    if (check) {
      pSearch.add(btnCheckIn);
      pSearch.add(btnCheckOut);
    }




		add(pSearch, BorderLayout.SOUTH);

		tfSearch.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				//System.out.println("~ "+tfSearch.getText());
				if (tfSearch.getText().trim().length() == 0) {
				     tableSorter.setRowFilter(null);
				  } else {
					  tableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + tfSearch.getText().trim()));
				  }
			}
		});

		table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column == 2) { // Pretpostavimo da je indeks kolone sa statusom 2
                    String status = (String) table.getValueAt(row, column);
                    if (status.equals("POTVRDJENA") || status.equals("ODBIJENA")) {
                        table.setRowSelectionInterval(row, row);
                        table.setSelectionBackground(Color.WHITE); // Vratiti na osnovnu boju
                    }
                }
            }
        });

		initActions(this.managers);
	}



	private void initActions(ManagerFactory managers) {
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddEditReservation add = new AddEditReservation(ReservationFrame.this, reservationManager, null, managers.getRoomTypeManager(), managers.getRoomManager(), managers.getAdditionalServiceManager(), table, managers.getIncomeManager());
				add.setVisible(true);
			}
		});

		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = table.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška", JOptionPane.WARNING_MESSAGE);
				}else {
					List<Reservation> reservations = reservationManager.getReservations();
					Reservation s = reservations.get(red);

					if(s != null) {
						AddEditReservation add = new AddEditReservation(ReservationFrame.this, reservationManager, s, managers.getRoomTypeManager(), managers.getRoomManager(), managers.getAdditionalServiceManager(), table, managers.getIncomeManager());
						add.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(null, "Nije moguće pronaci odabranog!", "Greška", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

    btnCheckIn.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            int red = table.getSelectedRow();
            if (red == -1) {
              JOptionPane.showMessageDialog(
                  null, "Morate odabrati red u tabeli.", "Greška", JOptionPane.WARNING_MESSAGE);
            } else {
              List<Reservation> reservations = reservationManager.getReservations();
              Reservation s = reservations.get(red);

              if (s != null) {
                if (s.getStatusRezervacije().equals(Reservation.RezervacijaStatus.POTVRDJENA)) {
                  // edit the room status

                  if (s.getDatumDolaska().isEqual(LocalDate.now())) {

                    managers.getRoomManager().changeOccupyRoom(s.getSoba());

                    JOptionPane.showMessageDialog(
                        null,
                        "CHECK IN uspešno izvršen!",
                        "Informacija",
                        JOptionPane.INFORMATION_MESSAGE);

                  } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "Nije moguće izvršiti CHECK IN na ovu rezervaciju! Datum dolaska nije jednak današnjem datumu!",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE);
                  }
                } else {
                  JOptionPane.showMessageDialog(
                      null,
                      "Nije moguće izvršiti CHECK IN na ovu rezervaciju! Status rezervacije nije POTVRDJENA!",
                      "Greška",
                      JOptionPane.ERROR_MESSAGE);
                }

              } else {
                JOptionPane.showMessageDialog(
                    null,
                    "Nije moguće izvršiti CHECK IN na ovu rezervaciju!",
                    "Greška",
                    JOptionPane.ERROR_MESSAGE);
              }
            }
          }
        });

    btnCheckOut.addActionListener( new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		int red = table.getSelectedRow();
    		if(red == -1) {
    			JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška", JOptionPane.WARNING_MESSAGE);
    		}else {
    			List<Reservation> reservations = reservationManager.getReservations();
    			Reservation s = reservations.get(red);

    			if(s != null) {
    				if(s.getSoba().getStatusSobe().equals(entity.Room.SobaStatus.ZAUZETO)) {
    					managers.getRoomManager().setForCleanup(s.getSoba());
    					managers.getReservationManager().setFinished(s);
    					managers.getAssignmentManager().assignHousekeeper(s.getSoba(), s);
    					JOptionPane.showMessageDialog(null, "CHECK OUT uspešno izvršen!", "Informacija", JOptionPane.INFORMATION_MESSAGE);
    					List<Guest> guests = managers.getReceptionistManager().getGuests();
    					boolean flag = false;
    					for (Guest guest : guests) {
    						System.out.println(guest.getKorisnickoIme());
    						if (guest.getKorisnickoIme().equals(s.getGost_username())) {
    							flag = true;
    						}
    					}
    					if(!flag) {
    						AddEditGuest add = new AddEditGuest(ReservationFrame.this, managers.getReceptionistManager(), null);
    						add.setVisible(true);
    					}
    					return;
    				}else {
    					JOptionPane.showMessageDialog(null, "Nije moguće izvršiti CHECK OUT na ovu rezervaciju!", "Greška", JOptionPane.ERROR_MESSAGE);
    				}
    			}else {
    				JOptionPane.showMessageDialog(null, "Nije moguće izvršiti CHECK OUT na ovu rezervaciju!", "Greška", JOptionPane.ERROR_MESSAGE);
    			}

    		}
    	}
    });


		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = table.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška", JOptionPane.WARNING_MESSAGE);
				}else {
					LocalDate datumDolaska = LocalDate.parse(table.getValueAt(red, 0).toString());
					LocalDate datumOdlaska = LocalDate.parse(table.getValueAt(red, 1).toString());
					String korisnickoIme = table.getValueAt(red, 7).toString();
					Reservation s = reservationManager.FindReservationByDateAndGuest(datumDolaska, datumOdlaska, korisnickoIme);
					if(s != null) {
						int izbor = JOptionPane.showConfirmDialog(null,"Da li ste sigurni da želite obrisati ovu rezervaciju?",
								s.getDatumDolaska() + " "+s.getDatumOdlaska() + " - gost " + korisnickoIme + " - Potvrda brisanja", JOptionPane.YES_NO_OPTION);
						if(izbor == JOptionPane.YES_OPTION) {
							reservationManager.remove(datumDolaska, datumOdlaska, korisnickoIme);
							refreshData();
						}
					}else {
						JOptionPane.showMessageDialog(null, "Nemoguće pronaći odabranog radnika!", "Greška", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}

	// potrebno osvezavanje podataka u tabeli bez gasenja prozora
	public void refreshData() {
		ReservationModel sm = (ReservationModel) this.table.getModel();
		sm.fireTableDataChanged();
	}

	// Pamcenje redosleda sortiranja za svaku kolonu posebno - primer
	@SuppressWarnings("serial")
	private Map<Integer, Integer> sortOrder = new HashMap<Integer, Integer>() {{put(0, 1);put(1, 1);put(2, 1);put(3, 1);}};

	// Manuelni sorter - potrebno za razumevanje rada podrazumevanog sortera tabele
	protected void sort(int index) {
		// index of table column



		System.out.println("kolona "+index+" smer "+sortOrder.get(index));
		sortOrder.put(index, sortOrder.get(index)*-1);
		refreshData();

	}

	private int compareLists(List<AdditionalService> dodatneUsluge, List<AdditionalService> dodatneUsluge1) {
		int size1 = dodatneUsluge.size();
		int size2 = dodatneUsluge1.size();
		int minSize = Math.min(size1, size2);

		for (int i = 0; i < minSize; i++) {
			int comparison = dodatneUsluge.get(i).compareTo(dodatneUsluge1.get(i));
			if (comparison != 0) {
				return comparison;
			}
		}

		// Ako su svi elementi jednaki, uporedi veličine lista
		return Integer.compare(size1, size2);
	}


}
