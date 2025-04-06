package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import entity.RoomType;
import manage.RoomTypeManager;
import org.junit.Before;
import org.junit.Test;

public class RoomTypeManagerTest {

    private RoomTypeManager roomTypeManager;

    @Before
    public void setUp() {
        roomTypeManager = new RoomTypeManager("testRoomTypes.csv");

        RoomType roomType1 = new RoomType("1", "Single");
        RoomType roomType2 = new RoomType("2", "Double");

        roomTypeManager.addRoomType(roomType1);
        roomTypeManager.addRoomType(roomType2);
    }

    @Test
    public void testFindRoomTypeById_nonExistingId() {
        RoomType foundRoomType = roomTypeManager.findRoomTypeById("3");
        assertNull(foundRoomType);
    }


    @Test
    public void testRemoveRoomType() {
        RoomType roomTypeToRemove = roomTypeManager.findRoomTypeById("1");

        roomTypeManager.removeRoomType(roomTypeToRemove);

        assertNull(roomTypeManager.findRoomTypeById("1"));
    }

    @Test
    public void testAddRoomType() {
        String numberOfBeds = "3";
        String typeName = "Suite";
        RoomType newRoomType = new RoomType(numberOfBeds, typeName);

        roomTypeManager.addRoomType(newRoomType);

        RoomType addedRoomType = roomTypeManager.findRoomTypeById(newRoomType.getId());
        if (addedRoomType != null) {
            assertEquals(numberOfBeds, addedRoomType.getNumberOfBeds());
            assertEquals(typeName, addedRoomType.getTypeName());
        }
    }
}
