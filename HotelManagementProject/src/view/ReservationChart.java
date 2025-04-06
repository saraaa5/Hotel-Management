package view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.PieStyler;

import entity.Reservation;
import entity.Worker;
import manage.ManagerFactory;

public class ReservationChart extends JFrame {

  private static final long serialVersionUID = 1L;
private JPanel contentPane;

private JFrame parent;
private ManagerFactory managers;

public ReservationChart(JFrame parent, ManagerFactory managers) {
	this.managers = managers;
	this.parent = parent;
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout());

    setContentPane(contentPane);

    contentPane.add(new XChartPanel<>(createReservationStatusPieChart()), BorderLayout.CENTER);

    JButton backButton = new JButton("Back");
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            if (parent != null) {
                parent.setVisible(true);
            }
        }
    });

    contentPane.add(backButton, BorderLayout.SOUTH);
}

private PieChart createReservationStatusPieChart() {
    PieChart chart = new PieChartBuilder().width(450).height(300)
            .title("Status of created reservations in the past 30 days.").build();

    chart.getStyler().setLegendPosition(PieStyler.LegendPosition.InsideNW);
    chart.getStyler().setLabelType(PieStyler.LabelType.NameAndValue);
    chart.getStyler().setAnnotationTextFontColor(Color.WHITE);

    for (Reservation.RezervacijaStatus s : Reservation.RezervacijaStatus.values()) {
      chart.addSeries(s.toString(), filterLogsByStatus(s));
    }



    return chart;
}

  private int filterLogsByStatus(Reservation.RezervacijaStatus status) {
    int counter = 0;
    List<Reservation> reservations = managers.getReservationManager().getReservations();

    for (Reservation r : reservations) {
      if (r.getStatusRezervacije().equals(status)) {
        counter++;
      }
    }
    return counter;
  }
}
