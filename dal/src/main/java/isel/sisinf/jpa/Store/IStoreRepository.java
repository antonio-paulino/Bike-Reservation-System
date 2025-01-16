package isel.sisinf.jpa.Store;

import isel.sisinf.jpa.IRepository;
import isel.sisinf.model.Store.IStore;

import java.util.List;

public interface IStoreRepository extends IRepository<IStore, List<IStore>, Integer>, IStoreDataMapper {
    void deactivate(IStore store);

}
