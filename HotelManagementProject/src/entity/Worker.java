package entity;

import java.time.LocalDate;

public class Worker extends Person {

	private int strucnaSprema, radniStaz;
    private double osnovica, koeficijent, plata;

	public Worker(String ime, String prezime, String pol, String adresa, String korisnickoIme,
			String lozinka, LocalDate datumRodjenja, int telefon, int strucnaSprema, int radniStaz, Role role
			) {
		super(ime, prezime, pol, adresa, korisnickoIme, lozinka, datumRodjenja, telefon, role);
		this.strucnaSprema = strucnaSprema;
		this.radniStaz = radniStaz;
		setKoeficijent();

	}
	public int getStrucnaSprema() {
		return strucnaSprema;
	}
	public void setStrucnaSprema(int strucnaSprema) {
		this.strucnaSprema = strucnaSprema;
	}
	public int getRadniStaz() {
		return radniStaz;
	}
	public void setRadniStaz(int radniStaz) {
		this.radniStaz = radniStaz;
	}
	public double getPlata() {
		return plata;
	}
	public void setPlata(int plata) {
		this.plata = plata;
	}
	public double getOsnovica() {
		return osnovica;
	}
	public void setOsnovica(double osnovica) {
		this.osnovica = osnovica;
	}
	public double getKoeficijent() {
		return koeficijent;
	}
	public void setKoeficijent() {
		this.koeficijent = this.strucnaSprema * 0.5 + this.radniStaz * 1000;
	}

	public void setSalary() {
    if (this.getRole() == Role.MAID) {
      this.osnovica = Osnovica.OSNOVICA_MAID.getValue();
    } else {
      this.osnovica = Osnovica.OSNOVICA_REC.getValue();
    }

    this.plata = (double) (this.osnovica + 0.5 * this.koeficijent);
	}

	@Override
    public String toString() {
        return toFileString();
    }

	public String toFileString() {
		return getIme() + "," + getPrezime() + "," + getPol() + "," + getAdresa() + "," + getKorisnickoIme() + "," + getLozinka() + "," +
				getDatumRodjenja() + "," + getTelefon() + "," + strucnaSprema + "," + radniStaz + "," + plata + "," + osnovica + "," + koeficijent + "," + getRole();
	}

	enum Osnovica {
        OSNOVICA_MAID(60000), OSNOVICA_REC(70000);

        private int value;

        private Osnovica(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


}
