package tests;

import manage.AdministratorManager;
import manage.ExpenseManager;
import entity.Expense;
import entity.Worker;
import org.junit.Before;
import org.junit.Test;
import entity.Role;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExpenseManagerTest {

    private ExpenseManager expenseManager;
    private AdministratorManager workerManager;

    @Before
    public void setUp() {
        workerManager = new AdministratorManager("test_zaposleni.txt");
        expenseManager = new ExpenseManager("test_troskovi.txt", workerManager);
    }

    @Test
    public void testAddExpense() {
        // Priprema test podataka
        Expense expense = new Expense(LocalDate.now(), 500.0);

        // Dodavanje troška
        expenseManager.addExpense(expense);

        // Provera da li je trošak uspešno dodat
        List<Expense> expenses = expenseManager.getExpenses();
        assertEquals(1, expenses.size());
        assertEquals(expense, expenses.get(0));
    }

    @Test
    public void testGetExpensesByDate() {
        Expense expense1 = new Expense(LocalDate.now().minusDays(2), 300.0);
        Expense expense2 = new Expense(LocalDate.now().minusDays(1), 400.0);
        Expense expense3 = new Expense(LocalDate.now(), 500.0);

        expenseManager.addExpense(expense1);
        expenseManager.addExpense(expense2);
        expenseManager.addExpense(expense3);

        LocalDate startDate = LocalDate.now().minusDays(3);
        LocalDate endDate = LocalDate.now().minusDays(1);
        List<Expense> expenses = expenseManager.getExpensesByDate(startDate, endDate);

        assertEquals(1, expenses.size());
        assertEquals(expense1, expenses.get(0));
    }

    @Test
    public void testCheckPayday() {
        workerManager.addWorker("Ana", "Anić", "Ženski", "Adresa 1", "ana_anic", "lozinka",
                LocalDate.of(1995, 3, 15), 123456789, 5, 10, Role.MAID);
        workerManager.addWorker("Marko", "Marković", "Muški", "Adresa 2", "marko_markovic", "lozinka",
                LocalDate.of(1990, 5, 20), 987654321, 6, 11, Role.RECEPTIONIST);

        expenseManager.checkPayday();

        List<Expense> expenses = expenseManager.getExpenses();
        boolean foundToday = false;
        for (Expense expense : expenses) {
            if (expense.getDate().equals(LocalDate.now())) {
                foundToday = true;
                break;
            }
        }
        assertEquals(true, foundToday);
    }
}
