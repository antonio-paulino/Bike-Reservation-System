package isel.sisinf;

import isel.sisinf.jpa.JPAContext;
import isel.sisinf.model.GPS.GPS;
import isel.sisinf.model.GPS.IGPS;
import org.junit.Test;

import static org.junit.Assert.*;

public class GPSTest {

    private final GPS testGPS = new GPS(0, 38.73, -9.13, 100);

    @Test
    public void testCreateGPS() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            IGPS gps = testGPS;
            context.getGPSRepository().create(gps);
            context.commit();
            IGPS createdGPS = context.getGPSRepository().findByKey(gps.getSerialNumber());
            assertEquals(gps, createdGPS);
            context.beginTransaction();
            context.getGPSRepository().delete(gps);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUpdateGPS() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            IGPS gps = testGPS;
            context.getGPSRepository().create(gps);
            context.commit();
            gps.setBattery(50);
            context.beginTransaction();
            context.getGPSRepository().update(gps);
            context.commit();
            IGPS updatedGPS = context.getGPSRepository().findByKey(gps.getSerialNumber());
            assertEquals(gps, updatedGPS);
            context.beginTransaction();
            context.getGPSRepository().delete(gps);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeleteGPS() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            IGPS gps = testGPS;
            context.getGPSRepository().create(gps);
            context.commit();
            IGPS createdGPS = context.getGPSRepository().findByKey(gps.getSerialNumber());
            assertNotNull(createdGPS);
            context.beginTransaction();
            context.getGPSRepository().delete(gps);
            context.commit();
            IGPS deletedGPS = context.getGPSRepository().findByKey(gps.getSerialNumber());
            assertNull(deletedGPS);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
