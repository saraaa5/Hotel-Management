//package view.addEdit;
//
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.time.LocalDate;
//import javax.swing.*;
//
//import entity.PriceList;
//import manage.PriceListManager;
//
//public class AddEditPriceList extends JDialog {
//    private JTextField tfDatumDolaska = new JTextField(20);
//    private JTextField tfDatumOdlaska = new JTextField(20);
//    private JTextField tfTipSobe = new JTextField(20);
//    private JTextField tfCenaSobe = new JTextField(20);
//    private JTextField tfDodatnaUsluga = new JTextField(20);
//    private JTextField tfCenaUsluge = new JTextField(20);
//
//    private PriceListManager priceListManager;
//    private PriceList priceList;
//
//    public AddEditPriceList(JFrame parent, PriceListManager priceListManager, PriceList priceList) {
//        super(parent, priceList == null ? "Dodaj Cenovnik" : "Izmeni Cenovnik", true);
//        this.priceListManager = priceListManager;
//        this.priceList = priceList;
//
//        setLayout(new BorderLayout());
//        JPanel panel = new JPanel(new GridLayout(6, 2));
//        panel.add(new JLabel("Datum Dolaska:"));
//        panel.add(tfDatumDolaska);
//        panel.add(new JLabel("Datum Odlaska:"));
//        panel.add(tfDatumOdlaska);
//        panel.add(new JLabel("Tip Sobe:"));
//        panel.add(tfTipSobe);
//        panel.add(new JLabel("Cena Sobe:"));
//        panel.add(tfCenaSobe);
//        panel.add(new JLabel("Dodatna Usluga:"));
//        panel.add(tfDodatnaUsluga);
//        panel.add(new JLabel("Cena Usluge:"));
//        panel.add(tfCenaUsluge);
//
//        add(panel, BorderLayout.CENTER);
//
//        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JButton btnOk = new JButton("OK");
//        JButton btnCancel = new JButton("Cancel");
//        buttonsPanel.add(btnOk);
//        buttonsPanel.add(btnCancel);
//
//        add(buttonsPanel, BorderLayout.SOUTH);
//
//        btnOk.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                LocalDate datumDolaska = LocalDate.parse(tfDatumDolaska.getText().trim());
//                LocalDate datumOdlaska = LocalDate.parse(tfDatumOdlaska.getText().trim());
//                String tipSobe = tfTipSobe.getText().trim();
//                double cenaSobe = Double.parseDouble(tfCenaSobe.getText().trim());
//                String dodatnaUsluga = tfDodatnaUsluga.getText().trim();
//                double cenaUsluge = Double.parseDouble(tfCenaUsluge.getText().trim());
//
//                if (priceList == null) {
//                    priceList = new PriceList(datumDolaska, datumOdlaska);
//                }
//                priceList.addRoomPrice(tipSobe, cenaSobe);
//                priceList.addAdditionalServicePrice(dodatnaUsluga, cenaUsluge);
//                priceListManager.addPriceList(priceList);
//                dispose();
//            }
//        });
//
//        btnCancel.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dispose();
//            }
//        });
//
//        pack();
//        setLocationRelativeTo(parent);
//    }
//}
