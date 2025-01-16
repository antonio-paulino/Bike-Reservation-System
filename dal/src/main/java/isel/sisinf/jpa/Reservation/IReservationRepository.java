package isel.sisinf.jpa.Reservation;

import isel.sisinf.jpa.IRepository;
import isel.sisinf.model.Reservation.IReservation;
import isel.sisinf.model.Reservation.IReservationID;

import java.util.List;

public interface IReservationRepository extends IRepository<IReservation, List<IReservation>, IReservationID>, IReservationDataMapper {
    void deactivate(IReservation reservation);

    boolean reservationsAvailable();

}
