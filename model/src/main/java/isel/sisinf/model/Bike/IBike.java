package isel.sisinf.model.Bike;

import isel.sisinf.model.GPS.GPS;

public interface IBike {
    int getId();
    void setId(int id);
    String getBrand();
    void setBrand(String brand);
    String getModel();
    void setModel(String model);
    double getWeight();
    void setWeight(double weight);
    int getGear();
    void setGear(int gear);
    String getState();
    void setState(String state);
    GPS getGPS();
    void setGPS(GPS gps);
    boolean isActive();
    void setActive(boolean active);

}
