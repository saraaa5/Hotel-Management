package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Reservation {
	private LocalDate datumDolaska;
	private LocalDate datumOdlaska;
	private RezervacijaStatus statusRezervacije;
	private String gost_username;
	private Room soba;
	private List<AdditionalService> dodatneUsluge = new ArrayList<AdditionalService>();
	private String id;
	private Double price;

	public Reservation(LocalDate datumDolaska, LocalDate datumOdlaska, RezervacijaStatus statusRezervacije, String gost_username, Room soba) {
        this.datumDolaska = datumDolaska;
        this.datumOdlaska = datumOdlaska;
        this.statusRezervacije = statusRezervacije;
        this.gost_username = gost_username;
        this.soba = soba;
        this.price = 0.0;
        generateUUID();
    }

	public String getUUID() {
		return id;
    }

	public void setUUID(String UUID) {
		this.id = UUID;

	}

	public Double getPrice() {
		return price;
    }

	public void setPrice(Double price) {
		this.price = price;
	}


    private void generateUUID() {
        this.id =  UUID.randomUUID().toString();
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

	public RezervacijaStatus getStatusRezervacije() {
		return statusRezervacije;
    }

	public void setStatusRezervacije(RezervacijaStatus statusRezervacije) {
		this.statusRezervacije = statusRezervacije;
    }

	public String getGost_username() {
		return gost_username;
    }

	public void setGost_username(String gost_username) {
		this.gost_username = gost_username;
    }

	public Room getSoba() {
		return soba;
    }

	public void setSoba(Room soba) {
		this.soba = soba;
    }

	public List<AdditionalService> getDodatneUsluge() {
		return dodatneUsluge;
    }

	public void setDodatneUsluge(List<AdditionalService> dodatneUsluge) {
		this.dodatneUsluge = dodatneUsluge;
	}

  public void appendDodatneUsluge(AdditionalService usluga) {
    this.dodatneUsluge.add(usluga);
  }


  @Override
  public String toString() {




	  StringBuilder sb = new StringBuilder();
	  sb.append(id).append(",").append(datumDolaska.toString()).append(",").append(datumOdlaska.toString()).append(",").append(statusRezervacije.toString()).append(",").append(gost_username).append(",").append(soba.getBrojSobe()).append(",");
    for (AdditionalService as : dodatneUsluge) {
      sb.append(as.getDodatnaUsluga()).append(",");
    }
    sb.append(price.toString());
    return sb.toString();
  }



	public enum RezervacijaStatus {
        NA_CEKANJU,
        POTVRDJENA,
        ODBIJENA,
        OTKAZANA,
        ZAVRSENA
    }

}
