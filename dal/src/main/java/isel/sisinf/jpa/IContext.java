package isel.sisinf.jpa;
import isel.sisinf.jpa.Bike.IClassicBikeRepository;
import isel.sisinf.jpa.Bike.IElectricBikeRepository;
import isel.sisinf.jpa.GPS.IGPSRepository;
import isel.sisinf.jpa.Person.IClientRepository;
import isel.sisinf.jpa.Person.IManagerRepository;
import isel.sisinf.jpa.Store.IStoreRepository;
import jakarta.persistence.LockModeType;

import java.sql.Timestamp;

public interface IContext extends AutoCloseable {

    enum IsolationLevel {READ_UNCOMMITTED, READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE }
    void beginTransaction();
    void beginTransaction(IsolationLevel isolationLevel);
    void commit();
    void flush();
    void clear();
    void persist(Object entity);

    void rollback();

    void close();

    void lock(Object entity, LockModeType lockMode);

    IGPSRepository getGPSRepository();

    IClientRepository getClientRepository();

    IManagerRepository getManagerRepository();
    IStoreRepository getStoreRepository();

    IClassicBikeRepository getClassicBikeRepository();

    IElectricBikeRepository getElectricBikeRepository();

    boolean isBikeAvailable(int bikeID, Timestamp start_date, Timestamp end_date);

    boolean personHasReservation(int personID, Timestamp start_date);

}