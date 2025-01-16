package isel.sisinf.model.Reservation;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ReservationID implements Serializable, IReservationID {
    private int reservation_no;
    private int store;

    public ReservationID() {
    }

    public ReservationID(int reservation_no, int store) {
        this.reservation_no = reservation_no;
        this.store = store;
    }

    public int getReservation_no() {
        return reservation_no;
    }

    public void setReservation_no(int reservation_no) {
        this.reservation_no = reservation_no;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "{reservation_no=" + reservation_no + ", store=" + store + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReservationID other = (ReservationID) obj;
        return reservation_no == other.reservation_no && store == other.store;
    }

    @Override
    public int hashCode() {
        return reservation_no + store;
    }

}
