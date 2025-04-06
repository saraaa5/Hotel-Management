package tests;

import entity.AdditionalService;
import entity.PriceList;
import entity.RoomType;
import manage.AdditionalServiceManager;
import manage.PriceListManager;
import manage.RoomTypeManager;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class PriceListManagerTest {

    private PriceListManager priceListManager;
    private RoomTypeManager roomTypeManager;
    private AdditionalServiceManager additionalServiceManager;

    @Before
    public void setUp() {
        roomTypeManager = new RoomTypeManager("test_room_types.txt");
        additionalServiceManager = new AdditionalServiceManager("test_additional_services.txt");
        priceListManager = new PriceListManager("test_price_lists.txt", roomTypeManager, additionalServiceManager);
    }

    @Test
    public void testAddPriceList() {
        RoomType roomType = new RoomType("101", "Standard Room");
        AdditionalService additionalService = new AdditionalService("Breakfast");
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 7, 10);
        PriceList priceList = new PriceList(startDate, endDate);

        priceList.addRoomPrice(roomType, 100.0);
        priceList.addAdditionalServicePrice(additionalService, 20.0);
        priceListManager.addPriceList(priceList);

        assertTrue(priceListManager.getPriceLists().contains(priceList));
    }

    @Test
    public void testFindPriceListByDateRange() {
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 7, 10);
        PriceList priceList = new PriceList(startDate, endDate);
        priceListManager.addPriceList(priceList);

        PriceList foundPriceList = priceListManager.findPriceListByDateRange(startDate, endDate);

        assertNotNull(foundPriceList);
        assertEquals(startDate, foundPriceList.getDatumDolaska());
        assertEquals(endDate, foundPriceList.getDatumOdlaska());
    }

    @Test
    public void testRemovePriceList() {
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 7, 10);
        PriceList priceList = new PriceList(startDate, endDate);
        priceListManager.addPriceList(priceList);
        priceListManager.removePriceList(startDate, endDate);

        assertNull(priceListManager.findPriceListByDateRange(startDate, endDate));
    }

    @Test
    public void testCheckDateRange() {
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 7, 10);
        PriceList priceList = new PriceList(startDate, endDate);
        priceListManager.addPriceList(priceList);

        boolean isDateRangeOccupied = priceListManager.checkDateRange(startDate, endDate);

        assertTrue(isDateRangeOccupied);
    }
}
