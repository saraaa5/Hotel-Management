package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.LegendPosition;

import manage.ManagerFactory;
import entity.Income;
import entity.RoomType;
public class IncomeChart extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JFrame parent;
    private ManagerFactory managers;

    public IncomeChart(JFrame parent, ManagerFactory managers) {
    	this.parent = parent;
        this.managers = managers;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 700);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());

        setContentPane(contentPane);

        // Create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for income by room type chart
        JPanel roomTypeIncomePanel = new JPanel();
        roomTypeIncomePanel.setLayout(new BorderLayout());
        roomTypeIncomePanel.add(new XChartPanel<>(createRoomTypeIncomeChart()), BorderLayout.CENTER);
        tabbedPane.addTab("Income by Room Type", roomTypeIncomePanel);

        // Panel for complete income chart
        JPanel completeIncomePanel = new JPanel();
        completeIncomePanel.setLayout(new BorderLayout());
        completeIncomePanel.add(new XChartPanel<>(createCompleteIncomeChart()), BorderLayout.CENTER);
        tabbedPane.addTab("Complete Income", completeIncomePanel);

        // Add the tabbed pane to the content pane
        contentPane.add(tabbedPane, BorderLayout.CENTER);

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

    private XYChart createRoomTypeIncomeChart() {
        // Sample data for room type income
        List<RoomType> roomTypes = managers.getRoomTypeManager().getRoomTypes();

        XYChart chart =
                new XYChartBuilder()
                        .width(800)
                        .height(600)
                        .title(getClass().getSimpleName())
                        .xAxisTitle("MONTHS")
                        .yAxisTitle("INCOME")
                        .build();

        // Customize Chart
        chart.getStyler().setLegendPosition(LegendPosition.OutsideE);
        chart.getStyler().setAxisTitlesVisible(false);
        chart.getStyler().setLegendPosition(LegendPosition.OutsideS);
        chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

        chart.getStyler().setCursorEnabled(true);
        chart.getStyler().setCursorColor(Color.GREEN);
        chart.getStyler().setCursorLineWidth(30f);
        chart.getStyler().setCursorFont(new Font("Verdana", Font.BOLD, 12));
        chart.getStyler().setCursorFontColor(Color.ORANGE);
        chart.getStyler().setCursorBackgroundColor(Color.BLUE);
        chart.getStyler().setCustomCursorXDataFormattingFunction(x -> "MONTH: " + x);
        chart.getStyler().setCustomCursorYDataFormattingFunction(y -> "INCOME: " + y);


        double[] months = new double[12];
        int currentMonth = LocalDate.now().getMonthValue();
        for (int i = 0; i < 12; i++) {
            if (currentMonth == 0) {
                currentMonth = 12;
            }

            months[currentMonth-1] = currentMonth;
            currentMonth--;
        }

        for (RoomType roomType : roomTypes) {
            double[] income = getIncomeByRoomType(roomType);

            chart.addSeries(roomType.getTypeName() + roomType.getNumberOfBeds(), months, income);
        }

        return chart;
    }

    private CategoryChart createCompleteIncomeChart() {
        // Sample data for complete income
        LocalDate today = LocalDate.now();
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(today.getMonth().toString());
            today = today.minusMonths(1);
        }

        List<Double> completeIncome = getCompleteIncome();

        CategoryChart chart =
                new CategoryChartBuilder()
                        .width(450)
                        .height(300)
                        .title("Complete Income in the Last 12 Months")
                        .xAxisTitle("Month")
                        .yAxisTitle("Income")
                        .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setLabelsVisible(true);

        chart.addSeries("Total Income", months, completeIncome);

        return chart;
    }

  private List<Double> getCompleteIncome() {
    List<Double> completeIncome = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      completeIncome.add(0.0);
    }

    LocalDate today = LocalDate.now();
    for (int i = 0; i < 12; i++) {
      for (Income income : managers.getIncomeManager().getIncomes()) {
        if (income.getDate().getMonthValue() == today.getMonthValue()) {
          completeIncome.set(i, completeIncome.get(i) + income.getAmount());
        }
      }
      today = today.minusMonths(1);
    }

    return completeIncome;
  }

  private double[] getIncomeByRoomType(RoomType roomType) {
    double[] income = new double[12];
    for (int i = 0; i < 12; i++) {
      income[i] = 0;
    }

    for (Income i : managers.getIncomeManager().getIncomes()) {
      if (i.getRoomTypeId().equals(roomType.getId())) {
        int month = i.getDate().getMonthValue();
        income[month - 1] += i.getAmount();
      }
    }

    return income;
  }
}
