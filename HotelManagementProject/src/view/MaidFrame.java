package view;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import entity.CleanupAssignment;
import entity.Guest;
import entity.Room;
import entity.Worker;
import manage.AdministratorManager;
import manage.AssignmentManager;
import manage.ReceptionistManager;
import manage.RoomManager;
import model.GuestModel;
import model.MaidModel;
import model.RoomModel;
import model.WorkerModel;
import view.addEdit.AddEditGuest;

import view.addEdit.AddEditWorker;

public class MaidFrame extends JFrame{
	private static final long serialVersionUID = -8026201049950423764L;
	private RoomManager roomManager;

	protected JToolBar mainToolbar = new JToolBar();
	protected JButton btnAdd = new JButton();
	protected JButton btnEdit = new JButton();
	protected JButton btnDelete = new JButton();
	protected JTextField tfSearch = new JTextField(20);
	protected JTable table;
	private AssignmentManager asm;
	protected TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
	private JFrame parentFrame;
	private List<CleanupAssignment> assignments;
	public MaidFrame(JFrame parent, RoomManager roomManager, AssignmentManager asm, Worker maid) {
		this.roomManager = roomManager;
		this.parentFrame = parent;
		this.asm = asm;
		this.assignments = asm.getAssignmentsForHousekeeper(maid.getKorisnickoIme(), LocalDate.now());
		// podesavanje prozora
		setTitle("Sobe");
		setSize(800, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon("img/icon.png").getImage());

		// podesavanje toolbar-a
		ImageIcon editIcon = new ImageIcon("./img/edit.png");
		ImageIcon scaledEditIcon = new ImageIcon(editIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		editIcon = scaledEditIcon;
		btnEdit.setIcon(editIcon);
		mainToolbar.add(btnEdit);

		mainToolbar.setFloatable(false);
		add(mainToolbar, BorderLayout.NORTH);

		// podesavanje tabele
		table = new JTable(new MaidModel(assignments));
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

		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = table.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greška", JOptionPane.WARNING_MESSAGE);
				}else {

					CleanupAssignment assignment = assignments.get(red);
					assignment.setIsCompleted(true);
					asm.completeAssignment(assignment);
					assignments.remove(red);
					((AbstractTableModel) table.getModel()).fireTableDataChanged();

					JOptionPane.showMessageDialog(null, "Soba ociscena.", "Obavestenje", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

	}

	// potrebno osvezavanje podataka u tabeli bez gasenja prozora
	public void refreshData() {
		RoomModel sm = (RoomModel) this.table.getModel();
		sm.fireTableDataChanged();
	}

	// Pamcenje redosleda sortiranja za svaku kolonu posebno - primer
	@SuppressWarnings("serial")
	private Map<Integer, Integer> sortOrder = new HashMap<Integer, Integer>() {{put(0, 1);put(1, 1);put(2, 1);put(3, 1);}};

	// Manuelni sorter - potrebno za razumevanje rada podrazumevanog sortera tabele
	protected void sort(int index) {
		// index of table column

		this.roomManager.getRooms().sort(new Comparator<Room>() {
			int retVal = 0;
			@Override
			public int compare(Room o1, Room o2) {
				switch (index) {
				case 0:
					retVal = Integer.compare(o1.getBrojSobe(), o2.getBrojSobe());
					break;
				case 1:
					retVal = o1.getTip().getTypeName().compareTo(o2.getTip().getTypeName());
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
