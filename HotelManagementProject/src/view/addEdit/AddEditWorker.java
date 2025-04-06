package view.addEdit;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import entity.Role;
import entity.Worker;
import manage.AdministratorManager;
import view.WorkersFrame;

public class AddEditWorker extends JDialog {
	private static final long serialVersionUID = -5247231764310200252L;
	private AdministratorManager studMng;
	private Worker editS;
	private JFrame parent;

	// Jedan isti dijalog za Add i Edit
	public AddEditWorker(JFrame parent, AdministratorManager studMng, Worker editStudent) {
		super(parent, true);
		this.parent = parent;
		if (editStudent != null) {
			setTitle("Izmena radnika");
		} else {
			setTitle("Dodavanje radnika");
		}
		this.studMng = studMng;
		this.editS = editStudent;

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

        // Ime
        JLabel lblIme = new JLabel("Ime");
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 1;
        add(lblIme, con);

        JTextField txtIme = new JTextField(20);
        con.gridx = 1;
        con.gridy = 0;
        con.gridwidth = 2;
        add(txtIme, con);

        // Prezime
        JLabel lblPrezime = new JLabel("Prezime");
        con.gridx = 0;
        con.gridy = 1;
        con.gridwidth = 1;
        add(lblPrezime, con);

        JTextField txtPrezime = new JTextField(20);
        con.gridx = 1;
        con.gridy = 1;
        con.gridwidth = 2;
        add(txtPrezime, con);

        // Pol
        JLabel lblPol = new JLabel("Pol");
        con.gridx = 0;
        con.gridy = 2;
        con.gridwidth = 1;
        add(lblPol, con);

        JTextField txtPol = new JTextField(20);
        con.gridx = 1;
        con.gridy = 2;
        con.gridwidth = 2;
        add(txtPol, con);

        // Adresa
        JLabel lblAdresa = new JLabel("Adresa");
        con.gridx = 0;
        con.gridy = 3;
        con.gridwidth = 1;
        add(lblAdresa, con);

        JTextField txtAdresa = new JTextField(20);
        con.gridx = 1;
        con.gridy = 3;
        con.gridwidth = 2;
        add(txtAdresa, con);

        // Korisničko ime
        JLabel lblKorisnickoIme = new JLabel("Korisničko ime");
        con.gridx = 0;
        con.gridy = 4;
        con.gridwidth = 1;
        add(lblKorisnickoIme, con);

        JTextField txtKorisnickoIme = new JTextField(20);
        con.gridx = 1;
        con.gridy = 4;
        con.gridwidth = 2;
        add(txtKorisnickoIme, con);

        // Lozinka
        JLabel lblLozinka = new JLabel("Lozinka");
        con.gridx = 0;
        con.gridy = 5;
        con.gridwidth = 1;
        add(lblLozinka, con);

        JTextField txtLozinka = new JTextField(20);
        con.gridx = 1;
        con.gridy = 5;
        con.gridwidth = 2;
        add(txtLozinka, con);

        // Datum rođenja
        JLabel lblDatumRodjenja = new JLabel("Datum rođenja");
        con.gridx = 0;
        con.gridy = 6;
        con.gridwidth = 1;
        add(lblDatumRodjenja, con);

        JTextField txtDatumRodjenja = new JTextField(20);
        con.gridx = 1;
        con.gridy = 6;
        con.gridwidth = 2;
        add(txtDatumRodjenja, con);

        // Telefon
        JLabel lblTelefon = new JLabel("Telefon");
        con.gridx = 0;
        con.gridy = 7;
        con.gridwidth = 1;
        add(lblTelefon, con);

        JTextField txtTelefon = new JTextField(20);
        con.gridx = 1;
        con.gridy = 7;
        con.gridwidth = 2;
        add(txtTelefon, con);

        // Stručna sprema
        JLabel lblStrucnaSprema = new JLabel("Stručna sprema");
        con.gridx = 0;
        con.gridy = 8;
        con.gridwidth = 1;
        add(lblStrucnaSprema, con);

        JTextField txtStrucnaSprema = new JTextField(20);
        con.gridx = 1;
        con.gridy = 8;
        con.gridwidth = 2;
        add(txtStrucnaSprema, con);

        // Radni staž
        JLabel lblRadni = new JLabel("Radni staž");
        con.gridx = 0;
        con.gridy = 9;
        con.gridwidth = 1;
        add(lblRadni, con);

        JTextField txtRadni = new JTextField(20);
        con.gridx = 1;
        con.gridy = 9;
        con.gridwidth = 2;
        add(txtRadni, con);

        // Plata


        JRadioButton receptionistRadioButton = new JRadioButton("RECEPTIONIST");
        JRadioButton maidRadioButton = new JRadioButton("MAID");

        // Create a button group and add radio buttons to it
        ButtonGroup group = new ButtonGroup();
        group.add(receptionistRadioButton);
        group.add(maidRadioButton);

        con.gridx = 0;
        con.gridy = 10;
        con.gridwidth = 1;
        add(receptionistRadioButton, con);
        con.gridx = 1;
        add(maidRadioButton, con);

        JButton btnCancel = new JButton("Cancel");
        con.gridx = 1;
        con.gridy = 11;
        con.gridwidth = 1;
        add(btnCancel, con);

        JButton btnOK = new JButton("OK");
        con.gridx = 2;
        con.gridy = 11;
        con.gridwidth = 1;
        add(btnOK, con);


		if (editS != null) {
			txtIme.setText(editS.getIme());
			txtPrezime.setText(editS.getPrezime());
			txtPol.setText(editS.getPol());
			txtAdresa.setText(editS.getAdresa());
            txtKorisnickoIme.setText(editS.getKorisnickoIme());
			txtLozinka.setText(editS.getLozinka());
			txtDatumRodjenja.setText(editS.getDatumRodjenja() + "");
			txtTelefon.setText(editS.getTelefon() + "");
			txtStrucnaSprema.setText(editS.getStrucnaSprema() + "");
			txtRadni.setText(editS.getRadniStaz() + "");

		}

		btnOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String ime = txtIme.getText().trim();
				String prezime = txtPrezime.getText().trim();
				String pol = txtPol.getText().trim();
                String adresa = txtAdresa.getText().trim();
                String korisnickoIme = txtKorisnickoIme.getText().trim();
                String lozinka = txtLozinka.getText().trim();
                String datumRodjenja = txtDatumRodjenja.getText().trim();
				int telefon = Integer.parseInt(txtTelefon.getText().trim());
				int strucnaSprema = Integer.parseInt(txtStrucnaSprema.getText().trim());
				int radniStaz = Integer.parseInt(txtRadni.getText().trim());
				Role role = receptionistRadioButton.isSelected() ? Role.RECEPTIONIST : Role.MAID;


				if (editS != null) {
					studMng.edit(ime, prezime, pol, adresa, editS.getKorisnickoIme(), lozinka, LocalDate.parse(datumRodjenja), telefon, strucnaSprema, radniStaz);
				} else {
					studMng.addWorker(ime, prezime, pol, adresa, korisnickoIme, lozinka, LocalDate.parse(datumRodjenja), telefon, strucnaSprema, radniStaz, role);
				}
				((WorkersFrame) parent).refreshData();
				AddEditWorker.this.dispose();
			}
		});

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AddEditWorker.this.dispose();
			}
		});
	}

}
