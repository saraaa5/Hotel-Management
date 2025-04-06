package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.AdditionalService;
import entity.PriceList;

public class PriceListManager {
    private List<PriceList> priceLists = new ArrayList<>();
    private String priceListFile;
    private RoomTypeManager roomTypeManager;
    private AdditionalServiceManager additionalServiceManager;

    public PriceListManager(String pricelistFile, RoomTypeManager roomTypeManager, AdditionalServiceManager additionalServiceManager) {
        this.priceLists = new ArrayList<>();
        this.priceListFile = pricelistFile;
        this.roomTypeManager = roomTypeManager;
        this.additionalServiceManager = additionalServiceManager;
    }

    public void saveData() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(this.priceListFile, false));
			for (PriceList a : priceLists) {
				pw.print(a.toString());
			}
			pw.close();
		} catch (IOException e) {
			System.out.println("Greška prilikom čuvanja podataka!");
		}

    }


    public boolean loadData() {

    try {
      BufferedReader br = new BufferedReader(new FileReader(this.priceListFile));
      String line = null;
      List<String[]> tokensList = new ArrayList<>();
      List<String[]> dateList = new ArrayList<>();
      while ((line = br.readLine()) != null) {
        String[] tokens = line.split(",");
        LocalDate datumDolaska = LocalDate.parse(tokens[5]);
        LocalDate datumOdlaska = LocalDate.parse(tokens[6]);

        boolean found = false;
        for (String[] date : dateList) {
          if (date[0].equals(datumDolaska.toString()) && date[1].equals(datumOdlaska.toString())) {
            found = true;
            break;
          }
        }
        if (!found) {
        	            dateList.add(new String[] {datumDolaska.toString(), datumOdlaska.toString()});

        }

        tokensList.add(tokens);


      }
      br.close();
      for(String[] pair : dateList) {
    	  PriceList priceList = new PriceList(LocalDate.parse(pair[0]), LocalDate.parse(pair[1]));
        for (String[] tokens : tokensList) {
          LocalDate datumDolaska = LocalDate.parse(tokens[5]);
          LocalDate datumOdlaska = LocalDate.parse(tokens[6]);
          if (datumDolaska.equals(priceList.getDatumDolaska())
              && datumOdlaska.equals(priceList.getDatumOdlaska())) {



            String rtId = tokens[1];

            String id = tokens[0];
            if(rtId.equals("")) {

            	String service = tokens[3];
            	Double servicePrice = Double.parseDouble(tokens[4]);
            	priceList.addAdditionalServicePrice(
            			additionalServiceManager.findAdditionalService(service), servicePrice);

            }else {
            	Double rtPrice = Double.parseDouble(tokens[2]);
            	priceList.addRoomPrice(roomTypeManager.findRoomTypeById(rtId), rtPrice);
            }
            priceList.setId(id);


          }
        }
        priceLists.add(priceList);
      }
    } catch (IOException e) {
      System.out.println("Greška prilikom čitanja podataka!");
      return false;
    }



    return true;
    }

    public void addPriceList(PriceList priceList) {
        this.priceLists.add(priceList);
        saveData();
    }

    public List<PriceList> getPriceLists() {
        return priceLists;
    }

    public PriceList findPriceListByDateRange(LocalDate datumDolaska, LocalDate datumOdlaska) {
        for (PriceList priceList : priceLists) {
            if (priceList.getDatumDolaska().equals(datumDolaska) && priceList.getDatumOdlaska().equals(datumOdlaska)) {
                return priceList;
            }
        }
        return null;
    }


    public void removePriceList(LocalDate datumDolaska, LocalDate datumOdlaska) {
        PriceList priceList = findPriceListByDateRange(datumDolaska, datumOdlaska);
        if (priceList != null) {
            priceLists.remove(priceList);
        }
    }

  public boolean checkDateRange(LocalDate datumDolaska, LocalDate datumOdlaska) {
    for (PriceList priceList : priceLists) {

      if (checkDateRange(datumDolaska, datumOdlaska, priceList)) {
        return true;
      }
    }
    return false;
  }

  public boolean checkDateRange(
      LocalDate datumDolaska, LocalDate datumOdlaska, PriceList priceList) {

      if ((datumDolaska.isAfter(priceList.getDatumDolaska())
              && datumDolaska.isBefore(priceList.getDatumOdlaska()))
          || (datumOdlaska.isAfter(priceList.getDatumDolaska())
              && datumOdlaska.isBefore(priceList.getDatumOdlaska()))
          || (datumDolaska.isEqual(priceList.getDatumDolaska())
              || datumOdlaska.isEqual(priceList.getDatumOdlaska()))
          || (datumDolaska.isBefore(priceList.getDatumDolaska())
              && datumOdlaska.isAfter(priceList.getDatumDolaska()))){
        return true;
      }
    return false;
  }
}
