package isel.sisinf.model.Reservation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReservationIDTest {

    @Test
    void equalsReturnsFalseForNull() {
        ReservationID reservationID = new ReservationID(1, 1);
        assertNotEquals(null, reservationID);
    }

    @Test
    void equalsReturnsFalseForDifferentClass() {
        ReservationID reservationID = new ReservationID(1, 1);
        assertNotEquals(reservationID, new Object());
    }

    @Test
    void equalsReturnsTrueForSameReservationNoAndStore() {
        ReservationID reservationID1 = new ReservationID(1, 1);
        ReservationID reservationID2 = new ReservationID(1, 1);
        assertEquals(reservationID1, reservationID2);
    }

    @Test
    void equalsReturnsFalseForDifferentReservationNo() {
        ReservationID reservationID1 = new ReservationID(1, 1);
        ReservationID reservationID2 = new ReservationID(2, 1);
        assertNotEquals(reservationID1, reservationID2);
    }

    @Test
    void equalsReturnsFalseForDifferentStore() {
        ReservationID reservationID1 = new ReservationID(1, 1);
        ReservationID reservationID2 = new ReservationID(1, 2);
        assertNotEquals(reservationID1, reservationID2);
    }

    @Test
    void hashCodeReturnsSameValueForSameReservationNoAndStore() {
        ReservationID reservationID1 = new ReservationID(1, 1);
        ReservationID reservationID2 = new ReservationID(1, 1);
        assertEquals(reservationID1.hashCode(), reservationID2.hashCode());
    }

    @Test
    void toStringReturnsCorrectFormat() {
        ReservationID reservationID = new ReservationID(1, 1);
        assertEquals("{reservation_no=1, store=1}", reservationID.toString());
    }
}