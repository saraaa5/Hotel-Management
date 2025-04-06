package view.addEdit;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entity.RoomType;
import manage.RoomTypeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEditRoomType extends JDialog {
    private JTextField typeNameField;
    private JTextField numberOfBedsField;
    private JButton saveButton;
    private JFrame parent;
    private RoomTypeManager rtm;

    public AddEditRoomType(Frame parent, RoomTypeManager roomTypeManager) {

        super(parent, "Add Room Type", true);
        this.parent = (JFrame) parent;
        this.rtm = roomTypeManager;
        setLayout(new GridLayout(3, 2, 10, 10));
        setSize(800, 650);
        // Initialize components
        typeNameField = new JTextField();
        numberOfBedsField = new JTextField();
        saveButton = new JButton("Save");

        // Add components to the dialog
        add(new JLabel("Type Name:"));
        add(typeNameField);
        add(new JLabel("Number of Beds:"));
        add(numberOfBedsField);
        add(new JLabel());  // Placeholder for layout
        add(saveButton);

        // Add button listener
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String typeName = typeNameField.getText();
                String numberOfBeds = numberOfBedsField.getText();

                // Validate input
                if (typeName.isEmpty() || numberOfBeds.isEmpty()) {
                    JOptionPane.showMessageDialog(parent, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {


                    // Save the data (e.g., to a database or data structure)
                    // Here you would add the code to save the data
                	RoomType roomType = new RoomType(numberOfBeds, typeName);
                	rtm.addRoomType(roomType);
                    JOptionPane.showMessageDialog(parent, "Room type saved successfully.");
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(parent, "Please enter a valid number of beds.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        pack();
        setLocationRelativeTo(parent);
    }


}
