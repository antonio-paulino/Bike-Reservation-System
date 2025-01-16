package isel.sisinf.model.Person;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "Person")
@DiscriminatorValue("G")
@NamedQuery(name = "Manager.findByKey",
    query = "SELECT m FROM Person m WHERE m.id = :id AND m.atrdisc = 'G' and m.active = true"
)
@NamedQuery(name = "Manager.findAll",
    query = "SELECT m FROM Person m WHERE m.atrdisc = 'G' and m.active = true"
)
public class Manager extends Person implements IManager {
    public Manager() {
    }

    public Manager(int id, String name, String address, String email, String phone, String identification, String nationality) {
        super(id, name, address, email, phone, identification, nationality, 'G');
    }

    public Manager(String[] data) {
        super(0, data[0], data[1], data[2], data[3], data[4], data[5], 'G');
    }
}

