package isel.sisinf.model.Reservation;

import isel.sisinf.model.Bike.Bike;
import isel.sisinf.model.Person.Person;
import isel.sisinf.model.Store.Store;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@NamedQuery(name = "Reservation.findAll", query = "SELECT r FROM Reservation r WHERE r.active = true")
@NamedQuery(name = "Reservation.findByKey", query = "SELECT r FROM Reservation r WHERE r.id = :id AND r.active = true")
@NamedStoredProcedureQuery(
        name = "createreservation",
        procedureName = "createreservation",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Timestamp.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Timestamp.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Double.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = Integer.class)
        }
)
@NamedStoredProcedureQuery(
        name = "reservationsavailable",
        procedureName = "reservationsavailable",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = Boolean.class)
        }
)
public class Reservation implements IReservation {

    @Column(nullable = false)
    @EmbeddedId
    private ReservationID id;

    @Column(nullable = false)
    @Version
    private int version;

    @Column(nullable = false)
    private Timestamp start_date;

    @Column
    private Timestamp end_date;

    @Column(precision = 4, scale = 2)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bike", referencedColumnName = "id")
    private Bike bike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store", referencedColumnName = "code", insertable = false, updatable = false)
    private Store storeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client", referencedColumnName = "id")
    private Person client;

    @Column(nullable = false)
    private boolean active;

    public Reservation() {
    }

    public Reservation(int reservation_no, int store_id, Timestamp start_date, Timestamp end_date, double valor, Bike bike, Store storeEntity, Person client) {
        id = new ReservationID(reservation_no, store_id);
        setStart_date(start_date);
        setEnd_date(end_date);
        setPrice(valor);
        setBike(bike);
        setStoreEntity(storeEntity);
        setClient(client);
        setActive(true);
    }

    public Reservation(String[] data, Bike bike, Store storeEntity, Person client) {
        id = new ReservationID(0, storeEntity.getCode());
        setStart_date(Timestamp.valueOf(data[0]));
        setEnd_date(Timestamp.valueOf(data[1]));
        setPrice(Double.parseDouble(data[2]));
        setBike(bike);
        setStoreEntity(storeEntity);
        setClient(client);
        setActive(true);
    }

    public ReservationID getId() {
        return id;
    }

    public ReservationID setId(int id) {
        return this.id = new ReservationID(id, storeEntity.getCode());
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(Timestamp start_date) {
        if (start_date.before(new Timestamp(System.currentTimeMillis())))
            throw new IllegalArgumentException("Start date must be after current date");
        if (end_date != null && start_date.after(end_date))
            throw new IllegalArgumentException("Start date must be before end date");
        this.start_date = start_date;
    }

    public Timestamp getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Timestamp end_date) {
        if (end_date != null && end_date.before(start_date))
            throw new IllegalArgumentException("End date must be after start date");
        this.end_date = end_date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public Store getStoreEntity() {
        return storeEntity;
    }

    public void setStoreEntity(Store storeEntity) {
        this.storeEntity = storeEntity;
    }

    public Person getClient() {
        return client;
    }

    public void setClient(Person client) {
        this.client = client;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "{id=" + id + ", start_date=" + start_date + ", end_date=" + end_date + ", price=" + price + ", bike=" + bike + ", storeEntity=" + storeEntity + ", client=" + client + ", active=" + active + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Reservation other = (Reservation) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}

