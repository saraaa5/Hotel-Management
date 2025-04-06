package tests;

import entity.AdditionalService;
import manage.AdditionalServiceManager;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

public class AdditionalServiceManagerTest {

    private AdditionalServiceManager manager;
    private final String testFilePath = "./test_dodatne_usluge.csv";

    @Before
    public void setUp() throws IOException {
        Files.write(new File(testFilePath).toPath(), "Breakfast\nLunch\nDinner\n".getBytes());
        manager = new AdditionalServiceManager(testFilePath);
        manager.loadData();
    }

    @Test
    public void testLoadData() {
        List<AdditionalService> services = manager.getServices();
        assertEquals(3, services.size());
        assertEquals("Breakfast", services.get(0).getDodatnaUsluga());
        assertEquals("Lunch", services.get(1).getDodatnaUsluga());
        assertEquals("Dinner", services.get(2).getDodatnaUsluga());
    }

    @Test
    public void testAddAdditionalServices() {
        manager.addAdditionalServices(new AdditionalService("Spa"));
        List<AdditionalService> services = manager.getServices();
        assertEquals(4, services.size());
        assertEquals("Spa", services.get(3).getDodatnaUsluga());
    }

    @Test
    public void testRemoveAdditionalService() {
        manager.removeAdditionalService("Lunch");
        List<AdditionalService> services = manager.getServices();
        assertEquals(2, services.size());
        assertNull(manager.findAdditionalService("Lunch"));
    }

    @Test
    public void testFindAdditionalService() {
        AdditionalService service = manager.findAdditionalService("Dinner");
        assertNotNull(service);
        assertEquals("Dinner", service.getDodatnaUsluga());
    }

    @Test
    public void testSaveDataServices() throws IOException {
        manager.addAdditionalServices(new AdditionalService("Gym"));
        manager.saveDataServices();

        List<String> lines = Files.readAllLines(new File(testFilePath).toPath());
        assertEquals(4, lines.size());
        assertEquals("Gym", lines.get(3));
    }
}
