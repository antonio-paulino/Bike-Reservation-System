package isel.sisinf;

import isel.sisinf.jpa.JPAContext;
import isel.sisinf.model.Bike.ElectricBike;
import isel.sisinf.model.GPS.GPS;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContextTest {

    private final GPS testGPS = new GPS(0, 38.736946, -9.138611, 100);
    private final ElectricBike testElectricBike =
            new ElectricBike(0, 75.00, "TestModel",
                    "TestBrand", 6, "livre",
                    testGPS, 100, 25);

    @Test
    public void testIsBikeAvailable() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            ElectricBike electricBike = testElectricBike;
            context.getElectricBikeRepository().create(electricBike);
            context.commit();
            assertTrue(context.isBikeAvailable(electricBike.getId(), null, null));
            electricBike.setState("ocupado");
            context.beginTransaction();
            context.getElectricBikeRepository().update(electricBike);
            context.commit();
            assertFalse(context.isBikeAvailable(electricBike.getId(), null, null));
            context.beginTransaction();
            context.getElectricBikeRepository().delete(electricBike);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
