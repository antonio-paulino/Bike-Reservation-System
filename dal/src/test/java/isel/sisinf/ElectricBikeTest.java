package isel.sisinf;

import isel.sisinf.jpa.JPAContext;
import isel.sisinf.model.Bike.ElectricBike;
import isel.sisinf.model.Bike.IElectricBike;
import isel.sisinf.model.GPS.GPS;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ElectricBikeTest {

    private final GPS testGPS = new GPS(0, 38.736946, -9.138611, 100);
    private final ElectricBike testElectricBike =
            new ElectricBike(0, 75.00, "TestModel",
            "TestBrand", 6, "livre",
            testGPS, 100, 25);

    @Test
    public void testCreateElectricBike() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            ElectricBike electricBike = testElectricBike;
            context.getElectricBikeRepository().create(electricBike);
            context.commit();
            ElectricBike createdElectricBike = (ElectricBike) context.getElectricBikeRepository().findByKey(electricBike.getId());
            assertEquals(electricBike, createdElectricBike);
            context.beginTransaction();
            context.getElectricBikeRepository().delete(electricBike);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUpdateElectricBike() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            ElectricBike electricBike = testElectricBike;
            context.getElectricBikeRepository().create(electricBike);
            context.commit();
            ElectricBike createdElectricBike = (ElectricBike) context.getElectricBikeRepository().findByKey(electricBike.getId());
            createdElectricBike.setAutonomy(200);
            createdElectricBike.setSpeed(50);
            createdElectricBike.setState("em reserva");
            context.beginTransaction();
            context.getElectricBikeRepository().update(createdElectricBike);
            context.commit();
            ElectricBike updatedElectricBike = (ElectricBike) context.getElectricBikeRepository().findByKey(createdElectricBike.getId());
            assertEquals(createdElectricBike, updatedElectricBike);
            context.beginTransaction();
            context.getElectricBikeRepository().delete(updatedElectricBike);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeleteElectricBike() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            ElectricBike electricBike = testElectricBike;
            context.getElectricBikeRepository().create(electricBike);
            context.commit();
            ElectricBike createdElectricBike = (ElectricBike) context.getElectricBikeRepository().findByKey(electricBike.getId());
            context.beginTransaction();
            context.getElectricBikeRepository().delete(createdElectricBike);
            context.commit();
            ElectricBike deletedElectricBike = (ElectricBike) context.getElectricBikeRepository().findByKey(createdElectricBike.getId());
            assertNull(deletedElectricBike);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeactivateElectricBike() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            context.getGPSRepository().create(testGPS);
            ElectricBike electricBike = testElectricBike;
            context.getElectricBikeRepository().create(electricBike);
            context.commit();
            assertTrue(electricBike.isActive());
            ElectricBike createdElectricBike = (ElectricBike) context.getElectricBikeRepository().findByKey(electricBike.getId());
            context.getElectricBikeRepository().deactivate(createdElectricBike);
            assertFalse(createdElectricBike.isActive());
            assertNull(context.getElectricBikeRepository().findByKey(createdElectricBike.getId()));
            context.beginTransaction();
            context.getElectricBikeRepository().delete(createdElectricBike);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetAll() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            context.getGPSRepository().create(testGPS);
            ElectricBike electricBike = testElectricBike;
            context.getElectricBikeRepository().create(electricBike);
            context.commit();
            List<IElectricBike> bikes = context.getElectricBikeRepository().findAll();
            assertTrue(bikes.contains(electricBike));
            context.beginTransaction();
            context.getElectricBikeRepository().delete(electricBike);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


}
