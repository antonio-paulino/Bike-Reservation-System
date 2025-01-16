package isel.sisinf.jpa.GPS;

import isel.sisinf.jpa.IRepository;
import isel.sisinf.model.GPS.IGPS;

import java.util.Collection;
import java.util.List;

public interface IGPSRepository extends IRepository<IGPS, List<IGPS>, Integer>, IGPSDataMapper {

}
