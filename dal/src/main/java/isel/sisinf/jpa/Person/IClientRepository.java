package isel.sisinf.jpa.Person;

import isel.sisinf.jpa.IRepository;
import isel.sisinf.model.Person.IClient;

import java.util.Collection;
import java.util.List;

public interface IClientRepository extends IRepository<IClient, List<IClient>, Integer>, IClientDataMapper {
}
