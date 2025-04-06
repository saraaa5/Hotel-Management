package entity;

import java.time.LocalDate;

public class Guest extends Person {

	public Guest(String ime, String prezime, String pol, String adresa, String korisnickoIme, String lozinka,
			LocalDate datumRodjenja, int telefon) {
		super(ime, prezime, pol, adresa, korisnickoIme, lozinka, datumRodjenja, telefon, Role.GUEST);

	}
	public String toFileString() {
		return getIme() + "," +getPrezime()+ "," + getPol() + "," + getAdresa() + "," + getKorisnickoIme() + "," + getLozinka() + "," +
				getDatumRodjenja()+ "," + getTelefon();
	}

	public int compareTo(Guest gost) {
		return this.getKorisnickoIme().compareTo(gost.getKorisnickoIme());
	}


}
