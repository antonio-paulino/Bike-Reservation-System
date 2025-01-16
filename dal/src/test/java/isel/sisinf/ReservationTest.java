package isel.sisinf;

import isel.sisinf.model.Bike.ClassicBike;
import isel.sisinf.model.Person.Client;
import isel.sisinf.model.Person.Manager;
import isel.sisinf.model.Reservation.IReservation;
import isel.sisinf.model.Reservation.Reservation;
import isel.sisinf.model.GPS.GPS;
import isel.sisinf.jpa.JPAContext;
import isel.sisinf.model.Store.Store;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ReservationTest {

    Timestamp date1 = Timestamp.valueOf(LocalDateTime.now().plusHours(1));
    Timestamp date2 = Timestamp.valueOf(LocalDateTime.now().plusDays(1));

    private final GPS testGPS = new GPS(0, 38.736946, -9.138611, 100);
    private final ClassicBike testClassicBike = new ClassicBike(0, 75.00,
            "TestModel", "TestBrand", 6,
            "livre", testGPS, 2);

    private final Client testClient = new Client(0, "John Doe",
            "Rua Teste", "testemail@asd.pt",
            "987654321", "987654321",
            "Portuguese");

    private final Manager testManager = new Manager(0, "John Doe",
            "Test", "testemail@isel.pt",
            "123456789", "123456789",
            "Portuguese");

    private final Store testStore = new Store(0, "testemail@isel.pt",
            "testaddress", "testlocation",
            "123456789", testManager);

    private Reservation createReservation() {
        return new Reservation(0, testStore.getCode(), date1,
                date2, 54.2,
                testClassicBike, testStore, testClient);
    }


    @Test
    public void testCreateReservation() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            context.getStoreRepository().create(testStore);
            context.getClientRepository().create(testClient);
            context.getClassicBikeRepository().create(testClassicBike);
            context.flush();
            Reservation reservation = createReservation();
            context.getReservationRepository().create(reservation);
            context.commit();
            Reservation createdReservation = (Reservation) context.getReservationRepository().findByKey(reservation.getId());
            assertEquals(reservation, createdReservation);
            context.beginTransaction();
            context.getReservationRepository().delete(reservation);
            context.getClassicBikeRepository().delete(testClassicBike);
            context.getClientRepository().delete(testClient);
            context.getManagerRepository().delete(testManager);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUpdateReservation() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            context.getStoreRepository().create(testStore);
            context.getClientRepository().create(testClient);
            context.getClassicBikeRepository().create(testClassicBike);
            context.flush();
            Reservation reservation = createReservation();
            context.getReservationRepository().create(reservation);
            context.commit();
            reservation.setPrice(99.00);
            context.beginTransaction();
            context.getReservationRepository().update(reservation);
            context.commit();
            Reservation updatedReservation = (Reservation) context.getReservationRepository().findByKey(reservation.getId());
            assertEquals(reservation, updatedReservation);
            context.beginTransaction();
            context.getReservationRepository().delete(updatedReservation);
            context.getClassicBikeRepository().delete(testClassicBike);
            context.getClientRepository().delete(testClient);
            context.getManagerRepository().delete(testManager);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeleteReservation() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            context.getStoreRepository().create(testStore);
            context.getClientRepository().create(testClient);
            context.getClassicBikeRepository().create(testClassicBike);
            context.flush();
            assertEquals("livre", testClassicBike.getState());
            Reservation reservation = createReservation();
            context.getReservationRepository().create(reservation);
            context.commit();
            assertEquals("em reserva", testClassicBike.getState());
            context.beginTransaction();
            context.getReservationRepository().delete(reservation);
            context.commit();
            context.beginTransaction();
            assertEquals("livre", testClassicBike.getState());
            context.getClassicBikeRepository().delete(testClassicBike);
            context.getClientRepository().delete(testClient);
            context.getManagerRepository().delete(testManager);
            context.commit();
            Reservation deletedReservation = (Reservation) context.getReservationRepository().findByKey(reservation.getId());
            assertNull(deletedReservation);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeactivateReservation() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            context.getStoreRepository().create(testStore);
            context.getClientRepository().create(testClient);
            context.getClassicBikeRepository().create(testClassicBike);
            context.flush();
            IReservation reservation = createReservation();
            context.getReservationRepository().create(reservation);
            context.commit();
            assertEquals("em reserva", testClassicBike.getState());
            assertTrue(reservation.isActive());
            context.getReservationRepository().deactivate(reservation);
            assertFalse(reservation.isActive());
            assertEquals("livre", testClassicBike.getState());
            context.beginTransaction();
            context.getClassicBikeRepository().delete(testClassicBike);
            context.getClientRepository().delete(testClient);
            context.getManagerRepository().delete(testManager);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetAllReservations() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            context.getStoreRepository().create(testStore);
            context.getClientRepository().create(testClient);
            context.getClassicBikeRepository().create(testClassicBike);
            context.flush();
            Reservation reservation = createReservation();
            context.getReservationRepository().create(reservation);
            context.commit();
            List<IReservation> reservations = context.getReservationRepository().findAll();
            assertTrue(reservations.contains(reservation));
            context.beginTransaction();
            context.getReservationRepository().delete(reservation);
            context.getClassicBikeRepository().delete(testClassicBike);
            context.getClientRepository().delete(testClient);
            context.getManagerRepository().delete(testManager);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
