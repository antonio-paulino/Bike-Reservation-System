package isel.sisinf;

import isel.sisinf.jpa.JPAContext;
import isel.sisinf.model.Person.IManager;
import isel.sisinf.model.Person.Manager;
import org.junit.Test;

import static org.junit.Assert.*;

public class ManagerTest {

    private final Manager testManager = new Manager(0, "John Doe",
            "Test", "test@isel.pt",
            "123456789", "123456789",
            "Portuguese");

    @Test
    public void testCreateManager() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Manager manager = testManager;
            context.getManagerRepository().create(manager);
            context.commit();
            IManager createdManager = context.getManagerRepository().findByKey(manager.getId());
            assertEquals(manager, createdManager);
            context.beginTransaction();
            context.getManagerRepository().delete(manager);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUpdateManager() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Manager manager = testManager;
            context.getManagerRepository().create(manager);
            context.commit();
            manager.setPhone("987654321");
            context.beginTransaction();
            context.getManagerRepository().update(manager);
            context.commit();
            IManager updatedManager = context.getManagerRepository().findByKey(manager.getId());
            assertEquals(manager, updatedManager);
            context.beginTransaction();
            context.getManagerRepository().delete(manager);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeleteManager() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Manager manager = testManager;
            context.getManagerRepository().create(manager);
            context.commit();
            IManager createdManager = context.getManagerRepository().findByKey(manager.getId());
            assertEquals(manager, createdManager);
            context.beginTransaction();
            context.getManagerRepository().delete(manager);
            context.commit();
            IManager deletedManager = context.getManagerRepository().findByKey(manager.getId());
            assertNull(deletedManager);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeactivateManager() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Manager manager = testManager;
            context.getManagerRepository().create(manager);
            context.commit();
            assertTrue(manager.isActive());
            context.getManagerRepository().deactivate(manager);
            assertFalse(manager.isActive());
            assertNull(context.getManagerRepository().findByKey(manager.getId()));
            context.beginTransaction();
            context.getManagerRepository().delete(manager);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
