package isel.sisinf.model.Reservation;

import isel.sisinf.model.Bike.Bike;
import isel.sisinf.model.Person.Person;
import isel.sisinf.model.Store.Store;

import java.sql.Timestamp;

public interface IReservation {

    ReservationID getId();

    ReservationID setId(int id);

    Timestamp getStart_date();

    void setStart_date(Timestamp start_date);
    Timestamp getEnd_date();
    void setEnd_date(Timestamp end_date);

    double getPrice();

    void setPrice(double price);

    Bike getBike();
    void setBike(Bike bike);

    Person getClient();

    void setClient(Person client);

    Store getStoreEntity();

    void setStoreEntity(Store storeEntity);

    boolean isActive();

    void setActive(boolean active);


}
