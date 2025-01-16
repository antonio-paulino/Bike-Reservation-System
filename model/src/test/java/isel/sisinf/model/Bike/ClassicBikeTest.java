package isel.sisinf.model.Bike;

import isel.sisinf.model.GPS.GPS;
import org.junit.jupiter.api.Test;
import static junit.framework.Assert.assertEquals;
import static org.eclipse.persistence.jpa.jpql.Assert.fail;

public class ClassicBikeTest {

    @Test
    public void setGearsChangesGears() {
        ClassicBike classicBike = new ClassicBike();
        classicBike.setGears(5);
        assertEquals(5, classicBike.getGears());
    }

    @Test
    public void setGearsToNegativeThrowsException() {
        ClassicBike classicBike = new ClassicBike();
        try {
            classicBike.setGears(-1);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Gears must be between 0 and 5", e.getMessage());
        }
    }

    @Test
    public void constructorSetsGears() {
        ClassicBike classicBike = new ClassicBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", new GPS(0, 38.736946, -9.138611, 100), 2);
        assertEquals(2, classicBike.getGears());
    }

    @Test
    public void constructorWithZeroGearsThrowsException() {
        try {
            new ClassicBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", new GPS(0, 38.736946, -9.138611, 100), 6);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Gears must be between 0 and 5", e.getMessage());
        }
    }

    @Test
    public void constructorWithNegativeGearsThrowsException() {
        try {
            new ClassicBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", new GPS(0, 38.736946, -9.138611, 100), -1);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Gears must be between 0 and 5", e.getMessage());
        }
    }


}