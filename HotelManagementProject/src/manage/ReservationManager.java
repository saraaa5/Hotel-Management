package manage;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import entity.*;
import entity.Reservation.RezervacijaStatus;
import entity.Room.SobaStatus;
import manage.ReceptionistManager;
import manage.RoomManager;

public class ReservationManager {
    private List<Reservation> rezervacije;
    private String rezervacijeFile;
    private RoomManager roomManager;
    private AdditionalServiceManager adsm;
    private PriceListManager plm;
    private List<Housekeeper> sobarice;


    public ReservationManager(String rezervacijeFile, RoomManager roomManager, AdditionalServiceManager adsm, PriceListManager plm) {
        this.rezervacije = new ArrayList<Reservation>();
        this.rezervacijeFile = rezervacijeFile;
        this.adsm = adsm;
        this.roomManager = roomManager;
        this.plm = plm;
        this.sobarice = new ArrayList<>();

    }

  public List<Reservation> getReservationsForGuest(String username) {
	  List<Reservation> retList = new ArrayList<>();
    for (Reservation r : rezervacije) {
      if (r.getGost_username().equals(username)) {
    	  retList.add(r);
      }
    }
    return retList;
  }

  public Room getRoomByFeatures(RoomType tipSobe, boolean ac, boolean tv, boolean balcony, boolean smoking) {
	  List<Room> sobe = roomManager.getRooms();

    for (Room r : sobe) {
      if (r.getTip().getId().equals(tipSobe.getId()) && r.isHasAc() == ac && r.isHasTv() == tv && r.isHasBalcony() == balcony && r.isSmoking() == smoking && r.getStatusSobe().equals(SobaStatus.SLOBODNA)) {
        return r;
      }
    }

	  return null;

  }

  public Room checkAvailability(LocalDate datumDolaska, LocalDate datumOdlaska, RoomType tipSobe, boolean ac, boolean tv, boolean balcony, boolean smoking, Reservation editRes) {
    Room r = getRoomByFeatures(tipSobe, ac, tv, balcony, smoking);

    if (r == null) {
      return null;
    }
    if (isRoomAvailable(r, datumDolaska, datumOdlaska, editRes)) {
      return r;
    }
    return null;
  }

    public List<Reservation> getReservations() {
        return rezervacije;
    }

    public List<Reservation> getReservationsByDates(LocalDate start, LocalDate end){
    List<Reservation> retList = new ArrayList<>();
    for (Reservation r : rezervacije) {
      if ((r.getDatumDolaska().isAfter(start) || r.getDatumDolaska().isEqual(start)) && (r.getDatumDolaska().isBefore(end) || r.getDatumDolaska().isEqual(end))) {
        retList.add(r);
      }
    }
    return retList;
    }

    public Double addReservation(Reservation rezervacija) {
        this.rezervacije.add(rezervacija);
        Double price = calculatePrice(rezervacija);
        this.saveData();
        return price;
    }

  public Double calculatePrice(Reservation rezervacija) {
	  List<PriceList> cenovnici = plm.getPriceLists();

	  Double price = 0.0;

	  long days = rezervacija.getDatumDolaska().until(rezervacija.getDatumOdlaska()).getDays();

	  double [] prices = new double[(int)days];

	  for (PriceList p : cenovnici) {
      if (rezervacija.getDatumDolaska().isBefore(p.getDatumDolaska())
          && rezervacija.getDatumOdlaska().isAfter(p.getDatumOdlaska())) {
    	  long startDiff = rezervacija.getDatumDolaska().until(p.getDatumDolaska()).getDays();
    	  long endDiff = p.getDatumOdlaska().until(rezervacija.getDatumOdlaska()).getDays();
        for (int i = 0; i < days; i++) {
          if (i < startDiff || i > endDiff) {
            continue;
          } else {
            prices[i] = generateDailyPrices(rezervacija, p);
          }

        }
      }
      if((rezervacija.getDatumDolaska().isAfter(p.getDatumDolaska()) && rezervacija.getDatumOdlaska().isBefore(p.getDatumOdlaska())) || (rezervacija.getDatumDolaska().isEqual(p.getDatumDolaska()) && rezervacija.getDatumOdlaska().isEqual(p.getDatumOdlaska()))) {
        for (int i = 0; i < days; i++) {
          prices[i] = generateDailyPrices(rezervacija, p);
        }
      }
      if(rezervacija.getDatumDolaska().isBefore(p.getDatumDolaska()) && rezervacija.getDatumOdlaska().isAfter(p.getDatumDolaska()) && rezervacija.getDatumOdlaska().isBefore(p.getDatumOdlaska())) {
        long startDiff = rezervacija.getDatumDolaska().until(p.getDatumDolaska()).getDays();
        for (int i = 0; i < days; i++) {
          if (i < startDiff) {
            continue;
          } else {
            prices[i] = generateDailyPrices(rezervacija, p);
          }
        }
      }

      if(rezervacija.getDatumDolaska().isAfter(p.getDatumDolaska()) && rezervacija.getDatumDolaska().isBefore(p.getDatumOdlaska()) && rezervacija.getDatumOdlaska().isAfter(p.getDatumOdlaska())) {
        long endDiff = p.getDatumOdlaska().until(rezervacija.getDatumOdlaska()).getDays();
        for (int i = 0; i < days; i++) {
          if (i > endDiff) {
            continue;
          } else {
            prices[i] = generateDailyPrices(rezervacija, p);
          }
        }
      }

    }

    for (int i = 0; i < days; i++) {
      price += prices[i];
    }
    rezervacija.setPrice(price);
    return price;
  }

  public void setFinished(Reservation s) {
	  s.setStatusRezervacije(RezervacijaStatus.ZAVRSENA);
	  saveData();
  }


  private double generateDailyPrices(Reservation res, PriceList p) {
	  double sum = 0.0;
	  double roomPrice = p.getRoomPrice(res.getSoba().getTip());
    for (AdditionalService as : res.getDodatneUsluge()) {
      sum += p.getAdditionalServicePrice(as);
    }
    return sum + roomPrice;
  }

  public void promeniStatusSobe(Room soba, Room.SobaStatus noviStatus) {
  }


    public boolean isRoomAvailable(Room soba, LocalDate datumDolaska, LocalDate datumOdlaska, Reservation editRes) {
       for (Reservation r : rezervacije) {
    	   if (editRes != null && r.getUUID().equals(editRes.getUUID())) {
    		   	continue;
      }
      if (r.getStatusRezervacije().equals(RezervacijaStatus.POTVRDJENA)) {
      if (r.getSoba().getBrojSobe() == soba.getBrojSobe()
          && checkDateRange(datumDolaska, datumOdlaska, r.getDatumDolaska(), r.getDatumOdlaska())) {

    	  return false;
      	}
      continue;

      }

     }
       return true;
    }


    public boolean checkDateRange(
    	      LocalDate datumDolaska, LocalDate datumOdlaska, LocalDate dolazak2, LocalDate odlazak2) {

    	      if ((datumDolaska.isAfter(dolazak2)
    	              && datumDolaska.isBefore(odlazak2))
    	          || (datumOdlaska.isAfter(dolazak2)
    	              && datumOdlaska.isBefore(odlazak2))
    	          || (datumDolaska.isEqual(dolazak2)
    	              || datumOdlaska.isEqual(odlazak2))
    	          || (datumDolaska.isBefore(dolazak2)
    	              && datumOdlaska.isAfter(dolazak2))){
    	        return true;
    	      }
    	    return false;
    	  }

    public void edit(Reservation rezervacija) {
    	        this.saveData();
    }

  public Reservation findReservation(String id) {
    for (Reservation r : rezervacije) {
      if (r.getUUID().equals(id)) {
        return r;
      }
    }
    return null;
  }


  public void cleanupReservations() {
	      for (Reservation r : rezervacije) {
	    	  	if ((r.getDatumDolaska().isBefore(LocalDate.now()) || r.getDatumDolaska().isEqual(LocalDate.now())) && r.getStatusRezervacije().equals(RezervacijaStatus.NA_CEKANJU)) {
	    	  				r.setStatusRezervacije(RezervacijaStatus.ODBIJENA);
      		}
	      }
	      saveData();
  }


  public boolean loadData() {
	  try {BufferedReader br = new BufferedReader(new FileReader(this.rezervacijeFile));

	  //complete the reading logic

	  String line = null;
	  while ((line = br.readLine()) != null) {

		  String[] tokens = line.split(",");

		  String id = tokens[0];
		  String datumDolaska = tokens[1];
		  String datumOdlaska = tokens[2];

		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		  LocalDate dateDolaska = LocalDate.parse(datumDolaska, formatter);
		  LocalDate dateOdlaska = LocalDate.parse(datumOdlaska, formatter);

		  String statusRezervacije = tokens[3];
		  String gostUsername = tokens[4];
		  String roomNumber = tokens[5];

		  Room soba = roomManager.findRoom(Integer.parseInt(roomNumber));
		  RezervacijaStatus status = RezervacijaStatus.valueOf(statusRezervacije);

		  int base = 6;
		  List<AdditionalService> dodatneUsluge = new ArrayList<>();

        while (base < tokens.length-1) {
          String uslugaIme = tokens[base];
          AdditionalService usluga = adsm.findAdditionalService(uslugaIme);
          dodatneUsluge.add(usluga);
          base++;
        }
        Double cena = Double.parseDouble(tokens[tokens.length-1]);

        Reservation rezervacija = new Reservation(dateDolaska, dateOdlaska, status, gostUsername, soba);
        rezervacija.setUUID(id);
        rezervacija.setDodatneUsluge(dodatneUsluge);
        rezervacija.setPrice(cena);
        rezervacije.add(rezervacija);
	  }

	  br.close();

	  } catch (IOException e){// TODO Auto-generated catch block
		  e.printStackTrace();
		  return false;

	  }


	  return true;
  }

    public Reservation FindReservationByDateAndGuest(LocalDate datumDolaska, LocalDate datumOdlaska, String korisnickoIme) {
        return null;
        }

    public void edit(LocalDate datumDolaska, LocalDate datumOdlaska, RezervacijaStatus statusRezervacije, List<AdditionalService> dodatneUsluge, String korisnickoIme, Room room) {

    }


    private List<String[]> transformData(){
		return null;
    }


    public boolean saveData() {
        	BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(this.rezervacijeFile));
      for (Reservation r : rezervacije) {
        bw.write(r.toString());
        bw.newLine();
      }
      bw.close();
    } catch (IOException e) {
      return false;
    }

    return true;
    }

    public void remove(LocalDate datumDolaska, LocalDate datumOdlaska, String korisnickoIme) {

    }
}
