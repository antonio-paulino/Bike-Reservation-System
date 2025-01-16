package isel.sisinf.model.GPS;

public interface IGPS {
    int getSerialNumber();
    void setSerialNumber(int serialNumber);
    double getLatitude();
    void setLatitude(double latitude);
    double getLongitude();
    void setLongitude(double longitude);
    int getBattery();

    void setBattery(int battery);
}
