package view.addEdit;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import entity.Guest;
import entity.Worker;
import manage.AdministratorManager;
import manage.ReceptionistManager;
import view.GuestFrame;
import view.WorkersFrame;

public class AddEditGuest extends JDialog {
	private static final long serialVersionUID = -5247231764310200252L;
	private ReceptionistManager recMng;
	private Guest editS;
	private JFrame parent;

	// Jedan isti dijalog za Add i Edit
	public AddEditGuest(JFrame parent, ReceptionistManager recMng, Guest editStudent) {
		super(parent, true);
		this.parent = parent;
		if (editStudent != null) {
			setTitle("Izmena radnika");
		} else {
			setTitle("Dodavanje gosta");
		}
		this.recMng = recMng;
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


        JButton btnCancel = new JButton("Cancel");
        con.gridx = 1;
        con.gridy = 8;
        con.gridwidth = 1;
        add(btnCancel, con);

        JButton btnOK = new JButton("OK");
        con.gridx = 2;
        con.gridy = 8;
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

                Guest gost = new Guest(ime, prezime, pol, adresa, korisnickoIme, lozinka, LocalDate.parse(datumRodjenja), telefon);
				// odve se odvaja GUI od funkcionalnosti Manager-a
				// ne mesati logiku app i funkcionalnosti sa GUI-om !
				if (editS != null) {
					recMng.edit(ime, prezime, pol, adresa, editS.getKorisnickoIme(), lozinka, LocalDate.parse(datumRodjenja), telefon);
				} else {
					recMng.addGuest(gost);
				}
				((GuestFrame) parent).refreshData();
				AddEditGuest.this.dispose();
			}
		});

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AddEditGuest.this.dispose();
			}
		});
	}

}
