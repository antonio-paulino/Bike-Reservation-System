package isel.sisinf.jpa.Bike;

import isel.sisinf.jpa.IRepository;
import isel.sisinf.model.Bike.IElectricBike;
import java.util.List;

public interface IElectricBikeRepository extends IRepository<IElectricBike, List<IElectricBike>, Integer>, IElectricBikeDataMapper {
    void deactivate(IElectricBike bike);
}
