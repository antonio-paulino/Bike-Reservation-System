package isel.sisinf.model.Reservation;

import isel.sisinf.model.Bike.Bike;
import isel.sisinf.model.Bike.ClassicBike;
import isel.sisinf.model.GPS.GPS;
import isel.sisinf.model.Person.Client;
import isel.sisinf.model.Person.Person;
import isel.sisinf.model.Store.Store;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Timestamp;
import java.time.Instant;

public class ReservationTest {

    @Test
    public void testReservationConstructorWithValidData() {
        ClassicBike bike = new ClassicBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", new GPS(0, 38.736946, -9.138611, 100), 2);
        Store store = new Store();
        store.setCode(1);
        Client client = new Client(1, "John Doe", "123 Street", "john@example.com", "123456789", "ID123", "US");
        Timestamp startDate = Timestamp.from(Instant.now().plusSeconds(3600));
        Timestamp endDate = Timestamp.from(Instant.now().plusSeconds(7200));
        double price = 100.0;

        Reservation reservation = new Reservation(1, store.getCode(), startDate, endDate, price, bike, store, client);

        assertEquals(1, reservation.getId().getReservation_no());
        assertEquals(store.getCode(), reservation.getId().getStore());
        assertEquals(startDate, reservation.getStart_date());
        assertEquals(endDate, reservation.getEnd_date());
        assertEquals(price, reservation.getPrice());
        assertEquals(bike, reservation.getBike());
        assertEquals(store, reservation.getStoreEntity());
        assertEquals(client, reservation.getClient());
        assertTrue(reservation.isActive());
    }

    @Test
    public void testReservationConstructorWithInvalidStartDate() {
        Bike bike = new ClassicBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", new GPS(0, 38.736946, -9.138611, 100), 2);
        Store store = new Store();
        store.setCode(1);
        Person client = new Client(1, "John Doe", "123 Street", "test@test.com", "123456789", "ID123", "US");
        Timestamp startDate = Timestamp.from(Instant.now().minusSeconds(3600)); // Start date in the past
        Timestamp endDate = Timestamp.from(Instant.now().plusSeconds(7200));
        double price = 100.0;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Reservation(1, store.getCode(), startDate, endDate, price, bike, store, client);
        });
        assertEquals("Start date must be after current date", exception.getMessage());
    }

    @Test
    public void testSetStartDateWithInvalidDate() {
        Reservation reservation = new Reservation();
        Timestamp invalidStartDate = Timestamp.from(Instant.now().minusSeconds(3600)); // Start date in the past

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservation.setStart_date(invalidStartDate);
        });
        assertEquals("Start date must be after current date", exception.getMessage());
    }

    @Test
    public void testSetEndDateWithInvalidDate() {
        Reservation reservation = new Reservation();
        Timestamp startDate = Timestamp.from(Instant.now().plusSeconds(3600));
        reservation.setStart_date(startDate);
        Timestamp invalidEndDate = Timestamp.from(Instant.now().plusSeconds(1800)); // End date before start date

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservation.setEnd_date(invalidEndDate);
        });
        assertEquals("End date must be after start date", exception.getMessage());
    }

    @Test
    public void testSetGetPrice() {
        Reservation reservation = new Reservation();
        double price = 150.0;
        reservation.setPrice(price);
        assertEquals(price, reservation.getPrice());
    }

    @Test
    public void testSetGetBike() {
        Reservation reservation = new Reservation();
        Bike bike = new ClassicBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", new GPS(0, 38.736946, -9.138611, 100), 2);
        reservation.setBike(bike);
        assertEquals(bike, reservation.getBike());
    }

    @Test
    public void testSetGetStoreEntity() {
        Reservation reservation = new Reservation();
        Store store = new Store();
        reservation.setStoreEntity(store);
        assertEquals(store, reservation.getStoreEntity());
    }

    @Test
    public void testSetGetClient() {
        Reservation reservation = new Reservation();
        Person client = new Client(1, "John Doe", "123 Street", "test@test.com", "123456789", "ID123", "US");
        reservation.setClient(client);
        assertEquals(client, reservation.getClient());
    }

    @Test
    public void testSetGetActive() {
        Reservation reservation = new Reservation();
        reservation.setActive(false);
        assertFalse(reservation.isActive());
    }

}
