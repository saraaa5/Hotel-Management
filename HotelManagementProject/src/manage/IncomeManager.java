package manage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.Income;
import entity.Worker;

public class IncomeManager {

	private List<Income> incomes;
	private String incomeFilename;
	public IncomeManager(String incomeFilename) {
    // TODO Auto-generated constructor stub
	      this.incomeFilename = incomeFilename;
	      this.incomes = new ArrayList<Income>();
  }


	public void addIncome(Income income) {
		System.out.println("Adding income");
        incomes.add(income);
        saveData();
	}

  public List<Income> getIncomes() {
    return incomes;
  }

  public void loadData() {
    try {BufferedReader reader = new BufferedReader(new FileReader(incomeFilename));
    	//
    	String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        Income income = new Income(Double.parseDouble(parts[0]), Integer.parseInt(parts[2]), LocalDate.parse(parts[1]), parts[3]);
        incomes.add(income);

      }

    } catch (IOException e){// TODO Auto-generated catch block
    	e.printStackTrace();}

  }

  public boolean saveData() {
	  PrintWriter pw = null;
	try {
		pw = new PrintWriter(new FileWriter(this.incomeFilename, false));
		for (Income i : incomes) {
			pw.println(i.toString());
		}
		pw.close();
	} catch (IOException e) {
		return false;
	}
	return true;}



  public List<Income> getIncomesByDate(LocalDate start, LocalDate end) {
      List<Income> result = new ArrayList<Income>();
      for (Income income : incomes) {
          if ((start.isBefore(income.getDate())||start.isEqual(income.getDate())) && (end.isAfter(income.getDate()) || end.isEqual(income.getDate()))) {
              result.add(income);
          }
      }
      return result;
  }




}
