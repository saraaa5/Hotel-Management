package tests;

import entity.*;
import manage.AdministratorManager;
import manage.AssignmentManager;
import manage.RoomManager;
import manage.RoomTypeManager;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class AssignmentManagerTest {

    private AssignmentManager manager;
    private AdministratorManager workerManager;
    private RoomManager roomManager;
    private RoomTypeManager roomTypeManager;

    @Before
    public void setUp() {
        workerManager = new AdministratorManager("test_zaposleni.txt");
        roomTypeManager = new RoomTypeManager("test_tip_soba.txt");
        roomManager = new RoomManager("test_sobe.txt", roomTypeManager);
        manager = new AssignmentManager("test_zadaci.txt", workerManager, roomManager);
    }

    @Test
    public void testGetNumberOfAssignments() {
        // Priprema test podataka
        Worker worker = new Worker("Marko", "Marković", "Muški", "Adresa 1", "marko_markovic", "lozinka",
                LocalDate.of(1990, 1, 1), 123456789, 5, 10, Role.MAID);
        CleanupAssignment assignment1 = new CleanupAssignment("marko_markovic", 101, "uuid1", LocalDate.now());
        CleanupAssignment assignment2 = new CleanupAssignment("marko_markovic", 102, "uuid2", LocalDate.now());

        // Dodavanje zadataka
        manager.addAssignment(assignment1);
        manager.addAssignment(assignment2);

        // Testiranje metode getNumberOfAssignments
        int count = manager.getNumberOfAssignments(LocalDate.now(), worker);
        assertEquals(2, count);
    }

    @Test
    public void testGetLastMonthCount() {
        // Priprema test podataka
        CleanupAssignment assignment1 = new CleanupAssignment("marko_markovic", 101, "uuid1", LocalDate.now().minusDays(10));
        CleanupAssignment assignment2 = new CleanupAssignment("marko_markovic", 102, "uuid2", LocalDate.now().minusMonths(1).plusDays(5));

        // Dodavanje zadataka
        manager.addAssignment(assignment1);
        manager.addAssignment(assignment2);

        // Testiranje metode getLastMonthCount
        int count = manager.getLastMonthCount("marko_markovic");
        assertEquals(2, count);
    }

    @Test
    public void testAssignHousekeeper() {
        // Priprema test podataka
        RoomType tipSobe = new RoomType("1", "Standardna jednokrevetna soba");
        Room room = new Room(101, tipSobe, true, true, true, false);
        Reservation reservation = new Reservation(
    LocalDate.now(),
    LocalDate.now().plusDays(1),
    Reservation.RezervacijaStatus.NA_CEKANJU,
    "username_gosta",
    room
);

        // Dodavanje sobe i radnika u menadžere
        roomManager.addRoom(room);
        workerManager.addWorker("Marko", "Marković", "Muški", "Adresa 1", "marko_markovic", "lozinka",
                LocalDate.of(1990, 1, 1), 123456789, 5, 10, Role.MAID);

        // Testiranje metode assignHousekeeper
        manager.assignHousekeeper(room, reservation);

        // Provera da li je zadatak dodat
        List<CleanupAssignment> assignments = manager.getAssignments();
        assertEquals(1, assignments.size());
        assertEquals("marko_markovic", assignments.get(0).getMaidId());
        assertEquals(101, assignments.get(0).getRoomId());
    }

    @Test
    public void testCompleteAssignment() {
        // Priprema test podataka
        CleanupAssignment assignment = new CleanupAssignment("marko_markovic", 101, "uuid1", LocalDate.now());
        manager.addAssignment(assignment);

        // Testiranje metode completeAssignment
        manager.completeAssignment(assignment);

        // Provera da li je zadatak završen
        assertTrue(assignment.getIsCompleted());

        // Provera da li je status sobe ažuriran
        List<Room> rooms = roomManager.getRooms();
        for (Room room : rooms) {
            if (room.getBrojSobe() == assignment.getRoomId()) {
                assertEquals(Room.SobaStatus.SLOBODNA, room.getStatusSobe());
            }
        }
    }

    @Test
    public void testGetAssignmentsForHousekeeper() {
        // Priprema test podataka
        CleanupAssignment assignment1 = new CleanupAssignment("marko_markovic", 101, "uuid1", LocalDate.now());
        CleanupAssignment assignment2 = new CleanupAssignment("marko_markovic", 102, "uuid2", LocalDate.now().plusDays(1));

        // Dodavanje zadataka
        manager.addAssignment(assignment1);
        manager.addAssignment(assignment2);

        // Testiranje metode getAssignmentsForHousekeeper
        List<CleanupAssignment> assignments = manager.getAssignmentsForHousekeeper("marko_markovic", LocalDate.now());
        assertEquals(1, assignments.size());
        assertEquals("uuid1", assignments.get(0).getReservationId());
    }
}
