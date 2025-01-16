package isel.sisinf;

import isel.sisinf.jpa.JPAContext;
import isel.sisinf.model.Person.Client;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClientTest {

    private final Client testClient = new Client(0, "John Doe",
            "Rua Teste", "test@asd.pt",
            "123456789", "123456789",
            "Portuguese");
    @Test
    public void testCreateClient() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Client client = testClient;
            context.getClientRepository().create(client);
            context.commit();
            Client createdClient = (Client) context.getClientRepository().findByKey(client.getId());
            assertEquals(client, createdClient);
            context.beginTransaction();
            context.getClientRepository().delete(client);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUpdateClient() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Client client = testClient;
            context.getClientRepository().create(client);
            context.commit();
            client.setPhone("987654321");
            context.beginTransaction();
            context.getClientRepository().update(client);
            context.commit();
            Client updatedClient = (Client) context.getClientRepository().findByKey(client.getId());
            assertEquals(client, updatedClient);
            context.beginTransaction();
            context.getClientRepository().delete(client);
            context.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeleteClient() {
        try (JPAContext context = new JPAContext()) {
            context.beginTransaction();
            Client client = testClient;
            context.getClientRepository().create(client);
            context.commit();
            Client createdClient = (Client) context.getClientRepository().findByKey(client.getId());
            assertEquals(createdClient, client);
            context.beginTransaction();
            context.getClientRepository().delete(client);
            context.commit();
            Client deletedManager = (Client) context.getClientRepository().findByKey(client.getId());
            assertNull(deletedManager);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
