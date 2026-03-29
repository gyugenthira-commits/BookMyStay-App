import java.util.*;

class RoomInventory {
    private Map<String, Integer> roomAvailability = new HashMap<>();

    public RoomInventory() {
        roomAvailability.put("Single", 1);
        roomAvailability.put("Double", 1);
        roomAvailability.put("Suite", 1);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}

class CancellationService {

    private Stack<String> releasedRoomIds;
    private Map<String, String> reservationRoomTypeMap;

    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    public void cancelBooking(String reservationId, RoomInventory inventory) {

        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Invalid cancellation: Reservation not found");
            return;
        }

        String roomType = reservationRoomTypeMap.get(reservationId);

        String releasedRoomId = reservationId + "-R";
        releasedRoomIds.push(releasedRoomId);

        Map<String, Integer> availability = inventory.getRoomAvailability();
        inventory.updateAvailability(roomType, availability.get(roomType) + 1);

        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Booking cancelled: " + reservationId + " | Room released: " + releasedRoomId);
    }

    public void showRollbackHistory() {
        System.out.println("Rollback History (LIFO):");
        while (!releasedRoomIds.isEmpty()) {
            System.out.println(releasedRoomIds.pop());
        }
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService();

        service.registerBooking("R101", "Single");
        service.registerBooking("R102", "Double");

        service.cancelBooking("R101", inventory);
        service.cancelBooking("R102", inventory);

        service.showRollbackHistory();
    }
}