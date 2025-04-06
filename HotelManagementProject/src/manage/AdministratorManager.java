package manage;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;


import entity.AdditionalService;
import entity.Role;
import entity.Worker;

public class AdministratorManager {
	private List<Worker>zaposleni;
	private String zaposleniFile;
	public AdministratorManager(String zaposleniFile) {
		super();
		this.zaposleniFile = zaposleniFile;
		this.zaposleni = new ArrayList<Worker>();
	}

    public List<Worker> getWorkers() {
		return zaposleni;
	}

	public void addWorker(String ime, String prezime, String pol, String adresa, String korisnickoIme,
			String lozinka, LocalDate datumRodjenja, int telefon, int strucnaSprema, int radniStaz, Role role) {
		Worker w = new Worker(ime, prezime, pol, adresa, korisnickoIme, lozinka, datumRodjenja, telefon, strucnaSprema, radniStaz, role);
		w.setSalary();
		this.zaposleni.add(w);
		this.saveData();
	}
	public void allWorkers() {
		System.out.println("Spisak zaposlenih: ");
		for (Worker worker: zaposleni) {
			System.out.println(worker);
		}
	}
	public Worker FindWorkerByUsername(String korisnickoIme) {
    	Worker retVal = null;
            for (Worker st : zaposleni) {
                if (st.getKorisnickoIme().equals(korisnickoIme)) {
                    retVal = st;
                    break;
                }
            }
        return retVal;
    }

	public void edit(String ime, String prezime, String pol, String adresa, String korisnickoIme,
			String lozinka, LocalDate datumRodjenja, int telefon, int strucnaSprema, int radniStaz) {
		Worker s = this.FindWorkerByUsername(korisnickoIme);
		s.setIme(ime);
		s.setPrezime(prezime);
		s.setPol(pol);
		s.setAdresa(adresa);
		s.setKorisnickoIme(korisnickoIme);
		s.setLozinka(lozinka);
		s.setDatumRodjenja(datumRodjenja);
		s.setTelefon(telefon);
		s.setStrucnaSprema(strucnaSprema);
		s.setRadniStaz(radniStaz);
		s.setSalary();
		this.saveData();
	}


	public void remove(String ime, String prezime) {
		Worker w = null;
		for (Worker worker : zaposleni) {
			if (worker.getIme().equals(ime) && worker.getPrezime().equals(prezime)) {
				w = worker;
				break;
			}
		}
		if (w!= null) {
			this.zaposleni.remove(w);
			this.saveData();
			System.out.println("Zaposleni " + ime +" "+ prezime + " je uklonjen!");
		}
		else {
			System.out.println("Zaposleni " + ime +" " + prezime + " nije pronađen!");
		}

	}



	public boolean saveData() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(this.zaposleniFile, false));
			for (Worker w : zaposleni) {
				pw.println(w.toFileString());
			}
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public boolean loadData() {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (BufferedReader br = new BufferedReader(new FileReader(this.zaposleniFile))) {
            String linija;
            while ((linija = br.readLine()) != null) {
                String[] tokeni = linija.split(",");
                Worker worker = new Worker(
                    tokeni[0], // ime
                    tokeni[1], // prezime
                    tokeni[2], // pol
                    tokeni[3], // adresa
                    tokeni[4], // korisnickoIme
                    tokeni[5], // lozinka
                    LocalDate.parse(tokeni[6], formatter), // datumRodjenja
                    Integer.parseInt(tokeni[7]), // telefon
                    Integer.parseInt(tokeni[8]), // strucnaSprema
                    Integer.parseInt(tokeni[9]), // radniStaz
                    Role.valueOf(tokeni[13]) // role
                );
                worker.setSalary();
                this.zaposleni.add(worker);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}



		public Worker verifyLoginInfo(String korisnickoIme, String lozinka) {
		loadData();
		for (Worker user : zaposleni) {
			if (user.getKorisnickoIme().equals(korisnickoIme) && user.getLozinka().equals(lozinka)) {
                return user;
            }
        }
        return null;
    }


}

