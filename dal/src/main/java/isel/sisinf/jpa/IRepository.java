package isel.sisinf.jpa;

public interface IRepository<T, TCollection, TKey> {
    T findByKey(TKey id);

    TCollection findAll();

    TCollection find(String query, Object... args);

}
