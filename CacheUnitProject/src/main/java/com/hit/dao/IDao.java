package com.hit.dao;

import java.io.IOException;
import java.io.Serializable;

public interface IDao<ID extends Serializable,T> {

    public void save(T entity) throws IOException; // Saves a given entity.
    public void delete(T entity) throws IOException; // Deletes a given entity.
    public T find(ID id) throws IllegalArgumentException, IOException; // Retrieves an entity by its id.
}
