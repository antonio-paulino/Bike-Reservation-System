package isel.sisinf.model.Bike;

import isel.sisinf.model.GPS.GPS;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("C")
@PrimaryKeyJoinColumn(name = "bike")
@NamedQuery(name = "ClassicBike.findAll", query = "SELECT cb FROM ClassicBike cb WHERE cb.atrdisc = 'C' and cb.active = true")
@NamedQuery(name = "ClassicBike.findByKey", query = "SELECT cb FROM ClassicBike cb WHERE cb.id = :id AND cb.atrdisc = 'C' and cb.active = true")
public class ClassicBike extends Bike implements IClassicBike {

    @Column(nullable = false)
    private int gears;

    public ClassicBike() {
    }

    public ClassicBike(int id, double weight, String model, String brand, Integer gear, String state, GPS gps, int gears) {
        super(id, weight, model, brand, gear, state, 'C', gps);
        setGears(gears);
    }

    public ClassicBike(String[] data, GPS gps) {
        super(0, Double.parseDouble(data[0]), data[1], data[2], Integer.parseInt(data[3]), data[4], 'C', gps);
        setGears(Integer.parseInt(data[5]));
    }

    private boolean isGearsValid(int gears) {
        return 0 <= gears && gears <= 5;
    }

    public int getGears() {
        return gears;
    }

    public void setGears(int gears) {
        if (!isGearsValid(gears)) {
            throw new IllegalArgumentException("Gears must be between 0 and 5");
        }
        this.gears = gears;
    }

}
