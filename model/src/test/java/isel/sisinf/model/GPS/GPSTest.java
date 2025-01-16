package isel.sisinf.model.GPS;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GPSTest {

    @Test
    public void testGetSerialNumber() {
        GPS instance = new GPS(1, 0.0, 0.0, 0);
        int expResult = 1;
        int result = instance.getSerialNumber();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetSerialNumber() {
        int serialNumber = 1;
        GPS instance = new GPS();
        instance.setSerialNumber(serialNumber);
        assertEquals(serialNumber, instance.getSerialNumber());
    }

    @Test
    public void testGetLatitude() {
        GPS instance = new GPS(0, 1.0, 0.0, 0);
        double expResult = 1.0;
        double result = instance.getLatitude();
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testSetLatitude() {
        double latitude = 1.0;
        GPS instance = new GPS();
        instance.setLatitude(latitude);
        assertEquals(latitude, instance.getLatitude(), 0.0);
    }

    @Test
    public void testGetLongitude() {
        GPS instance = new GPS(0, 0.0, 1.0, 0);
        double expResult = 1.0;
        double result = instance.getLongitude();
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testSetLongitude() {
        double longitude = 1.0;
        GPS instance = new GPS();
        instance.setLongitude(longitude);
        assertEquals(longitude, instance.getLongitude(), 0.0);
    }

    @Test
    public void testGetBattery() {
        GPS instance = new GPS(0, 0.0, 0.0, 1);
        int expResult = 1;
        int result = instance.getBattery();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetBattery() {
        int battery = 1;
        GPS instance = new GPS();
        instance.setBattery(battery);
        assertEquals(battery, instance.getBattery());
    }

    @Test
    public void testConstructor() {
        GPS instance = new GPS(1, 1.0, 1.0, 1);
        assertEquals(1, instance.getSerialNumber());
        assertEquals(1.0, instance.getLatitude(), 0.0);
        assertEquals(1.0, instance.getLongitude(), 0.0);
        assertEquals(1, instance.getBattery());
    }

    @Test
    public void testConstructorWithArray() {
        GPS instance = new GPS(new String[] {"1.0", "1.0", "1"});
        assertEquals(0, instance.getSerialNumber());
        assertEquals(1.0, instance.getLatitude(), 0.0);
        assertEquals(1.0, instance.getLongitude(), 0.0);
        assertEquals(1, instance.getBattery());
    }
}