package isel.sisinf.model.GPS;

import jakarta.persistence.*;

@Entity
@NamedQuery(name = "GPS.findBySerialNumber",
    query = "SELECT g FROM GPS g WHERE g.serialNumber = :serialNumber"
)
@NamedQuery(name = "GPS.findAll",
    query = "SELECT g FROM GPS g"
)
public class GPS implements IGPS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serialNumber;

    @Column(nullable = false, precision = 6, scale = 4)
    private double latitude;

    @Column(nullable = false, precision = 6, scale = 4)
    private double longitude;

    @Column(nullable = false)
    private int battery;

    public GPS() {
    }

    public GPS(int serialNumber, double latitude, double longitude, int battery) {
        setSerialNumber(serialNumber);
        setLatitude(latitude);
        setLongitude(longitude);
        setBattery(battery);
    }

    public GPS(String[] data) {
        setSerialNumber(0);
        setLatitude(Double.parseDouble(data[0]));
        setLongitude(Double.parseDouble(data[1]));
        setBattery(Integer.parseInt(data[2]));
    }

    @Override
    public int getSerialNumber() {
        return serialNumber;
    }

    @Override
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int getBattery() {
        return battery;
    }

    @Override
    public void setBattery(int battery) {
        if (battery < 0 || battery > 100)
            throw new IllegalArgumentException("Battery must be between 0 and 100");
        this.battery = battery;
    }

    @Override
    public String toString() {
        return "{serialNumber=" + serialNumber + ", latitude=" + latitude + ", longitude=" + longitude + ", battery=" + battery + "}";
    }

    @Override
    public int hashCode() {
        return serialNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GPS other = (GPS) obj;
        return serialNumber == other.serialNumber;
    }

}
