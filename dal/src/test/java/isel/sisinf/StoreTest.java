package isel.sisinf;

import isel.sisinf.jpa.JPAContext;
import isel.sisinf.model.Person.Manager;
import isel.sisinf.model.Store.IStore;
import isel.sisinf.model.Store.Store;
import org.junit.Test;

import static org.junit.Assert.*;

public class StoreTest {

    private final Manager testManager = new Manager(0, "John Doe",
            "teste", "test@isel.pt",
            "123456789", "123456789",
            "Portuguese");
    private final Manager testManager2 = new Manager(0, "John Doe Replacement",
            "teste", "test2@isel.pt",
            "987654321", "987654321",
            "Portuguese");
    private final Store testStore = new Store(0, "testemail@isel.pt", "testaddress", "testlocation", "123456789", testManager);

    @Test
    public void testCreateStore() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Store store = testStore;
            context.getManagerRepository().create(testManager);
            context.getStoreRepository().create(store);
            context.commit();
            IStore createdStore = context.getStoreRepository().findByKey(store.getCode());
            assertEquals(store, createdStore);
            context.beginTransaction();
            context.getStoreRepository().delete(store);
            context.getManagerRepository().delete(testManager);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUpdateStore() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Store store = testStore;
            context.getManagerRepository().create(testManager);
            context.getManagerRepository().create(testManager2);
            context.getStoreRepository().create(store);
            context.commit();
            store.setManager(testManager2);
            context.beginTransaction();
            context.getStoreRepository().update(store);
            context.commit();
            IStore updatedStore = context.getStoreRepository().findByKey(store.getCode());
            assertEquals(store, updatedStore);
            context.beginTransaction();
            context.getStoreRepository().delete(store);
            context.getManagerRepository().delete(testManager2);
            context.getManagerRepository().delete(testManager);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeleteStore() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Store store = testStore;
            context.getManagerRepository().create(testManager);
            context.getStoreRepository().create(store);
            context.commit();
            context.beginTransaction();
            context.getStoreRepository().delete(store);
            context.getManagerRepository().delete(testManager);
            context.commit();
            IStore deletedStore = context.getStoreRepository().findByKey(store.getCode());
            assertNull(deletedStore);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeactivateStore() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Store store = testStore;
            context.getManagerRepository().create(testManager);
            context.getStoreRepository().create(store);
            context.commit();
            assertTrue(store.isActive());
            context.getStoreRepository().deactivate(store);
            IStore deactivatedStore = context.getStoreRepository().findByKey(store.getCode());
            assertFalse(deactivatedStore.isActive());
            context.beginTransaction();
            context.getStoreRepository().delete(store);
            context.getManagerRepository().delete(testManager);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


}
