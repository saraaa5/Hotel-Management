package manage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import entity.AdditionalService;

public class AdditionalServiceManager {
	private String dodatneUslugeeFile;
	private List<AdditionalService>dodatneUsluge;



	public AdditionalServiceManager(String dodatneUslugeeFile) {
		super();
		this.dodatneUslugeeFile = dodatneUslugeeFile;
		this.dodatneUsluge = new ArrayList<AdditionalService>();
	}


	public List<AdditionalService> getServices() {
		return dodatneUsluge;
	}

	public void addAdditionalServices(AdditionalService dodatnaUsluga) {
	    dodatneUsluge.add(dodatnaUsluga);
		this.saveDataServices();
	}

	public void removeAdditionalService(String dodatnaUsluga) {
		AdditionalService a = null;
		for (AdditionalService service : dodatneUsluge) {
			if (service.getDodatnaUsluga().equals(dodatnaUsluga)) {
				a = service;
				break;
			}
		}
		if (a!= null) {
			this.dodatneUsluge.remove(a);
			this.saveDataServices();
			System.out.println("Dodatna usluga " + dodatnaUsluga + " je uklonjena!");
		}
		else {
			System.out.println("Dodatna usluga " + dodatnaUsluga + " nije pronađena!");
		}
	}
	public boolean saveDataServices() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(this.dodatneUslugeeFile, false));
			for (AdditionalService a : dodatneUsluge) {
				pw.println(a.toFileString());
			}
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}


  public AdditionalService findAdditionalService(String dodatnaUsluga) {
    for (AdditionalService service : dodatneUsluge) {
      if (service.getDodatnaUsluga().equals(dodatnaUsluga)) {
        return service;
      }
    }
    return null;
  }

	public boolean loadData() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.dodatneUslugeeFile));
			String linija = null;
			while ((linija = br.readLine()) != null) {
				String[] tokeni = linija.split(",");
				this.dodatneUsluge.add(new AdditionalService(tokeni[0]));
			}
			br.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
