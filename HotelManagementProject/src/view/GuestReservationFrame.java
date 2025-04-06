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
import model.ResGuestModel;
import model.WorkerModel;
import view.addEdit.AddEditResGuest;
import view.addEdit.AddEditWorker;

public class GuestReservationFrame extends JFrame{
	private static final long serialVersionUID = -8026201049950423764L;
	private ReservationManager reservationManager;
	private ReceptionistManager receptionistManager;
	private Guest gost;

	protected JToolBar mainToolbar = new JToolBar();
	protected JButton btnAdd = new JButton();
	protected JButton btnEdit = new JButton();
	protected JButton btnDelete = new JButton();
	protected JTextField tfSearch = new JTextField(20);
	protected JTable table;
	protected TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
	private JFrame parentFrame;
	private ManagerFactory managers;


	public GuestReservationFrame(JFrame parent, ManagerFactory managers, Guest gost) {
		this.reservationManager = managers.getReservationManager();
		this.parentFrame = parent;
		this.gost = gost;
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

		ImageIcon deleteIcon = new ImageIcon("./img/remove.png");
		ImageIcon scaledRemoveIcon = new ImageIcon(deleteIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		deleteIcon = scaledRemoveIcon;
		btnDelete.setIcon(deleteIcon);
		mainToolbar.add(btnDelete);
		mainToolbar.setFloatable(false);
		add(mainToolbar, BorderLayout.NORTH);

		// podesavanje tabele
		table = new JTable(new ResGuestModel(reservationManager, gost.getKorisnickoIme()));
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

		initActions();
	}



	private void initActions() {
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddEditResGuest add = new AddEditResGuest(GuestReservationFrame.this, reservationManager, managers.getRoomTypeManager(), managers.getRoomManager(), managers.getAdditionalServiceManager(), table, managers.getIncomeManager(), gost);
				add.setVisible(true);
			}
		});

    btnDelete.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {

            int red = table.getSelectedRow();
            if (red == -1) {
              JOptionPane.showMessageDialog(
                  null, "Morate odabrati red u tabeli.", "Greška", JOptionPane.WARNING_MESSAGE);
            } else {

              Reservation r = reservationManager.getReservationsForGuest(gost.getKorisnickoIme()).get(red);
              if (r.getStatusRezervacije() == Reservation.RezervacijaStatus.POTVRDJENA
                  || r.getStatusRezervacije() == Reservation.RezervacijaStatus.NA_CEKANJU) {
                r.setStatusRezervacije(Reservation.RezervacijaStatus.OTKAZANA);
                reservationManager.saveData();
                ((ResGuestModel) table.getModel()).fireTableDataChanged();
              } else {
                JOptionPane.showMessageDialog(
                    null,
                    "Nije moguće otkazati rezervaciju koja je već otkazana, odbijena ili zavrsena!",
                    "Greška",
                    JOptionPane.ERROR_MESSAGE);
              }
            }
          }
        });
	}

	// potrebno osvezavanje podataka u tabeli bez gasenja prozora
	public void refreshData() {
		ResGuestModel sm = (ResGuestModel) this.table.getModel();
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
