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

import entity.Expense;
import entity.Income;
import entity.Worker;

public class ExpenseManager {

	private List<Expense> expenses;
	private String expensesFilename;
	private AdministratorManager workerManager;
	public ExpenseManager(String expensesFilename, AdministratorManager workerManager) {
    // TODO Auto-generated constructor stub
		this.workerManager = workerManager;
		this.expensesFilename = expensesFilename;
		this.expenses = new ArrayList<Expense>();
  }

  public List<Expense> getExpenses() {
    return expenses;
  }

  public void addExpense(Expense expense) {
    expenses.add(expense);
    saveData();
  }

  public void checkPayday() {
    // TODO implement
	  LocalDate now = LocalDate.now();
	  int difference = 100;
	  for (Expense expense : expenses) {
		  int diff = now.compareTo(expense.getDate());
      if (diff < difference) {
        difference = diff;
      }
    }
	  if (difference >= 25) {
		  List<Worker> workers = workerManager.getWorkers();
		  double total = 0;
		  for (Worker worker : workers) {
        total += worker.getPlata();
      }
		  addExpense(new Expense(now, total));
		  System.out.println("Payday today!");
    }
  }

  public void loadData() {
	  try {
		  BufferedReader reader = new BufferedReader(new FileReader(expensesFilename));

	  String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        double amount = Double.parseDouble(parts[0]);
        LocalDate date = LocalDate.parse(parts[1]);
        Expense expense = new Expense(date, amount);
        expenses.add(expense);
      }

	  } catch (IOException e){// TODO Auto-generated catch block
  e.printStackTrace();}


	  // TODO implement
  }
  public boolean saveData() {
	  PrintWriter pw = null;
	try {
		pw = new PrintWriter(new FileWriter(this.expensesFilename, false));
		for (Expense e : expenses) {
			pw.println(e.toString());
		}
		pw.close();
	} catch (IOException e) {
		return false;
	}
	return true;}

  public List<Expense> getExpensesByDate(LocalDate start, LocalDate end) {
    List<Expense> result = new ArrayList<Expense>();
    for (Expense expense : expenses) {
      if (start.isBefore(expense.getDate()) && end.isAfter(expense.getDate())) {
        result.add(expense);
      }
    }
    return result;
  }
}
