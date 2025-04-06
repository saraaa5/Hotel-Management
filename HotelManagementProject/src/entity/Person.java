package entity;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Person {
	private String ime, prezime, pol, adresa, korisnickoIme, lozinka;
	private java.time.LocalDate datumRodjenja;
	private int telefon;
	private Role role;
	public Person(String ime, String prezime, String pol, String adresa, String korisnickoIme, String lozinka,
			LocalDate datumRodjenja, int telefon, Role role) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.pol = pol;
		this.adresa = adresa;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.datumRodjenja = datumRodjenja;
		this.telefon = telefon;
		this.role = role;
	}

	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getPol() {
		return pol;
	}
	public void setPol(String pol) {
		this.pol = pol;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	public String getKorisnickoIme() {
		return korisnickoIme;
	}

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}
	public String getLozinka() {
		return lozinka;
	}
	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}
	public java.time.LocalDate getDatumRodjenja() {
		return datumRodjenja;
	}
	public void setDatumRodjenja(java.time.LocalDate datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}
	public int getTelefon() {
		return telefon;
	}
	public void setTelefon(int telefon) {
		this.telefon = telefon;
	}

	@Override
	public int hashCode() {
		return Objects.hash(adresa, datumRodjenja, ime, korisnickoIme, lozinka, pol, prezime, telefon);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return Objects.equals(adresa, other.adresa) && Objects.equals(datumRodjenja, other.datumRodjenja)
				&& Objects.equals(ime, other.ime) && Objects.equals(korisnickoIme, other.korisnickoIme)
				&& Objects.equals(lozinka, other.lozinka) && Objects.equals(pol, other.pol)
				&& Objects.equals(prezime, other.prezime) && telefon == other.telefon;
	}

	@Override
	public String toString() {
		return "Person [ime=" + ime + ", prezime=" + prezime + ", pol=" + pol + ", adresa=" + adresa
				+ ", korisnickoIme=" + korisnickoIme + ", lozinka=" + lozinka + ", datumRodjenja=" + datumRodjenja
				+ ", telefon=" + telefon + "]";
	}

}

