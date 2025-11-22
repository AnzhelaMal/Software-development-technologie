package ua.kpi.ia33.shellweb.repo;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
}
