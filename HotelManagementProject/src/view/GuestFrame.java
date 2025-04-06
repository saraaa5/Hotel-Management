package view;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.Guest;
import entity.Worker;
import manage.AdministratorManager;
import manage.ReceptionistManager;
import model.GuestModel;
import model.WorkerModel;
import view.addEdit.AddEditGuest;
import view.addEdit.AddEditWorker;

public class GuestFrame extends JFrame{
	private static final long serialVersionUID = -8026201049950423764L;
	private ReceptionistManager receptionistManager;

	protected JToolBar mainToolbar = new JToolBar();
	protected JButton btnAdd = new JButton();
	protected JButton btnEdit = new JButton();
	protected JButton btnDelete = new JButton();
	protected JTextField tfSearch = new JTextField(20);
	protected JTable table;
	protected TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
	private JFrame parentFrame;

	public GuestFrame(JFrame parent, ReceptionistManager receptionistManager) {
		this.receptionistManager = receptionistManager;
		this.parentFrame = parent;
		// podesavanje prozora
		setTitle("Gosti");
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
		table = new JTable(new GuestModel(receptionistManager));
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

		initActions();
	}



	private void initActions() {
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddEditGuest add = new AddEditGuest(GuestFrame.this, receptionistManager, null);
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
					String korisnickoIme = table.getValueAt(red, 4).toString();
					Guest s = receptionistManager.findGuestByUsername(korisnickoIme);
					if(s != null) {
						AddEditGuest add = new AddEditGuest(GuestFrame.this, receptionistManager, s);
						add.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(null, "Nije moguće pronaci odabranog!", "Greška", JOptionPane.ERROR_MESSAGE);
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
					String korisnickoIme = table.getValueAt(red, 4).toString();
					Guest s = receptionistManager.findGuestByUsername(korisnickoIme);
					if(s != null) {
						int izbor = JOptionPane.showConfirmDialog(null,"Da li ste sigurni da želite obrisati ovog radnika?",
								s.getIme() + " "+s.getPrezime() +" - Potvrda brisanja", JOptionPane.YES_NO_OPTION);
						if(izbor == JOptionPane.YES_OPTION) {
							receptionistManager.remove(s.getIme(), s.getPrezime());
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
		GuestModel sm = (GuestModel) this.table.getModel();
		sm.fireTableDataChanged();
	}

	// Pamcenje redosleda sortiranja za svaku kolonu posebno - primer
	@SuppressWarnings("serial")
	private Map<Integer, Integer> sortOrder = new HashMap<Integer, Integer>() {{put(0, 1);put(1, 1);put(2, 1);put(3, 1);}};

	// Manuelni sorter - potrebno za razumevanje rada podrazumevanog sortera tabele
	protected void sort(int index) {
		// index of table column

		this.receptionistManager.getGuests().sort(new Comparator<Guest>() {
			int retVal = 0;
			@Override
			public int compare(Guest o1, Guest o2) {
				switch (index) {
				case 0:
					retVal = o1.getIme().compareTo(o2.getIme());
					break;
				case 1:
					retVal = o1.getPrezime().compareTo(o2.getPrezime());
					break;
				case 2:
					retVal = o1.getPol().compareTo(o2.getPol());
					break;
				case 3:
					retVal = o1.getAdresa().compareTo(o2.getAdresa());
					break;
				case 4:
					retVal = o1.getKorisnickoIme().compareTo(o2.getKorisnickoIme());
					break;
				case 5:
					retVal = o1.getLozinka().compareTo(o2.getLozinka());
					break;
				case 6:
					retVal = o1.getDatumRodjenja().compareTo(o2.getDatumRodjenja());
					break;
				case 7:
					retVal = Integer.compare(o1.getTelefon(), o2.getTelefon());
					break;
				default:
					System.out.println("Prosirena tabela");
					System.exit(1);
					break;
				}
				return retVal*sortOrder.get(index);
			}
		});

		System.out.println("kolona "+index+" smer "+sortOrder.get(index));
		sortOrder.put(index, sortOrder.get(index)*-1);
		refreshData();

	}

}
