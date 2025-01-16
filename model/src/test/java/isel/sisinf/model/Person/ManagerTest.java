package isel.sisinf.model.Person;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {

    @Test
    public void managerCreationWithValidData() {
        Manager manager = new Manager(1, "John Doe", "Test Address", "test@isel.pt", "123456789", "123456789", "Portuguese");
        assertEquals("John Doe", manager.getName());
        assertEquals("Test Address", manager.getAddress());
        assertEquals("test@isel.pt", manager.getEmail());
        assertEquals("123456789", manager.getPhone());
        assertEquals("123456789", manager.getIdentification());
        assertEquals("Portuguese", manager.getNationality());
        assertTrue(manager.isActive());
    }

    @Test
    public void managerCreationWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Manager(-1, "John Doe", "Test Address", "test@isel.pt", "123456789", "123456789", "Portuguese");
        });
    }

    @Test
    public void managerCreationWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Manager(1, "This name is way too long to be a valid name because it has more than 40 characters", "Test Address", "test@isel.pt", "123456789", "123456789", "Portuguese");
        });
    }

    @Test
    public void managerCreationWithInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Manager(1, "John Doe", "Test Address", "not a valid email", "123456789", "123456789", "Portuguese");
        });
    }

    @Test
    public void managerCreationWithInvalidPhone() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Manager(1, "John Doe", "Test Address", "test@isel.pt", "This phone number is way too long to be a valid phone number because it has more than 20 characters", "123456789", "Portuguese");
        });
    }

    @Test
    public void managerCreationWithInvalidIdentification() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Manager(1, "John Doe", "Test Address", "test@isel.pt", "123456789", "This identification is way too long to be a valid identification because it has more than 12 characters", "Portuguese");
        });
    }

    @Test
    public void managerCreationWithInvalidNationality() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Manager(1, "John Doe", "Test Address", "test@isel.pt", "123456789", "123456789", "This nationality is way too long to be a valid nationality because it has more than 20 characters");
        });
    }
}