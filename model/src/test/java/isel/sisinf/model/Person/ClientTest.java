package isel.sisinf.model.Person;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    @Test
    public void clientCreationWithValidData() {
        Client client = new Client(1, "John Doe", "123 Street", "john@example.com", "123456789", "ID123", "US");
        assertEquals(1, client.getId());
        assertEquals("John Doe", client.getName());
        assertEquals("123 Street", client.getAddress());
        assertEquals("john@example.com", client.getEmail());
        assertEquals("123456789", client.getPhone());
        assertEquals("ID123", client.getIdentification());
        assertEquals("US", client.getNationality());
    }

    @Test
    public void clientCreationWithEmptyDataShouldFail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Client(0, "", "", "", "", "", "");
        });
    }

    @Test
    public void clientUpdateWithInvalidPhoneShouldFail() {
        Client client = new Client(1, "John Doe", "123 Street", "john@example.com", "123456789", "ID123", "US");
        assertThrows(IllegalArgumentException.class, () -> {
            client.setPhone("123456789123456789012");
        });
    }
}