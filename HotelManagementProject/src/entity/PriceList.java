package entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PriceList {
    private LocalDate datumDolaska;
    private LocalDate datumOdlaska;
    private Map<RoomType, Double> ceneSoba;
    private Map<AdditionalService, Double> ceneDodatnihUsluga;
    private String id;
	public PriceList(LocalDate datumDolaska, LocalDate datumOdlaska) {
		this.datumDolaska = datumDolaska;
		this.datumOdlaska = datumOdlaska;
		this.ceneSoba = new HashMap<RoomType, Double>();
		this.ceneDodatnihUsluga = new HashMap<AdditionalService, Double>();
		generateUUID();
	}


    private void generateUUID() {
        this.id =  UUID.randomUUID().toString();
    }

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public LocalDate getDatumDolaska() {
		return datumDolaska;
	}
	public void setDatumDolaska(LocalDate datumDolaska) {
		this.datumDolaska = datumDolaska;
	}
	public LocalDate getDatumOdlaska() {
		return datumOdlaska;
	}
	public void setDatumOdlaska(LocalDate datumOdlaska) {
		this.datumOdlaska = datumOdlaska;
	}
	public void addRoomPrice(RoomType tip, double cenaSobe) {
		ceneSoba.put(tip, cenaSobe);
	}
	public void addAdditionalServicePrice(AdditionalService dodatnaUsluga, double cenaUsluge) {
		ceneDodatnihUsluga.put(dodatnaUsluga, cenaUsluge);
	}

  public double getRoomPrice(RoomType tip) {
	  for (RoomType roomType : ceneSoba.keySet()) {
      if (roomType.getId().equals(tip.getId())) {
        return ceneSoba.get(roomType);
      }
	  }
        return 0;
  }

  public double getAdditionalServicePrice(AdditionalService dodatnaUsluga) {
    for (AdditionalService additionalService : ceneDodatnihUsluga.keySet()) {
      if (additionalService.getDodatnaUsluga().equals(dodatnaUsluga.getDodatnaUsluga())) {
        return ceneDodatnihUsluga.get(additionalService);
      }
    }
    return 0;
  }

  public void setRoomPrices(Map<RoomType, Double> ceneSoba) {
    this.ceneSoba = ceneSoba;
  }

  public void setAdditionalServicePrices(Map<AdditionalService, Double> ceneDodatnihUsluga) {
    this.ceneDodatnihUsluga = ceneDodatnihUsluga;
  }

  public Map<RoomType, Double> getRoomPrices() {
    return ceneSoba;
  }

  public Map<AdditionalService, Double> getAdditionalServicePrices() {
    return ceneDodatnihUsluga;
  }

  public String toString() {
    //write format is roomtype, rootype price, additional service, additional service price, start date, end date
	      StringBuilder sb = new StringBuilder();

    for (RoomType roomType : ceneSoba.keySet()) {
      sb.append(id + "," + roomType.getId() + "," + ceneSoba.get(roomType) + "," + "," + "," + datumDolaska + "," + datumOdlaska + "\n");

    }

    for (AdditionalService additionalService : ceneDodatnihUsluga.keySet()) {
      sb.append(id + "," +
          "," + ","
              + additionalService.getDodatnaUsluga() + ","
              + ceneDodatnihUsluga.get(additionalService) + "," + datumDolaska + "," + datumOdlaska + "\n");
    }

    return sb.toString();
  }
}
