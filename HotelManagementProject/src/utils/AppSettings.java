package utils;
//služi za čuvanje naziva fajlova
public class AppSettings {
    private String dodatneUslugeFilename, assignmentsFilename,  gostiFilename, rezervacijeFilename, sobeFilename, zaposleniFilename, roomTypeFilename, priceListFilename, incomeFilename, expensesFilename;

    public AppSettings(String dodatneUslugeFilename, String gostiFilename, String rezervacijeFilename, String sobeFilename, String zaposleniFilename, String roomTypeFilename, String priceListFilename, String incomeFilename, String expensesFilename, String assignmentsFilename) {
        this.dodatneUslugeFilename = dodatneUslugeFilename;
        this.gostiFilename = gostiFilename;
        this.rezervacijeFilename = rezervacijeFilename;
        this.sobeFilename = sobeFilename;
        this.zaposleniFilename = zaposleniFilename;
        this.roomTypeFilename = roomTypeFilename;
        this.priceListFilename = priceListFilename;
        this.incomeFilename = incomeFilename;
        this.expensesFilename = expensesFilename;
        this.assignmentsFilename = assignmentsFilename;
    }

  public String getAssignmentsFilename() {
    return assignmentsFilename;
  }

  public String getIncomeFilename() {
    return incomeFilename;
  }

  public String getExpensesFilename() {
	      return expensesFilename;
  }

    public String getDodatneUslugeFilename() {
        return dodatneUslugeFilename;
    }

    public String getGostiFilename() {
        return gostiFilename;
    }

    public String getRezervacijeFilename() {
        return rezervacijeFilename;
    }

    public String getSobeFilename() {
        return sobeFilename;
    }

    public String getZaposleniFilename() {
        return zaposleniFilename;
    }

  public String getRoomTypeFilename() {
    return roomTypeFilename;
  }

  public String getPriceListFilename() {
    return priceListFilename;
  }

}
