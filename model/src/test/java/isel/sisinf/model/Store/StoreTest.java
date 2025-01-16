package isel.sisinf.model.Store;


import isel.sisinf.model.Person.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertThrows;

public class StoreTest {

    private Manager validManager;

    @BeforeEach
    public void setup() {
        validManager = new Manager(0, "John Doe", "teste", "test@isel.pt", "123456789", "123456789", "Portuguese");
        Store validStore = new Store(0, "testemail@isel.pt", "testaddress", "testlocation", "123456789", validManager);
    }

    @Test
    public void createStoreWithNegativeCode() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Store(-1, "testemail@isel.pt", "testaddress", "testlocation", "123456789", validManager);
        });
    }

    @Test
    public void createStoreWithInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Store(0, "invalidEmail", "testaddress", "testlocation", "123456789", validManager);
        });
    }

    @Test
    public void createStoreWithLongAddress() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Store(0, "testemail@isel.pt", "a".repeat(101), "testlocation", "123456789", validManager);
        });
    }

    @Test
    public void createStoreWithLongLocation() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Store(0, "testemail@isel.pt", "testaddress", "a".repeat(31), "123456789", validManager);
        });
    }

    @Test
    public void createStoreWithLongPhone() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Store(0, "testemail@isel.pt", "testaddress", "testlocation", "1".repeat(31), validManager);
        });
    }
}