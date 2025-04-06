package tests;

import entity.Income;
import manage.IncomeManager;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IncomeManagerTest {

    private IncomeManager incomeManager;

    @Before
    public void setUp() {
        incomeManager = new IncomeManager("test_income.txt");
    }

    @Test
    public void testAddIncome() {
        Income income = new Income(1000.0, 2, LocalDate.now(), "Description");

        incomeManager.addIncome(income);

        List<Income> incomes = incomeManager.getIncomes();
        assertEquals(1, incomes.size());
        assertEquals(income.getAmount(), incomes.get(0).getAmount(), 0.001);
        assertEquals(income.getDate(), incomes.get(0).getDate());
    }

    @Test
    public void testGetIncomesByDate() {
        Income income1 = new Income(1000.0,1, LocalDate.now().minusDays(5), "Description1");
        Income income2 = new Income(1500.0,2,  LocalDate.now().minusDays(2), "Description2");

        incomeManager.addIncome(income1);
        incomeManager.addIncome(income2);

        LocalDate startDate = LocalDate.now().minusDays(3);
        LocalDate endDate = LocalDate.now().minusDays(1);
        List<Income> incomes = incomeManager.getIncomesByDate(startDate, endDate);

        assertEquals(1, incomes.size());
        assertEquals(income2.getAmount(), incomes.get(0).getAmount(), 0.001);
        assertEquals(income2.getDate(), incomes.get(0).getDate());
    }
}
