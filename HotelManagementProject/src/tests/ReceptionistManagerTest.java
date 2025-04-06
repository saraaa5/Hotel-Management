package tests;

import manage.ReceptionistManager;
import entity.Guest;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;

public class ReceptionistManagerTest {

    private ReceptionistManager receptionistManager;
    private String testFile = "test_guests.txt";

    @Before
    public void setUp() {
        receptionistManager = new ReceptionistManager(testFile);
        prepareTestData();
    }

    private void prepareTestData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Guest guest1 = new Guest("Marko", "Markovic", "M", "Adresa 1", "marko", "lozinka123", LocalDate.parse("1990-01-01", formatter), 123456789);
        Guest guest2 = new Guest("Jovan", "Jovanovic", "M", "Adresa 2", "jovan", "testtest", LocalDate.parse("1985-05-05", formatter), 987654321);

        receptionistManager.addGuest(guest1);
        receptionistManager.addGuest(guest2);
    }

    @Test
    public void testFindGuestByUsername() {
        Guest foundGuest = receptionistManager.findGuestByUsername("marko");

        assertNotNull(foundGuest);
        assertEquals("Marko", foundGuest.getIme());
        assertEquals("Markovic", foundGuest.getPrezime());
    }

    @Test
    public void testFindGuestUsernameByUsername() {
        String foundUsername = receptionistManager.findGuestUsernameByUsername("jovan");

        assertNotNull(foundUsername);
        assertEquals("jovan", foundUsername);
    }

    @Test
    public void testVerifyLoginInfo() {
        Guest verifiedGuest = receptionistManager.verifyLoginInfo("marko", "lozinka123");

        assertNotNull(verifiedGuest);
        assertEquals("Marko", verifiedGuest.getIme());
    }

    @Test
    public void testFindGuestByName() {
        Guest foundGuest = receptionistManager.findGuestByName("Jovan");

        assertNotNull(foundGuest);
        assertEquals("Jovanovic", foundGuest.getPrezime());
    }

    @Test
    public void testEditGuest() {
        receptionistManager.edit("Marko", "Markovic", "M", "Nova adresa", "marko", "novaLozinka", LocalDate.now(), 987654321);

        Guest editedGuest = receptionistManager.findGuestByUsername("marko");

        assertNotNull(editedGuest);
        assertEquals("Nova adresa", editedGuest.getAdresa());
        assertEquals("novaLozinka", editedGuest.getLozinka());
    }

    @Test
    public void testRemoveGuest() {
        receptionistManager.remove("Marko", "Markovic");

        Guest removedGuest = receptionistManager.findGuestByUsername("marko");

        assertNull(removedGuest);
    }

}
