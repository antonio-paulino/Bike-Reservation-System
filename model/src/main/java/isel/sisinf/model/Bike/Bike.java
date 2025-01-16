package isel.sisinf.model.Bike;


import isel.sisinf.model.GPS.GPS;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "atrdisc")
@NamedStoredProcedureQuery(
        name = "isbikeavailable",
        procedureName = "isbikeavailable",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Timestamp.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Timestamp.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = Boolean.class),
        }
)
public abstract class Bike implements IBike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, precision = 4, scale = 2)
    private double weight;

    @Column(nullable = false, length = 20)
    private String model;

    @Column(nullable = false, length = 30)
    private String brand;

    @Column
    private Integer gear;

    @Column(nullable = false, length = 30)
    private String state;

    @Column(nullable = false)
    private char atrdisc;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "gps", referencedColumnName = "serialNumber")
    private GPS gps;

    @Column(nullable = false)
    private boolean active;

    @Transient
    private static List<Integer> validGears = List.of(1,6,18,24);

    public static void addValidGear(int gear) {
        validGears.add(gear);
    }

    public static void removeValidGear(int gear) {
        validGears.remove(gear);
    }

    public static boolean isValidGear(int gear) {
        return validGears.contains(gear);
    }

    public Bike() {
    }

    public Bike(int id, double weight, String model, String brand, Integer gear, String state, char atrdisc, GPS gps) {
        setId(id);
        setWeight(weight);
        setModel(model);
        setBrand(brand);
        setGear(gear);
        setState(state);
        setAtrdisc(atrdisc);
        setGPS(gps);
        setActive(true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("Invalid id");
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight < 0 || weight > 100)
            throw new IllegalArgumentException("Weight must be between 0 and 100");
        this.weight = weight;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        if (model.length() > 20)
            throw new IllegalArgumentException("Model must have at most 20 characters");
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        if (brand.length() > 30)
            throw new IllegalArgumentException("Brand must have at most 30 characters");
        this.brand = brand;
    }

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        if (!isValidGear(gear))
            throw new IllegalArgumentException("Invalid gear");
        this.gear = gear;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if (state.length() > 40)
            throw new IllegalArgumentException("State must have at most 40 characters");
        if (!state.equals("livre") && !state.equals("ocupado") && !state.equals("em reserva") && !state.equals("em manutenção"))
            throw new IllegalArgumentException("Invalid state");
        this.state = state;
    }

    public char getAtrdisc() {
        return atrdisc;
    }

    public void setAtrdisc(char atrdisc) {
        if (atrdisc != 'C' && atrdisc != 'E')
            throw new IllegalArgumentException("Invalid atrdisc");
        this.atrdisc = atrdisc;
    }

    public GPS getGPS() {
        return gps;
    }

    public void setGPS(GPS gps) {
        this.gps = gps;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "{id=" + id + ", weight=" + weight + ", model=" + model + ", brand=" + brand + ", gear=" + gear + ", state=" + state + ", atrdisc=" + atrdisc + ", gps=" + gps + ", active=" + active + "}";
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bike other = (Bike) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
