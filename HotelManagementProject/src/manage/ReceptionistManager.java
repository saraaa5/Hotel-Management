package manage;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import entity.Guest;
import entity.Worker;

import java.util.ArrayList;

public class ReceptionistManager {
	private List<Guest>gosti;
	private String gostiFile;
	public ReceptionistManager(String gostiFile) {
		this.gosti = new ArrayList<>();
		this.gostiFile = gostiFile;
	}
	public List<Guest> getGuests() {
		return gosti;
	}

	public void addGuest(Guest gost) {
	    gosti.add(gost);
	    saveData();
	}
    public Guest findGuestByUsername(String korisnickoIme) {
		loadData();
    	for (Guest guest : gosti) {
    		System.out.println(korisnickoIme + " " + guest.getKorisnickoIme());
			if (guest.getKorisnickoIme().equals(korisnickoIme)) {
				return guest;
			}

    	}
    	return null;
	}

	public String findGuestUsernameByUsername(String korisnickoIme) {
		Guest gost = findGuestByUsername(korisnickoIme);
		if (gost != null) {
			return gost.getKorisnickoIme();
		} else {
			return null;
		}
	}

	private boolean saveData() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(this.gostiFile, false));
			for (Guest g : gosti) {
				pw.println(g.toFileString());
			}
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public boolean loadData() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // Clear the existing list before loading new data
		this.gosti.clear();

		try (BufferedReader br = new BufferedReader(new FileReader(this.gostiFile))) {
			String linija;
			while ((linija = br.readLine()) != null) {
				String[] tokeni = linija.split(",");
				Guest guest = new Guest(
					tokeni[0],
					tokeni[1],
					tokeni[2],
					tokeni[3],
					tokeni[4],
					tokeni[5],
					LocalDate.parse(tokeni[6], formatter),
					Integer.parseInt(tokeni[7])
				);
				this.gosti.add(guest);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public Guest verifyLoginInfo(String korisnickoIme, String lozinka) {
		loadData();
		for (Guest guest : gosti) {
			if (guest.getKorisnickoIme().equals(korisnickoIme) && guest.getLozinka().equals(lozinka)) {
                return guest;
            }
        }
        return null;
    }

	public Guest findGuestByName(String guestName) {
		loadData();
		for (Guest guest : gosti) { // Pretpostavimo da imate listu gostiju
			if (guest.getIme().equals(guestName)) {
				return guest;
			}
		}
		return null;
	}

	public void edit(String ime, String prezime, String pol, String adresa, String korisnickoIme,
			String lozinka, LocalDate datumRodjenja, int telefon) {
		Guest s = this.findGuestByUsername(korisnickoIme);
		s.setIme(ime);
		s.setPrezime(prezime);
		s.setPol(pol);
		s.setAdresa(adresa);
		s.setKorisnickoIme(korisnickoIme);
		s.setLozinka(lozinka);
		s.setDatumRodjenja(datumRodjenja);
		s.setTelefon(telefon);
		this.saveData();
	}

	public void remove(String ime, String prezime) {
		Guest w = null;
		for (Guest guest : gosti) {
			if (guest.getIme().equals(ime) && guest.getPrezime().equals(prezime)) {
				w = guest;
				break;
			}
		}
		if (w!= null) {
			this.gosti.remove(w);
			this.saveData();
			System.out.println("Gost " + ime +" "+ prezime + " je uklonjen!");
		}
		else {
			System.out.println("Gost " + ime +" " + prezime + " nije pronađen!");
		}

	}
}
