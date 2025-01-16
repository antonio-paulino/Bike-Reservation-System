package isel.sisinf.jpa.Person;

import isel.sisinf.jpa.IRepository;
import isel.sisinf.model.Person.IManager;

import java.util.Collection;
import java.util.List;

public interface IManagerRepository extends IRepository<IManager, List<IManager>, Integer>, IManagerDataMapper {
    void deactivate(IManager manager);
}

