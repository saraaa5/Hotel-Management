package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import entity.Role;
import entity.Worker;
import manage.ManagerFactory;

import org.knowm.xchart.*;
import org.knowm.xchart.style.PieStyler;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MaidChart extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JFrame parent;
    private ManagerFactory managers;

    public MaidChart(JFrame parent, ManagerFactory managers) {
        this.parent = parent;
        this.managers = managers;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());

        setContentPane(contentPane);

        // Add the XChartPanel with the pie chart to the content pane
        contentPane.add(new XChartPanel<>(createMaidWorkPieChart()), BorderLayout.CENTER);

        // Add a back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current frame
                if (parent != null) {
                    parent.setVisible(true); // Show the parent frame
                }
            }
        });

        // Add the back button to the content pane at the bottom
        contentPane.add(backButton, BorderLayout.SOUTH);
    }

    private PieChart createMaidWorkPieChart() {
        PieChart chart = new PieChartBuilder().width(450).height(300)
                .title("Workload of workers (past 30 days)").build();

        chart.getStyler().setLegendPosition(PieStyler.LegendPosition.InsideNW);
        chart.getStyler().setLabelType(PieStyler.LabelType.NameAndValue);
        chart.getStyler().setAnnotationTextFontColor(Color.WHITE);

        // Example data - replace with actual data
        List<Worker> workers = managers.getAdministratorManager().getWorkers();
        List<Worker> maids = new ArrayList<>();
    for (Worker worker : workers) {
      if (worker.getRole().equals(Role.MAID)) {
        maids.add(worker);
      }
    }

        for (Worker w : maids) {
        	int count = managers.getAssignmentManager().getLastMonthCount(w.getKorisnickoIme());
        	chart.addSeries(w.getKorisnickoIme(), count);
        }

        return chart;
    }


}
