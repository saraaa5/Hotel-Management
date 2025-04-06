package tests;

import manage.AdministratorManager;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

import entity.Role;
import entity.Worker;

public class AdministratorManagerTest {

    private AdministratorManager manager;
    private final String testFile = "test_workers.txt";

    @Before
    public void setUp() {
        manager = new AdministratorManager(testFile);
    }

    @Test
    public void testAddWorker() {
        manager.addWorker("Marko", "Delic", "Muski", "Address 123", "admin_marko", "marko1234",
                LocalDate.of(1990, 1, 1), 123456789, 5, 10, Role.ADMIN);

        // Check if worker was added
        assertEquals(1, manager.getWorkers().size());
        assertEquals("Marko", manager.getWorkers().get(0).getIme());
        assertEquals("Delic", manager.getWorkers().get(0).getPrezime());
        assertEquals("admin_marko", manager.getWorkers().get(0).getKorisnickoIme());
    }

    @Test
    public void testFindWorkerByUsername() {
        manager.addWorker("Marko", "Delic", "Muski", "Address 123", "admin_marko", "marko1234",
                LocalDate.of(1990, 1, 1), 123456789, 5, 10, Role.ADMIN);

        Worker foundWorker = manager.FindWorkerByUsername("admin_marko");
        assertNotNull(foundWorker);
        assertEquals("Marko", foundWorker.getIme());
        assertEquals("Delic", foundWorker.getPrezime());
    }

    @Test
    public void testEditWorker() {
        manager.addWorker("Marko", "Delic", "Muski", "Address 123", "admin_marko", "marko1234",
                LocalDate.of(1990, 1, 1), 123456789, 5, 10, Role.ADMIN);

        manager.edit("Jane", "Smith", "Female", "New Address 456", "admin_marko", "newpassword",
                LocalDate.of(1995, 5, 5), 987654321, 10, 15);

        Worker editedWorker = manager.FindWorkerByUsername("admin_marko");
        assertEquals("Jane", editedWorker.getIme());
        assertEquals("Smith", editedWorker.getPrezime());
        assertEquals("Female", editedWorker.getPol());
        assertEquals("New Address 456", editedWorker.getAdresa());
        assertEquals("newpassword", editedWorker.getLozinka());
        assertEquals(LocalDate.of(1995, 5, 5), editedWorker.getDatumRodjenja());
        assertEquals(987654321, editedWorker.getTelefon());
        assertEquals(10, editedWorker.getStrucnaSprema());
        assertEquals(15, editedWorker.getRadniStaz());
    }

    @Test
    public void testRemoveWorker() {
        manager.addWorker("Marko", "Delic", "Muski", "Address 123", "admin_marko", "marko1234",
                LocalDate.of(1990, 1, 1), 123456789, 5, 10, Role.ADMIN);

        manager.remove("Marko", "Delic");

        assertEquals(0, manager.getWorkers().size());
    }
}
