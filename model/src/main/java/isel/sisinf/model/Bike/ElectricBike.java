package isel.sisinf.model.Bike;

import isel.sisinf.model.GPS.GPS;
import jakarta.persistence.*;


@Entity
@DiscriminatorValue("E")
@PrimaryKeyJoinColumn(name = "bike")
@NamedQuery(name = "ElectricBike.findAll", query = "SELECT eb FROM ElectricBike eb WHERE eb.atrdisc = 'E' and eb.active = true")
@NamedQuery(name = "ElectricBike.findByKey", query = "SELECT eb FROM ElectricBike eb WHERE eb.id = :id AND eb.atrdisc = 'E' and eb.active = true")
public class ElectricBike extends Bike implements IElectricBike {

    @Column(nullable = false)
    private int autonomy;

    @Column(nullable = false)
    private int speed;

    public ElectricBike() {
    }

    public ElectricBike(int id, double weight, String model, String brand, Integer gear, String state, GPS gps, int autonomy, int speed) {
        super(id, weight, model, brand, gear, state, 'E', gps);
        setAutonomy(autonomy);
        setSpeed(speed);
    }

    public ElectricBike(String[] data, GPS gps) {
        super(0, Double.parseDouble(data[0]), data[1], data[2], Integer.parseInt(data[3]), data[4], 'E', gps);
        setAutonomy(Integer.parseInt(data[5]));
        setSpeed(Integer.parseInt(data[6]));
    }

    public int getAutonomy() {
        return autonomy;
    }

    public void setAutonomy(int autonomy) {
        if (autonomy < 0) {
            throw new IllegalArgumentException("Autonomy must be a positive number");
        }
        this.autonomy = autonomy;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("Speed must be a positive number");
        }
        this.speed = speed;
    }

}