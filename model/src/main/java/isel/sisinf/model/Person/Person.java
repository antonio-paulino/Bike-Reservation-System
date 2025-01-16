package isel.sisinf.model.Person;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@DiscriminatorColumn(name = "atrdisc")
@NamedStoredProcedureQuery(
        name = "personhasreservation",
        procedureName = "personhasreservation",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Timestamp.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = Boolean.class),
        }
)
public abstract class Person implements IPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, length = 150)
    private String address;

    @Column(nullable = false, length = 40)
    private String email;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 12)
    private String identification;

    @Column(nullable = false, length = 20)
    private String nationality;

    @Column(nullable = false, length = 2)
    private char atrdisc;

    @Column(nullable = false)
    private boolean active;

    public Person() {
    }

    public Person(int id, String name, String address, String email, String phone, String identification, String nationality, char atrdisc) {
        setId(id);
        setName(name);
        setAddress(address);
        setEmail(email);
        setPhone(phone);
        setIdentification(identification);
        setNationality(nationality);
        setAtrdisc(atrdisc);
        setActive(true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("Invalid id");
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.length() > 40)
            throw new IllegalArgumentException("Name must have at most 40 characters");
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address.length() > 150)
            throw new IllegalArgumentException("Address must have at most 150 characters");
        this.address = address;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone.length() > 20)
            throw new IllegalArgumentException("Phone must have at most 20 characters");
        this.phone = phone;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        if (identification.length() > 12)
            throw new IllegalArgumentException("Identification must have at most 12 characters");
        this.identification = identification;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        if(nationality.length() > 20)
            throw new IllegalArgumentException("Nationality must have at most 20 characters");
        this.nationality = nationality;
    }

    public char getAtrdisc() {
        return atrdisc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private void setAtrdisc(char atrdisc) {
        if (atrdisc != 'G' && atrdisc != 'C')
            throw new IllegalArgumentException("Person must be a manager or a client (G or C)");
        this.atrdisc = atrdisc;
    }

    @Override
    public String toString() {
        return "{address=" + address + ", active=" + active + ", atrdisc=" + atrdisc + ", email=" + email
                + ", id=" + id + ", identification=" + identification + ", name=" + name + "}";
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
