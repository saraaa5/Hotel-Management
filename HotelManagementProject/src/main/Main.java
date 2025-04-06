package main;
import manage.*;
import utils.AppSettings;
import view.MainFrame;


public class Main {

	public static void main(String[] args) {
            System.out.println("Učitavanje...Molimo sačekajte.");
		AppSettings appSettings = new AppSettings("./data/dodatneUsluge.csv", "./data/gosti.csv",
                "./data/rezervacije.csv", "./data/sobe.csv", "./data/zaposleni.csv", "./data/roomTypes.csv", "./data/cenovnik.csv", "./data/income.csv", "./data/expense.csv", "./data/assignments.csv");
		ManagerFactory controlers = new ManagerFactory(appSettings);

		controlers.loadData();

		MainFrame main = new MainFrame(controlers);
		main.toString();

        }

}
