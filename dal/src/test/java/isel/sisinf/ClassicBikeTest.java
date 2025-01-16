package isel.sisinf;

import isel.sisinf.model.Bike.ClassicBike;
import isel.sisinf.model.Bike.IClassicBike;
import isel.sisinf.model.GPS.GPS;
import isel.sisinf.jpa.JPAContext;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ClassicBikeTest {

    private final GPS testGPS = new GPS(0, 38.736946, -9.138611, 100);
    private final ClassicBike testClassicBike = new ClassicBike(0, 75.00, "TestModel", "TestBrand", 6, "livre", testGPS, 2);

    @Test
    public void testCreateClassicBike() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            ClassicBike classicBike = testClassicBike;
            context.getClassicBikeRepository().create(classicBike);
            context.commit();
            ClassicBike createdClassicBike = (ClassicBike) context.getClassicBikeRepository().findByKey(classicBike.getId());
            assertEquals(classicBike, createdClassicBike);
            context.beginTransaction();
            context.getClassicBikeRepository().delete(classicBike);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUpdateClassicBike() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            ClassicBike classicBike = testClassicBike;
            context.getClassicBikeRepository().create(classicBike);
            context.commit();
            classicBike.setGears(3);
            context.beginTransaction();
            context.getClassicBikeRepository().update(classicBike);
            context.commit();
            ClassicBike updatedClassicBike = (ClassicBike) context.getClassicBikeRepository().findByKey(classicBike.getId());
            assertEquals(classicBike, updatedClassicBike);
            context.beginTransaction();
            context.getClassicBikeRepository().delete(classicBike);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeleteClassicBike() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            ClassicBike classicBike = testClassicBike;
            context.getClassicBikeRepository().create(classicBike);
            context.commit();
            context.beginTransaction();
            context.getClassicBikeRepository().delete(classicBike);
            context.commit();
            ClassicBike deletedClassicBike = (ClassicBike) context.getClassicBikeRepository().findByKey(classicBike.getId());
            assertNull(deletedClassicBike);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeactivateClassicBike() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            ClassicBike classicBike = testClassicBike;
            context.getClassicBikeRepository().create(classicBike);
            context.commit();
            context.beginTransaction();
            assertTrue(classicBike.isActive());
            context.getClassicBikeRepository().deactivate(classicBike);
            context.commit();
            assertFalse(classicBike.isActive());
            assertNull(context.getClassicBikeRepository().findByKey(classicBike.getId()));
            context.beginTransaction();
            context.getClassicBikeRepository().delete(classicBike);
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
            context.getClassicBikeRepository().create(testClassicBike);
            context.commit();
            List<IClassicBike> bikes = context.getClassicBikeRepository().findAll();
            assertTrue(bikes.contains(testClassicBike));
            context.beginTransaction();
            context.getClassicBikeRepository().delete(testClassicBike);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
