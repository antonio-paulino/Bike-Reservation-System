package isel.sisinf.jpa;

import isel.sisinf.jpa.Bike.IClassicBikeRepository;
import isel.sisinf.jpa.Bike.IElectricBikeRepository;
import isel.sisinf.jpa.GPS.IGPSRepository;
import isel.sisinf.jpa.Person.IClientRepository;
import isel.sisinf.jpa.Person.IManagerRepository;
import isel.sisinf.jpa.Reservation.IReservationRepository;
import isel.sisinf.jpa.Store.IStoreRepository;
import isel.sisinf.model.Bike.IClassicBike;
import isel.sisinf.model.Bike.IElectricBike;
import isel.sisinf.model.GPS.IGPS;
import isel.sisinf.model.Person.IClient;
import isel.sisinf.model.Person.IManager;
import isel.sisinf.model.Reservation.IReservation;
import isel.sisinf.model.Reservation.IReservationID;
import isel.sisinf.model.Store.IStore;
import jakarta.persistence.*;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.Session;

import java.sql.Timestamp;
import java.util.List;

public class JPAContext implements IContext {

    private final EntityManagerFactory _emf;

    private final EntityManager _em;

    private EntityTransaction _tx;

    private int _txcount;

    // JPA Context and transaction management

    public JPAContext() {
        this("go-cycle");
    }

    public JPAContext(String persistenceUnitName) {
        super();
        _emf = jakarta.persistence.Persistence.createEntityManagerFactory(persistenceUnitName);
        _em = _emf.createEntityManager();
    }

    @Override
    public void beginTransaction() {
        if(_tx == null)
        {
            _tx = _em.getTransaction();
            _tx.begin();
            _txcount=0;
        }
        ++_txcount;
    }

    @Override
    public void beginTransaction(IsolationLevel isolationLevel)
    {
        beginTransaction();
        Session session =_em.unwrap(Session.class);
        DatabaseLogin databaseLogin = (DatabaseLogin) session.getDatasourceLogin();

        int isolation = DatabaseLogin.TRANSACTION_READ_COMMITTED;
        if(isolationLevel == IsolationLevel.READ_UNCOMMITTED)
            isolation = DatabaseLogin.TRANSACTION_READ_UNCOMMITTED;
        else if(isolationLevel == IsolationLevel.REPEATABLE_READ)
            isolation = DatabaseLogin.TRANSACTION_REPEATABLE_READ;
        else if(isolationLevel == IsolationLevel.SERIALIZABLE)
            isolation = DatabaseLogin.TRANSACTION_SERIALIZABLE;

        databaseLogin.setTransactionIsolation(isolation);
    }

    @Override
    public void commit() {

        --_txcount;
        if(_txcount==0 && _tx != null)
        {
            _em.flush();
            _tx.commit();
            _tx = null;
        }
    }

    @Override
    public void flush() {
        _em.flush();
    }

    @Override
    public void clear() {
        _em.clear();
    }

    @Override
    public void persist(Object entity) {
        _em.persist(entity);
    }

    @Override
    public void rollback() {
        --_txcount;
        if(_txcount==0 && _tx != null)
        {
            _em.flush();
            _tx.rollback();
            _tx = null;
        }
    }

    @Override
    public void close() {
        if(_tx != null)
            _tx.rollback();
        _em.close();
        _emf.close();
    }

    /**
     * @param entity Object in which a lock will be applied
     * @param lockMode The type of the lock
     */
    @Override
    public void lock(Object entity, LockModeType lockMode) {
        _em.lock(entity, lockMode);
    }

    // Helpers

    protected List helperQueryImpl(String jpql, Object... params)
    {
        Query q = _em.createQuery(jpql);

        for(int i = 0; i < params.length; ++i)
            q.setParameter(i+1, params[i]);

        return q.getResultList();
    }

    protected Object helperCreateImpl(Object entity)
    {
        beginTransaction();
        _em.persist(entity);
        commit();
        return entity;
    }

    protected Object helperUpdateImpl(Object entity)
    {
        beginTransaction();
        _em.merge(entity);
        commit();
        return entity;
    }

    protected void helperDeleteImpl(Object entity)
    {
        beginTransaction();
        Object managedEntity = _em.merge(entity);
        _em.remove(managedEntity);
        commit();
    }

    public <T> T getResultHelper(TypedQuery<T> query) {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Repositories

    IGPSRepository gpsRepository = new GPSRepository();

    IClientRepository clientRepository = new ClientRepository();

    IManagerRepository managerRepository = new ManagerRepository();

    IStoreRepository storeRepository = new StoreRepository();

    IElectricBikeRepository electricBikeRepository = new ElectricBikeRepository();

    IClassicBikeRepository classicBikeRepository = new ClassicBikeRepository();

    IReservationRepository reservationRepository = new ReservationRepository();

    @Override
    public IGPSRepository getGPSRepository() {
        return gpsRepository;
    }

    @Override
    public IClientRepository getClientRepository() {
        return clientRepository;
    }

    @Override
    public IManagerRepository getManagerRepository() {
        return managerRepository;
    }

    public IStoreRepository getStoreRepository() {return storeRepository; }

    public IElectricBikeRepository getElectricBikeRepository() {return electricBikeRepository; }

    public IClassicBikeRepository getClassicBikeRepository() {return classicBikeRepository; }

    public IReservationRepository getReservationRepository() {return reservationRepository;}

    public boolean isBikeAvailable(int bikeId, Timestamp start_date, Timestamp end_date) {
        StoredProcedureQuery query = _em.createNamedStoredProcedureQuery("isbikeavailable");
        query.setParameter(1, bikeId);
        query.setParameter(2, start_date);
        query.setParameter(3, end_date);
        query.execute();
        return (Boolean) query.getOutputParameterValue(4);
    }

    @Override
    public boolean personHasReservation(int personID, Timestamp start_date) {
        StoredProcedureQuery query = _em.createNamedStoredProcedureQuery("personhasreservation");
        query.setParameter(1, personID);
        query.setParameter(2, start_date);
        query.execute();
        return (Boolean) query.getOutputParameterValue(3);
    }

    protected class GPSRepository implements IGPSRepository {

        @Override
        public IGPS create(IGPS entity) {
            return (IGPS) helperCreateImpl(entity);
        }

        @Override
        public IGPS update(IGPS entity) {
            return (IGPS) helperUpdateImpl(entity);
        }

        @Override
        public void delete(IGPS entity) {
            helperDeleteImpl(entity);
        }

        @Override
        public IGPS findByKey(Integer id) {
            TypedQuery<IGPS> query = _em.createNamedQuery("GPS.findBySerialNumber", IGPS.class)
                    .setParameter("serialNumber", id);
            return getResultHelper(query);
        }

        @Override
        public List<IGPS> findAll() {
            return _em.createNamedQuery("GPS.findAll", IGPS.class).getResultList();
        }

        @Override
        public List<IGPS> find(String query, Object... args) {
            return helperQueryImpl(query, args);
        }

    }

    protected class ClientRepository implements IClientRepository {

        @Override
        public IClient create(IClient entity) {
            return (IClient) helperCreateImpl(entity);
        }

        @Override
        public IClient update(IClient entity) {
            return (IClient) helperUpdateImpl(entity);
        }

        @Override
        public void delete(IClient entity) {
            helperDeleteImpl(entity);
        }

        @Override
        public IClient findByKey(Integer id) {
            TypedQuery<IClient> query = _em.createNamedQuery("Client.findByKey", IClient.class)
                    .setParameter("id", id);
            return getResultHelper(query);
        }

        @Override
        public List<IClient> findAll() {
            return _em.createNamedQuery("Client.findAll", IClient.class).getResultList();
        }

        @Override
        public List<IClient> find(String query, Object... args) {
            return helperQueryImpl(query, args);
        }

    }

    protected class ManagerRepository implements IManagerRepository {

        @Override
        public IManager create(IManager entity) {
            return (IManager) helperCreateImpl(entity);
        }

        @Override
        public IManager update(IManager entity) {
            return (IManager) helperUpdateImpl(entity);
        }

        @Override
        public void delete(IManager entity) {
            helperDeleteImpl(entity);
        }

        @Override
        public IManager findByKey(Integer id) {
            TypedQuery<IManager> query = _em.createNamedQuery("Manager.findByKey", IManager.class)
                    .setParameter("id", id);
            return getResultHelper(query);
        }

        @Override
        public List<IManager> findAll() {
            return _em.createNamedQuery("Manager.findAll", IManager.class).getResultList();
        }

        @Override
        public List<IManager> find(String query, Object... args) {
            return helperQueryImpl(query, args);
        }

        @Override
        public void deactivate(IManager manager) {
            manager.setActive(false);
            helperUpdateImpl(manager);
        }

    }

    protected class StoreRepository implements IStoreRepository {


        @Override
        public IStore create(IStore entity) {
            return (IStore) helperCreateImpl(entity);
        }

        @Override
        public IStore update(IStore entity) {
            return (IStore) helperUpdateImpl(entity);
        }

        @Override
        public void delete(IStore entity) {
            helperDeleteImpl(entity);
        }

        @Override
        public IStore findByKey(Integer id) {
            TypedQuery<IStore> query = _em.createNamedQuery("Store.findByKey", IStore.class)
                    .setParameter("code", id);
            return getResultHelper(query);
        }

        @Override
        public List<IStore> findAll() {
            return _em.createNamedQuery("Store.findAll", IStore.class).getResultList();
        }

        @Override
        public List<IStore> find(String query, Object... args) {
            return helperQueryImpl(query, args);
        }

        @Override
        public void deactivate(IStore store) {
            store.setActive(false);
            helperUpdateImpl(store);
        }

    }

    protected class ElectricBikeRepository implements IElectricBikeRepository {

        @Override
        public IElectricBike create(IElectricBike entity) {
            return (IElectricBike) helperCreateImpl(entity);
        }

        @Override
        public IElectricBike update(IElectricBike entity) {
            return (IElectricBike) helperUpdateImpl(entity);
        }

        @Override
        public void delete(IElectricBike entity) {
            helperDeleteImpl(entity);
        }

        @Override
        public IElectricBike findByKey(Integer id) {
            TypedQuery<IElectricBike> query = _em.createNamedQuery("ElectricBike.findByKey", IElectricBike.class)
                    .setParameter("id", id);
            return getResultHelper(query);
        }

        @Override
        public List<IElectricBike> findAll() {
            return _em.createNamedQuery("ElectricBike.findAll", IElectricBike.class).getResultList();
        }

        @Override
        public List<IElectricBike> find(String query, Object... args) {
            return helperQueryImpl(query, args);
        }

        @Override
        public void deactivate(IElectricBike bike) {
            bike.setActive(false);
            helperUpdateImpl(bike);
        }

    }

    protected class ClassicBikeRepository implements IClassicBikeRepository {

        @Override
        public IClassicBike create(IClassicBike entity) {
            return (IClassicBike) helperCreateImpl(entity);
        }

        @Override
        public IClassicBike update(IClassicBike entity) {
            return (IClassicBike) helperUpdateImpl(entity);
        }

        @Override
        public void delete(IClassicBike entity) {
            helperDeleteImpl(entity);
        }

        @Override
        public IClassicBike findByKey(Integer id) {
            TypedQuery<IClassicBike> query = _em.createNamedQuery("ClassicBike.findByKey", IClassicBike.class)
                    .setParameter("id", id);
            return getResultHelper(query);
        }

        @Override
        public List<IClassicBike> findAll() {
            return _em.createNamedQuery("ClassicBike.findAll", IClassicBike.class).getResultList();
        }

        @Override
        public List<IClassicBike> find(String query, Object... args) {
            return helperQueryImpl(query, args);
        }

        @Override
        public void deactivate(IClassicBike bike) {
            bike.setActive(false);
            helperUpdateImpl(bike);
        }

    }
    protected class ReservationRepository implements IReservationRepository {

        @Override
        public IReservation findByKey(IReservationID id) {
            TypedQuery<IReservation> query = _em.createNamedQuery("Reservation.findByKey", IReservation.class)
                    .setParameter("id", id);
            return getResultHelper(query);
        }

        @Override
        public List<IReservation> findAll() {
            return _em.createNamedQuery("Reservation.findAll", IReservation.class).getResultList();
        }

        @Override
        public List<IReservation> find(String query, Object... args) {
            return helperQueryImpl(query, args);
        }

        @Override
        public void deactivate(IReservation reservation) {
            beginTransaction();
            reservation.setActive(false);
            helperUpdateImpl(reservation);
            reservation.getBike().setState("livre");
            helperUpdateImpl(reservation.getBike());
            commit();
        }

        @Override
        public boolean reservationsAvailable() {
            StoredProcedureQuery query = _em.createNamedStoredProcedureQuery("reservationsavailable");
            query.execute();
            return (Boolean) query.getOutputParameterValue(1);
        }

        @Override
        public IReservation create(IReservation entity) {
            beginTransaction();
            StoredProcedureQuery query = _em.createNamedStoredProcedureQuery("createreservation");
            query.setParameter(1, entity.getStoreEntity().getCode());
            query.setParameter(2, entity.getBike().getId());
            query.setParameter(3, entity.getClient().getId());
            query.setParameter(4, entity.getStart_date());
            query.setParameter(5, entity.getEnd_date());
            query.setParameter(6, entity.getPrice());
            query.execute();
            entity.getId().setReservation_no((Integer) query.getOutputParameterValue(7));
            _em.refresh(entity.getBike());
            IReservation reservation = findByKey(entity.getId());
            commit();
            return reservation;
        }

        @Override
        public IReservation update(IReservation entity) {
            return (IReservation) helperUpdateImpl(entity);
        }

        @Override
        public void delete(IReservation entity) {
            beginTransaction();
            helperDeleteImpl(entity);
            _em.flush();
            _em.refresh(entity.getBike());
            commit();
        }

    }


}
