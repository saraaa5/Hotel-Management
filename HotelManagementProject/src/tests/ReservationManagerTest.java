package tests;

import entity.*;
import entity.Reservation.RezervacijaStatus;
import entity.RoomType;
import manage.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ReservationManagerTest {

    private ReservationManager reservationManager;
    private RoomManager roomManager;
    private RoomTypeManager roomTypeManager;
    private AdditionalServiceManager additionalServiceManager;
    private PriceListManager priceListManager;
    private String testFile = "test_reservations.txt";
    private String sobeFile = "test_sobe.txt";
    private String dodatneUslugice = "test_dodatne.txt";
    private String priceList = "test_cenovnik.txt";

    @Before
    public void setUp() {
        roomManager = new RoomManager(sobeFile,roomTypeManager);
        additionalServiceManager = new AdditionalServiceManager(dodatneUslugice);
        priceListManager = new PriceListManager(priceList, roomTypeManager, additionalServiceManager);


        reservationManager = new ReservationManager(testFile, roomManager, additionalServiceManager, priceListManager);
        prepareTestData();

    }
    private void prepareTestData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        RoomType tipSobe = new RoomType("1", "Standardna jednokrevetna soba");
        Room room = new Room(101, tipSobe, true, true, true, false);

        additionalServiceManager.addAdditionalServices(new AdditionalService("Breakfast"));
        additionalServiceManager.addAdditionalServices(new AdditionalService("Parking"));

        LocalDate arrivalDate = LocalDate.now().plusDays(1);
        LocalDate departureDate = LocalDate.now().plusDays(5);

        List<AdditionalService> additionalServices = new ArrayList<>();
        additionalServices.add(additionalServiceManager.findAdditionalService("Breakfast"));
        additionalServices.add(additionalServiceManager.findAdditionalService("Parking"));

        Reservation reservation = new Reservation(arrivalDate, departureDate, RezervacijaStatus.NA_CEKANJU, "test_user", room);
        reservation.setDodatneUsluge(additionalServices);

        reservationManager.addReservation(reservation);
    }




    @Test
    public void testAddReservation() {
        RoomType tipSobe = new RoomType("1", "Standardna jednokrevetna soba");
        Room room = new Room(101, tipSobe, true, true, true, false);
        LocalDate arrivalDate = LocalDate.now().plusDays(2);
        LocalDate departureDate = LocalDate.now().plusDays(7);

        List<AdditionalService> additionalServices = new ArrayList<>();
        additionalServices.add(additionalServiceManager.findAdditionalService("Breakfast"));

        Reservation newReservation = new Reservation(arrivalDate, departureDate, RezervacijaStatus.NA_CEKANJU, "test_user", room);
        newReservation.setDodatneUsluge(additionalServices);

        Double price = reservationManager.addReservation(newReservation);

        assertNotNull(price);
        assertTrue(price >= 0.0);
    }

    @Test
    public void testCalculatePrice() {
        RoomType tipSobe = new RoomType("1", "Standardna jednokrevetna soba");
        Room room = new Room(101, tipSobe, true, true, true, false);

        LocalDate arrivalDate = LocalDate.now().plusDays(3);
        LocalDate departureDate = LocalDate.now().plusDays(8);

        List<AdditionalService> additionalServices = new ArrayList<>();
        additionalServices.add(additionalServiceManager.findAdditionalService("Parking"));

        Reservation newReservation = new Reservation(arrivalDate, departureDate, RezervacijaStatus.NA_CEKANJU, "test_user", room);
        newReservation.setDodatneUsluge(additionalServices);

        Double price = reservationManager.calculatePrice(newReservation);

        assertNotNull(price);
        assertTrue(price >= 0.0);
    }

    @Test
    public void testSetFinished() {
        Reservation reservation = reservationManager.getReservations().get(0);
        reservationManager.setFinished(reservation);

        assertEquals(RezervacijaStatus.ZAVRSENA, reservation.getStatusRezervacije());
    }

    @Test
    public void testCheckAvailability() {
        // Testiranje provere dostupnosti sobe
        RoomType roomType = new RoomType("Single Room", "single_room");

        LocalDate arrivalDate = LocalDate.now().plusDays(4);
        LocalDate departureDate = LocalDate.now().plusDays(9);

        Room availableRoom = reservationManager.checkAvailability(arrivalDate, departureDate, roomType, true, true, true, false, null);

        if (availableRoom == null) {
            System.out.println("nema soba");
        }
        else {
            assertEquals(Room.SobaStatus.SLOBODNA, availableRoom.getStatusSobe());

        }
    }


}
