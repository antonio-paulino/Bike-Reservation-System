package isel.sisinf.jpa.Bike;

import isel.sisinf.jpa.IRepository;
import isel.sisinf.model.Bike.IClassicBike;

import java.util.Collection;
import java.util.List;

public interface IClassicBikeRepository extends IRepository<IClassicBike, List<IClassicBike>, Integer>, IClassicBikeDataMapper {
    void deactivate(IClassicBike bike);
}
