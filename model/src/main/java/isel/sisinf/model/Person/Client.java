package isel.sisinf.model.Person;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "Person")
@DiscriminatorValue("C")
@NamedQuery(name = "Client.findByKey",
    query = "SELECT c FROM Person c WHERE c.id = :id AND c.atrdisc = 'C'"
)
@NamedQuery(name = "Client.findAll",
    query = "SELECT c FROM Person c WHERE c.atrdisc = 'C' and c.active = true"
)
public class Client extends Person implements IClient {
    public Client() {
    }

    public Client(int id, String name, String address, String email, String phone, String identification, String nationality) {
        super(id, name, address, email, phone, identification, nationality, 'C');
    }

    public Client(String[] data) {
        super(0, data[0], data[1], data[2], data[3], data[4], data[5], 'C');
    }

}
