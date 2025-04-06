package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import entity.Role;
import entity.Room;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import entity.CleanupAssignment;
import entity.Expense;
import entity.Income;
import entity.Reservation;
import entity.Worker;
import manage.ManagerFactory;

public class ReportsFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    private JFrame parent;


    public ReportsFrame(JFrame parent, ManagerFactory managers) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 700);
        this.parent = parent;
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(10, 10, 960, 640);
        contentPane.add(tabbedPane);

        createTab("PRIHODI/RASODI", managers);
        createTab("SPREMACICE/SOBE", managers);
        createTab("POTVRDJENE REZERVACIJE", 	    managers);
        createTab("ZAHTEVI REZERVACIJA", managers);
        createTab("ODBIJENE/OTKAZANE REZERVACIJE", managers);
        createTab("BROJ NOCENJA U SOBI", managers);
        createTab("PRIHOD PO SOBAMA", 	    managers);
    }

    private void createTab(String title, ManagerFactory managers) {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JComboBox<String> comboBox = new JComboBox<>();
        populateDateComboBox(comboBox);
        comboBox.setBounds(10, 10, 150, 30);
        panel.add(comboBox);

        JComboBox<String> comboBox2 = new JComboBox<>();
        populateDateComboBox(comboBox2);
        comboBox2.setBounds(160, 10, 150, 30);
        panel.add(comboBox2);

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(10, 50, 940, 480);
        panel.add(scrollPane);

        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 550, 100, 30);
    backButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
        	  parent.setVisible(true);
        	              dispose();
                      }
        });
        panel.add(backButton);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(850, 550, 100, 30);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	LocalDate date1 =
                        LocalDate.parse(
                            comboBox.getSelectedItem().toString(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalDate date2 =
                        LocalDate.parse(
                            comboBox2.getSelectedItem().toString(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    switch (title) {
                      case "PRIHODI/RASODI":
                        List<Income> incomes = managers.getIncomeManager().getIncomesByDate(date1, date2);
                        List<Expense> expenses =
                            managers.getExpenseManager().getExpensesByDate(date1, date2);
                        Double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();

                        Double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();

                        textArea.setText("Total income: " + totalIncome + "\n");
                        textArea.append("Total expense: " + totalExpense + "\n");
                        textArea.append("Net income: " + (totalIncome - totalExpense) + "\n");

                        break;

                      case "SPREMACICE/SOBE":
                        List<Worker> ws = managers.getAdministratorManager().getWorkers();
                        List<Worker> maids = new ArrayList<>();
                        for (Worker w : ws) {
                          if (w.getRole().equals(Role.MAID)) {
                            maids.add(w);
                          }
                        }

                        for (Worker w : maids) {
                          textArea.append(w.getKorisnickoIme() + "\n");
                          int totalAssignments = 0;

                          for (CleanupAssignment ca : managers.getAssignmentManager().getAssignments()) {
                            if ((ca.getDate().isAfter(date1) && ca.getDate().isBefore(date2))
                                && ca.getMaidId().equals(w.getKorisnickoIme())
                                && ca.getIsCompleted()){
                              totalAssignments++;
                            }
                          }
                          textArea.append(
                              "Total assignments: " + totalAssignments + "\n-----------------\n");
                        }
                        break;

                      case "POTVRDJENE REZERVACIJE":
                        List<Income> incomes2 = managers.getIncomeManager().getIncomesByDate(date1, date2);
                        textArea.setText("Reservations accepted: " + incomes2.size() + "\n");
                        break;

                      case "ZAHTEVI REZERVACIJA":
                        List<Reservation> reservations =
                            managers.getReservationManager().getReservationsByDates(date1, date2);
                        textArea.setText("Reservations requested: " + reservations.size() + "\n");

                        break;

                      case "ODBIJENE/OTKAZANE REZERVACIJE":
                        List<Reservation> reservations2 =
                            managers.getReservationManager().getReservationsByDates(date1, date2);
                        int rejectedCount = 0;
                        for (Reservation r : reservations2) {
                          if (!r.getStatusRezervacije().equals(Reservation.RezervacijaStatus.ODBIJENA)) {
                            rejectedCount++;
                          }
                        }

                        textArea.setText("Reservations rejected: " + rejectedCount + "\n");
                        int canceledCount = 0;
                        for (Reservation r : reservations2) {
                          if (r.getStatusRezervacije().equals(Reservation.RezervacijaStatus.OTKAZANA)) {
                            canceledCount++;
                          }
                        }

                        textArea.append("Reservations canceled: " + canceledCount + "\n");

                        break;

                      case "BROJ NOCENJA U SOBI":
                        List<Reservation> reservations3 =
                            managers.getReservationManager().getReservationsByDates(date1, date2);
                        List<Room> rooms = managers.getRoomManager().getRooms();

                        for (Room r : rooms) {
                          int totalNights = 0;
                          for (Reservation res : reservations3) {
                            if (res.getSoba().getBrojSobe() == r.getBrojSobe()
                                && res.getStatusRezervacije()
                                    .equals(Reservation.RezervacijaStatus.ZAVRSENA)) {
                              int diff =
                                  res.getDatumOdlaska().getDayOfYear()
                                      - res.getDatumDolaska().getDayOfYear();
                              totalNights += diff;
                            }
                          }
                          textArea.append(
                              "Room: "
                                  + r.getBrojSobe()
                                  + ", type: "
                                  + r.getTip().getTypeName()
                                  + ", beds: "
                                  + r.getTip().getNumberOfBeds()
                                  + "\n"
                                  + " Total nights: "
                                  + totalNights
                                  + "\n----------------\n");
                        }

                        break;

                      case "PRIHOD PO SOBAMA":
                        List<Room> rooms2 = managers.getRoomManager().getRooms();
                        List<Income> incomes3 = managers.getIncomeManager().getIncomesByDate(date1, date2);
                        for (Room r : rooms2) {
                          Double totalIncome2 = 0.0;
                          for (Income i : incomes3) {
                            if (i.getRoomNumber() == r.getBrojSobe()) {
                              totalIncome2 += i.getAmount();
                            }
                          }
                          textArea.append(
                              "Room: "
                                  + r.getBrojSobe()
                                  + ", type: "
                                  + r.getTip().getTypeName()
                                  + ", beds: "
                                  + r.getTip().getNumberOfBeds()
                                  + "\n"
                                  + " Total income: "
                                  + totalIncome2
                                  + "\n----------------\n");
                        }
                        break;
                    }
            }
        });
        panel.add(submitButton);

        tabbedPane.addTab(title, panel);
    }

    private void populateDateComboBox(JComboBox<String> comboBox) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        IntStream.range(0, 365).forEach(i -> {
            LocalDate date = currentDate.minusDays(i);
            comboBox.addItem(date.format(formatter));
        });
    }
}
