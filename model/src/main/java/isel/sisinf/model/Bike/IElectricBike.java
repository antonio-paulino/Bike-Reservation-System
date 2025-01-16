package isel.sisinf.model.Bike;

public interface IElectricBike extends IBike {
    public int getAutonomy();
    public void setAutonomy(int autonomy);

    public int getSpeed();

    public void setSpeed(int speed);

}
