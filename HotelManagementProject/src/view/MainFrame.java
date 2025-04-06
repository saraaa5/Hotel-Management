package view;

import entity.Guest;
import entity.Role;
import entity.Worker;
import manage.AdministratorManager;
import manage.ManagerFactory;
import manage.ReceptionistManager;
import view.PriceListFrame;
import view.addEdit.AddEditRoomType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 8456560429229699542L;

    private ManagerFactory managers;

    public MainFrame(ManagerFactory managers) {
        this.managers = managers;

        loginDialog();
    }

    private void loginDialog() {
        JDialog d = new JDialog();
        d.setTitle("Prijava - Hotel SunnyPal");
        d.setLocationRelativeTo(null);
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        d.setResizable(false);
        initLoginGUI(d);
        d.pack();
        d.setVisible(true);
    }

    private void initLoginGUI(JDialog dialog) {
    GridLayout layout = new GridLayout(4, 2, 0, 20);
    dialog.setLayout(layout);

    JTextField tfKorisnickoIme = new JTextField(20);
    JPasswordField pfLozinka = new JPasswordField(20);
    JButton btnOk = new JButton("OK");
    JButton btnCancel = new JButton("Cancel");

    dialog.getRootPane().setDefaultButton(btnOk);

    dialog.add(new JLabel("Dobrodošli! Molimo da se prijavite:"));
    dialog.add(new JLabel());
    dialog.add(new JLabel("Korisničko ime"));
    dialog.add(tfKorisnickoIme);
    dialog.add(new JLabel("Šifra"));
    dialog.add(pfLozinka);
    dialog.add(btnOk);
    dialog.add(btnCancel);

    btnOk.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            AdministratorManager administratorManager = new AdministratorManager("./data/zaposleni.csv");
            ReceptionistManager receptionistManager = new ReceptionistManager("./data/gosti.csv");
            String korisnickoIme = tfKorisnickoIme.getText().trim();
            String lozinka = new String(pfLozinka.getPassword()).trim();
            System.out.println(korisnickoIme + " " + lozinka);
            if (korisnickoIme.isEmpty() || lozinka.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Niste uneli sve podatke.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Worker user = administratorManager.verifyLoginInfo(korisnickoIme, lozinka);
            if (user != null) {
                dialog.setVisible(false);
                dialog.dispose();
                showMainWindow(user);
                return;
            }

            Guest guest = receptionistManager.verifyLoginInfo(korisnickoIme, lozinka);
            if (guest != null) {
                dialog.setVisible(false);
                dialog.dispose();
                showGuestWindow(guest);
                return;
            }
            JOptionPane.showMessageDialog(null, "Pogrešno korisničko ime ili lozinka.", "Greška", JOptionPane.ERROR_MESSAGE);
        }
    });

    btnCancel.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.setVisible(false);
            dialog.dispose();
        }
    });
}


    private void showMainWindow(Worker user) {
        Role role = user.getRole();
        if (role.equals(Role.ADMIN)) {
            showAdminWindow();
        } else if (role.equals(Role.RECEPTIONIST)) {
            showRecepcionerWindow();
        } else if (role.equals(Role.MAID)) {
            showSobaricaWindow(user);
        } else {
            JOptionPane.showMessageDialog(this, "Nepoznata uloga korisnika!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
	private void showGuestWindow(Guest guest) {
		this.setTitle("Gost - Hotel SunnyPal");
        initGostGUI(guest);
        this.setVisible(true);
	}

    private void showAdminWindow() {
        this.setTitle("Admin - Hotel SunnyPal");
        initAdminGUI();
        this.setVisible(true);
    }

    private void showRecepcionerWindow() {
        this.setTitle("Recepcioner - Hotel SunnyPal");
        initRecepcionerGUI();
        this.setVisible(true);
    }

    private void showSobaricaWindow(Worker user) {
        this.setTitle("Sobarica - Hotel SunnyPal");
        initSobaricaGUI(user);
        this.setVisible(true);
    }


    private void initAdminGUI() {
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(new ImageIcon("img/icon.png").getImage());

        JMenuBar mainMenu = new JMenuBar();

		JMenu menu = new JMenu("Meni");
		JMenuItem zaposleniItem = new JMenuItem("Zaposleni");
		JMenuItem gostiItem = new JMenuItem("Gosti");
        JMenuItem rezervacijeItem = new JMenuItem("Rezervacije");
		JMenuItem sobeItem = new JMenuItem("Sobe");
		JMenuItem cenovnikItem = new JMenuItem("Cenovnik");
		JMenuItem rtItem = new JMenuItem("Tipovi soba");
        JMenuItem izvestajiItem = new JMenuItem("Izveštaji");
        JMenuItem incomeChartItem = new JMenuItem("Chartovi Prihoda");
        JMenuItem maidChart = new JMenuItem("Chartovi sobarica");
        JMenuItem reservationChart = new JMenuItem("Chartovi rezervacija");
        JMenuItem logoutItem = new JMenuItem("Odjava");


		menu.add(zaposleniItem);
		menu.add(gostiItem);
        menu.add(rezervacijeItem);
		menu.add(sobeItem);
		menu.add(cenovnikItem);
		menu.add(rtItem);
        menu.add(izvestajiItem);
        menu.add(maidChart);
        menu.add(incomeChartItem);
        menu.add(reservationChart);
        menu.add(logoutItem);


		mainMenu.add(menu);

		this.setJMenuBar(mainMenu);

    reservationChart.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            ReservationChart tf = new ReservationChart(MainFrame.this, managers);
            tf.setVisible(true);
          }
        });

    maidChart.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            MaidChart tf = new MaidChart(MainFrame.this, managers);
            tf.setVisible(true);
          }
        });

    incomeChartItem.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            IncomeChart tf = new IncomeChart(MainFrame.this, managers);
            tf.setVisible(true);
          }
        });

		izvestajiItem.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
                ReportsFrame tf = new ReportsFrame(MainFrame.this, managers);
                tf.setVisible(true);
                }
		});

	    rtItem.addActionListener(new ActionListener() {
	    	            @Override
	    	 public void actionPerformed(ActionEvent e) {
	    	            	AddEditRoomType tf = new AddEditRoomType(MainFrame.this, managers.getRoomTypeManager());
	    	            	tf.setVisible(true);
	    	            }
	    });

    cenovnikItem.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            PriceListFrame tf = new PriceListFrame(MainFrame.this, managers);
            tf.setVisible(true);
          }
        });

		zaposleniItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WorkersFrame tf = new WorkersFrame(MainFrame.this, managers.getAdministratorManager());
				tf.setVisible(true);
			}
		});
        sobeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RoomsFrame tf = new RoomsFrame(MainFrame.this, managers.getRoomManager(), managers.getRoomTypeManager());
				tf.setVisible(true);
			}
		});
        rezervacijeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ReservationFrame tf = new ReservationFrame(MainFrame.this, managers, false);
				tf.setVisible(true);
			}
		});
        gostiItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GuestFrame tf = new GuestFrame(MainFrame.this, managers.getReceptionistManager());
				tf.setVisible(true);
			}
		});

		    JLabel hotelImageLabel = new JLabel();
			hotelImageLabel.setHorizontalAlignment(JLabel.CENTER);
			hotelImageLabel.setVerticalAlignment(JLabel.CENTER);
			ImageIcon hotelImageIcon = new ImageIcon("./img/sunnypal.jpg"); // Putanja do slike

            Image image = hotelImageIcon.getImage();
            Image scaledImage = image.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            ImageIcon scaledHotelImageIcon = new ImageIcon(scaledImage);
            hotelImageLabel.setIcon(scaledHotelImageIcon);
			add(hotelImageLabel, BorderLayout.CENTER);

            logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Zatvori trenutni prozor
                loginDialog(); // Prikazi prozor za prijavu
            }
        });
    }


    private void initRecepcionerGUI() {
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(new ImageIcon("img/icon.png").getImage());

        JMenuBar mainMenu = new JMenuBar();

		JMenu menu = new JMenu("Meni");
        JMenuItem rezervacijeItem = new JMenuItem("Upravljanje rezervacijama/check in/check out");
		JMenuItem sobeItem = new JMenuItem("Sobe");
        JMenuItem gostiItem = new JMenuItem("Gosti");
        JMenuItem logoutItem = new JMenuItem("Odjava");

        menu.add(rezervacijeItem);
		menu.add(sobeItem);
        menu.add(gostiItem);
        menu.add(logoutItem);

		mainMenu.add(menu);

		this.setJMenuBar(mainMenu);

		rezervacijeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ReservationFrame tf = new ReservationFrame(MainFrame.this, managers, true);
				tf.setVisible(true);
			}
		});
        sobeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RoomsFrame tf = new RoomsFrame(MainFrame.this, managers.getRoomManager(), managers.getRoomTypeManager());
				tf.setVisible(true);
			}
		});
        gostiItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GuestFrame tf = new GuestFrame(MainFrame.this, managers.getReceptionistManager());
				tf.setVisible(true);
			}
		});

		    JLabel hotelImageLabel = new JLabel();
			hotelImageLabel.setHorizontalAlignment(JLabel.CENTER);
			hotelImageLabel.setVerticalAlignment(JLabel.CENTER);
			ImageIcon hotelImageIcon = new ImageIcon("./img/sunnypal.jpg"); // Putanja do slike

            Image image = hotelImageIcon.getImage();
            Image scaledImage = image.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            ImageIcon scaledHotelImageIcon = new ImageIcon(scaledImage);
            hotelImageLabel.setIcon(scaledHotelImageIcon);
			add(hotelImageLabel, BorderLayout.CENTER);

                        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                loginDialog();
            }
        });
    }

    private void initSobaricaGUI(Worker user) {
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(new ImageIcon("img/icon.png").getImage());

        JMenuBar mainMenu = new JMenuBar();

        JMenu sobaricaMenu = new JMenu("Sobarica meni");
        JMenuItem manageRoomsItem = new JMenuItem("Upravljanje sobama");
        JMenuItem logoutItem = new JMenuItem("Odjava");


        sobaricaMenu.add(manageRoomsItem);
        sobaricaMenu.add(logoutItem);


        mainMenu.add(sobaricaMenu);

        this.setJMenuBar(mainMenu);
        manageRoomsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MaidFrame tf = new MaidFrame(MainFrame.this, managers.getRoomManager(), managers.getAssignmentManager(), user);
				tf.setVisible(true);
			}
		});
        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                loginDialog();
            }
        });
        JLabel hotelImageLabel = new JLabel();
			hotelImageLabel.setHorizontalAlignment(JLabel.CENTER);
			hotelImageLabel.setVerticalAlignment(JLabel.CENTER);
			ImageIcon hotelImageIcon = new ImageIcon("./img/sunnypal.jpg"); // Putanja do slike

            Image image = hotelImageIcon.getImage();
            Image scaledImage = image.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            ImageIcon scaledHotelImageIcon = new ImageIcon(scaledImage);
            hotelImageLabel.setIcon(scaledHotelImageIcon);
			add(hotelImageLabel, BorderLayout.CENTER);
    }

    private void initGostGUI(Guest gost) {
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(new ImageIcon("img/icon.png").getImage());

        JMenuBar mainMenu = new JMenuBar();

        JMenu gostMenu = new JMenu("Gost meni");
        JMenuItem viewReservationsItem = new JMenuItem("Pregled rezervacija");

        gostMenu.add(viewReservationsItem);

        mainMenu.add(gostMenu);

        this.setJMenuBar(mainMenu);
        viewReservationsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GuestReservationFrame tf = new GuestReservationFrame(MainFrame.this, managers, gost);
				tf.setVisible(true);
			}
		});
                JLabel hotelImageLabel = new JLabel();
			hotelImageLabel.setHorizontalAlignment(JLabel.CENTER);
			hotelImageLabel.setVerticalAlignment(JLabel.CENTER);
			ImageIcon hotelImageIcon = new ImageIcon("./img/sunnypal.jpg"); // Putanja do slike

            Image image = hotelImageIcon.getImage();
            Image scaledImage = image.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            ImageIcon scaledHotelImageIcon = new ImageIcon(scaledImage);
            hotelImageLabel.setIcon(scaledHotelImageIcon);
			add(hotelImageLabel, BorderLayout.CENTER);
    }
}

