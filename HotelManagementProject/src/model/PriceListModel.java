package model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.PriceList;
import manage.PriceListManager;

public class PriceListModel extends AbstractTableModel {
    private PriceListManager priceListManager;
    private String[] columnNames = {"Datum početka", "Datum kraja", "Tip Sobe", "Cena Sobe", "Dodatna Usluga", "Cena Usluge"};
    private List<PriceList> priceLists;

    public PriceListModel(PriceListManager priceListManager) {
        this.priceListManager = priceListManager;
        this.priceLists = priceListManager.getPriceLists();
    }

    @Override
    public int getRowCount() {
        return priceLists.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PriceList priceList = priceLists.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return priceList.getDatumDolaska();
            case 1:
                return priceList.getDatumOdlaska();
            case 2:
                // Implement logic to get room type and price (if available)
            case 3:
                // Implement logic to get room type and price (if available)
            case 4:
                // Implement logic to get additional service and price (if available)
            case 5:
                // Implement logic to get additional service and price (if available)
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
