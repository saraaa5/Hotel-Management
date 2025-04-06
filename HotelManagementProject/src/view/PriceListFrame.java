package view;
import javax.swing.*;

import entity.AdditionalService;
import entity.PriceList;
import entity.RoomType;
import manage.AdditionalServiceManager;
import manage.ManagerFactory;
import manage.PriceListManager;
import manage.RoomTypeManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class PriceListFrame extends JDialog {
    private JTextField startDateField;
    private JTextField endDateField;
    private JPanel servicesPanel;
    private JPanel roomsPanel;
    private JButton addServiceButton;
    private JButton addRoomButton;
    private JTextArea priceListTextArea;
    private JButton saveButton;
    private JButton cancelButton;

    private List<JComboBox<AdditionalService>> servicesDropdowns;
    private List<JTextField> servicePriceFields;
    private List<JComboBox<RoomType>> roomTypeDropdowns;
    private List<JTextField> roomPriceFields;
    private ManagerFactory managerFactory;
    private PriceList pricelist;
    private HashMap<AdditionalService, Double> services = new HashMap<>();
    private HashMap<RoomType, Double> roomTypes = new HashMap<>();
    private PriceListManager priceListManager;

    public PriceListFrame(Frame parent, ManagerFactory managerFactory) {
    	super(parent, "Add New Price List", true);
        getContentPane().setLayout(new BorderLayout());
        this.managerFactory = managerFactory;
        this.priceListManager = managerFactory.getPriceListManager();
        // Initialize components
        startDateField = new JTextField();
        endDateField = new JTextField();
        servicesPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        roomsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        addServiceButton = new JButton("Add Service");
        addServiceButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		AdditionalService service = (AdditionalService) servicesDropdowns.get(0).getSelectedItem();
        		double price = Double.parseDouble(servicePriceFields.get(0).getText());
        		services.put(service, price);
        		priceListTextArea.append("Service: " + service + " Price: " + price + "\n");
        	}
        });
        addRoomButton = new JButton("Add Room Type");
        addRoomButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            RoomType roomType = (RoomType) roomTypeDropdowns.get(0).getSelectedItem();
            double price = Double.parseDouble(roomPriceFields.get(0).getText());
            roomTypes.put(roomType, price);
            priceListTextArea.append("Room Type: " + roomType + " Price: " + price + "\n");
          }
        });
        priceListTextArea = new JTextArea(10, 30);
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        servicesDropdowns = new ArrayList<>();
        servicePriceFields = new ArrayList<>();
        roomTypeDropdowns = new ArrayList<>();
        roomPriceFields = new ArrayList<>();

        // Add initial service and room fields
        addServiceRow();
        addRoomRow();

        // Layout for the dialog
        JPanel datePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        datePanel.add(new JLabel("Start Date (dd/mm/yyyy):"));
        datePanel.add(startDateField);
        datePanel.add(new JLabel("End Date (dd/mm/yyyy):"));
        datePanel.add(endDateField);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        JPanel servicesContainer = new JPanel(new BorderLayout());
        servicesContainer.add(new JLabel("Services:"), BorderLayout.NORTH);
        servicesContainer.add(servicesPanel, BorderLayout.CENTER);
        servicesContainer.add(addServiceButton, BorderLayout.SOUTH);

        JPanel roomsContainer = new JPanel(new BorderLayout());
        roomsContainer.add(new JLabel("Room Types:"), BorderLayout.NORTH);
        roomsContainer.add(roomsPanel, BorderLayout.CENTER);
        roomsContainer.add(addRoomButton, BorderLayout.SOUTH);

        JPanel inputsPanel = new JPanel(new BorderLayout());
        inputsPanel.add(datePanel, BorderLayout.NORTH);
        inputsPanel.add(servicesContainer, BorderLayout.CENTER);
        inputsPanel.add(roomsContainer, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputsPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(priceListTextArea), BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        // Button listeners


        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePriceList(parent);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(parent);
    }

    private void addServiceRow() {
    	AdditionalServiceManager asm = managerFactory.getAdditionalServiceManager();
    	List<AdditionalService> services = asm.getServices();
        JComboBox<AdditionalService> servicesDropdown = new JComboBox<>(services.toArray(new AdditionalService[0]));
        JTextField servicePriceField = new JTextField();
        servicesPanel.add(servicesDropdown);
        servicesPanel.add(servicePriceField);
        servicesDropdowns.add(servicesDropdown);
        servicePriceFields.add(servicePriceField);
        servicesPanel.revalidate();
        servicesPanel.repaint();
    }

    private void addRoomRow() {
    	RoomTypeManager rtm = managerFactory.getRoomTypeManager();
    	List<RoomType> roomTypes = rtm.getRoomTypes();
        JComboBox<RoomType> roomTypeDropdown = new JComboBox<>(roomTypes.toArray(new RoomType[0]));
        JTextField roomPriceField = new JTextField();
        roomsPanel.add(roomTypeDropdown);
        roomsPanel.add(roomPriceField);
        roomTypeDropdowns.add(roomTypeDropdown);
        roomPriceFields.add(roomPriceField);
        roomsPanel.revalidate();
        roomsPanel.repaint();
    }

    private void savePriceList(Frame parent) {
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            if (start.isAfter(end)) {
                JOptionPane.showMessageDialog(parent, "Start date should be before end date.", "Invalid Dates", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (priceListManager.checkDateRange(start, end)) {
            	JOptionPane.showMessageDialog(parent, "There is an existing pricelist for the selected date!", "Invalid Dates", JOptionPane.ERROR_MESSAGE);
                return;
            }


            this.pricelist = new PriceList(start, end);
            this.pricelist.setAdditionalServicePrices(services);
            this.pricelist.setRoomPrices(roomTypes);
            priceListManager.addPriceList(pricelist);
            JOptionPane.showMessageDialog(parent, "Price List saved successfully.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Invalid input. Please check your data.\n" + ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
