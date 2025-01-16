package isel.sisinf.model.Store;

import isel.sisinf.model.Person.Manager;
import jakarta.persistence.*;

@Entity
@NamedQuery(name = "Store.findByKey", query = "SELECT s FROM Store s WHERE s.code = :code")
@NamedQuery(name = "Store.findAll", query = "SELECT s FROM Store s")
public class Store implements IStore {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int code;

    @Column(nullable = false, unique = true, length = 40)
    private String email;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false)
    private String location;

    @Column(length = 20)
    private String phone;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "manager", referencedColumnName = "id")
    private Manager manager;

    @Column(nullable = false)
    private boolean active;

    public Store() {
    }

    public Store(int code, String email, String address, String location, String phone, Manager manager) {
        setCode(code);
        setEmail(email);
        setAddress(address);
        setLocation(location);
        setPhone(phone);
        setManager(manager);
        setActive(true);
    }

    public Store(String[] data, Manager manager) {
        setCode(0);
        setEmail(data[0]);
        setAddress(data[1]);
        setLocation(data[2]);
        setPhone(data[3]);
        setManager(manager);
        setActive(true);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        if (code < 0)
            throw new IllegalArgumentException("Invalid code");
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email.length() > 40)
            throw new IllegalArgumentException("Email must have at most 40 characters");
        if (!email.matches("^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})"))
            throw new IllegalArgumentException("Invalid email format");
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address.length() > 100)
            throw new IllegalArgumentException("Address must have at most 100 characters");
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location.length() > 30)
            throw new IllegalArgumentException("Location must have at most 30 characters");
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone.length() > 30)
            throw new IllegalArgumentException("Phone must have at most 30 characters");
        this.phone = phone;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
           return "{code=" + code + ", email=" + email + ", address=" + address + ", location=" + location
                    + ", phone=" + phone + ", manager=" + manager + ", active=" + active + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Store other = (Store) obj;
        return code == other.code;
    }

    @Override
    public int hashCode() {
        return code;
    }

}