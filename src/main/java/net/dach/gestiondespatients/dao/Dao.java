package net.dach.gestiondespatients.dao;

import java.util.List;

public interface Dao<T> {
    List<T> findAll() throws Exception;
    T findById(int id) throws Exception;
    void save(T t) throws Exception;
    void update(T t) throws Exception;
    void delete(int id) throws Exception;
}