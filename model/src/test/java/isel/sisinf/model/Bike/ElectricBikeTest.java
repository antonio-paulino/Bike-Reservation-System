package isel.sisinf.model.Bike;


import isel.sisinf.model.GPS.GPS;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ElectricBikeTest {

    @Test
    public void electricBikeCreationWithValidParameters() {
        GPS gps = new GPS(0, 38.736946, -9.138611, 100);
        ElectricBike electricBike = new ElectricBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", gps, 100, 25);
        assertEquals(100, electricBike.getAutonomy());
        assertEquals(25, electricBike.getSpeed());
    }

    @Test
    public void electricBikeCreationWithNegativeAutonomy() {
        GPS gps = new GPS(0, 38.736946, -9.138611, 100);
        assertThrows(IllegalArgumentException.class, () -> new ElectricBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", gps, -100, 25));
    }

    @Test
    public void electricBikeCreationWithNegativeSpeed() {
        GPS gps = new GPS(0, 38.736946, -9.138611, 100);
        assertThrows(IllegalArgumentException.class, () -> new ElectricBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", gps, 100, -25));
    }

    @Test
    public void setAutonomyWithValidValue() {
        GPS gps = new GPS(0, 38.736946, -9.138611, 100);
        ElectricBike electricBike = new ElectricBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", gps, 100, 25);
        electricBike.setAutonomy(200);
        assertEquals(200, electricBike.getAutonomy());
    }

    @Test
    public void setAutonomyWithNegativeValue() {
        GPS gps = new GPS(0, 38.736946, -9.138611, 100);
        ElectricBike electricBike = new ElectricBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", gps, 100, 25);
        assertThrows(IllegalArgumentException.class, () -> electricBike.setAutonomy(-200));
    }

    @Test
    public void setSpeedWithValidValue() {
        GPS gps = new GPS(0, 38.736946, -9.138611, 100);
        ElectricBike electricBike = new ElectricBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", gps, 100, 25);
        electricBike.setSpeed(50);
        assertEquals(50, electricBike.getSpeed());
    }

    @Test
    public void setSpeedWithNegativeValue() {
        GPS gps = new GPS(0, 38.736946, -9.138611, 100);
        ElectricBike electricBike = new ElectricBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", gps, 100, 25);
        assertThrows(IllegalArgumentException.class, () -> electricBike.setSpeed(-50));
    }
}