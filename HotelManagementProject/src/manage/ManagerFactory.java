package manage;

import utils.AppSettings;


public class ManagerFactory {
    private AppSettings appSettings;
    private AdditionalServiceManager additionalServiceManager;
    private AdministratorManager administratorManager;
    private ReceptionistManager receptionistManager;
    private ReservationManager reservationManager;
    private RoomManager roomManager;
    private RoomTypeManager roomTypeManager;
    private PriceListManager priceListManager;
    private IncomeManager incomeManager;
    private ExpenseManager expenseManager;
    private AssignmentManager assignmentManager;

    public ManagerFactory(AppSettings appSettings) {
        this.appSettings = appSettings;
        this.additionalServiceManager = new AdditionalServiceManager(this.appSettings.getDodatneUslugeFilename());
        this.administratorManager = new AdministratorManager(this.appSettings.getZaposleniFilename());
        this.receptionistManager = new ReceptionistManager(this.appSettings.getGostiFilename());
        this.roomTypeManager = new RoomTypeManager(this.appSettings.getRoomTypeFilename());
        this.roomManager = new RoomManager(this.appSettings.getSobeFilename(), this.roomTypeManager);
        this.priceListManager = new PriceListManager(this.appSettings.getPriceListFilename(), this.roomTypeManager, this.additionalServiceManager);
        this.reservationManager = new ReservationManager(this.appSettings.getRezervacijeFilename(), this.roomManager, this.additionalServiceManager, this.priceListManager);
        this.incomeManager = new IncomeManager(this.appSettings.getIncomeFilename());
        this.expenseManager = new ExpenseManager(this.appSettings.getExpensesFilename(), this.administratorManager);
        this.assignmentManager = new AssignmentManager(this.appSettings.getAssignmentsFilename(), this.administratorManager, this.roomManager);
    }

  public AssignmentManager getAssignmentManager() {
    return assignmentManager;
  }

    public AdditionalServiceManager getAdditionalServiceManager() {
        return additionalServiceManager;
    }

    public AdministratorManager getAdministratorManager() {
        return administratorManager;
    }

    public ReceptionistManager getReceptionistManager() {
        return receptionistManager;
    }

    public ReservationManager getReservationManager() {
        return reservationManager;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

  public RoomTypeManager getRoomTypeManager() {
    return roomTypeManager;
  }

  public PriceListManager getPriceListManager() {
	      return priceListManager;
  }

  public IncomeManager getIncomeManager() {
	      return incomeManager;
  }

  public ExpenseManager getExpenseManager() {
          return expenseManager;
          }

    public void loadData() {


        this.administratorManager.loadData();
        this.receptionistManager.loadData();
        this.additionalServiceManager.loadData();
        //loading room types
        this.roomTypeManager.loadData();
        //loading rooms
        this.roomManager.loadData();
        //loading price list
        this.priceListManager.loadData();
        //load reservations
        this.reservationManager.loadData();
        //load income
        this.incomeManager.loadData();
        //load expenses
        this.expenseManager.loadData();
        this.expenseManager.checkPayday();
        //load assignments
        this.assignmentManager.loadData();
        //cleanup non active reservations
        this.reservationManager.cleanupReservations();
    }
}
