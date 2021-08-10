package com.hit.services;

import com.hit.dm.DataModel;

public class CacheUnitController<T> {

    private CacheUnitService<T> cacheUnitService;

    public String getStatistics() {
        return cacheUnitService.getStatistics();
    }

    public CacheUnitController(String algoName, Integer capacity) {
        cacheUnitService = new CacheUnitService<T>(algoName, capacity);
    }

    public boolean update(DataModel<T>[] dataModels){
        return cacheUnitService.update(dataModels);
    }

    public boolean delete(DataModel<T>[] dataModels){
        return cacheUnitService.delete(dataModels);
    }

    public DataModel<T>[] get(DataModel<T>[] dataModels){
        return cacheUnitService.get(dataModels);
    }

}

