
package com.hit.services;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.MFUAlgoCacheImpl;
import com.hit.algorithm.RandomReplacementAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;
import com.hit.server.Server;

import java.io.IOException;

public class CacheUnitService<T> {

    private CacheUnit cacheUnit;
    IDao<Long, DataModel<T>> dataAccessObject;
    public static Integer numberOfRequests = 0;
    public static Integer numberOfSwaps = 0;
    private Integer capacity = 0;
    private String algoType = null;

    // Constructor.
    public CacheUnitService(String algoName, Integer capacity) {
        this.capacity = capacity;
        IAlgoCache<Long, DataModel<String>> algo = null;
        dataAccessObject = new DaoFileImpl<>("src\\main\\resources\\datasource.txt");
        switch(algoName) {
            case "LRU":
            {
                algo = new LRUAlgoCacheImpl<>(capacity);
                algoType = "LRU";
                break;
            }
            case "MFU":
            {
                algo = new MFUAlgoCacheImpl<>(capacity);
                algoType = "MFU";
                break;
            }
            case "RANDOM":
            {
                algo = new RandomReplacementAlgoCacheImpl<>(capacity);
                algoType = "Random";
                break;
            }
        }

        cacheUnit = new CacheUnit<>(algo);
    }

    public String getStatistics() {
        String values;
        values = new String(capacity.toString()+"-"+algoType+"-"+Server.numberOfRequests.toString()+"-"+numberOfRequests.toString()+"-"+numberOfSwaps.toString());
        return values;
    }


    public boolean delete(DataModel<T>[] dataModels){
        numberOfRequests++;
        int length = dataModels.length;
        Long[] ids = new Long[length];
        for (int i=0; i < length; i++) {
            if (dataModels[i] != null) {
                try {
                    dataAccessObject.delete(dataModels[i]);
                } catch (IOException | IllegalArgumentException e) {
                    return false;
                }
            }
            ids[i] = dataModels[i].getDataModelId();
        }
        cacheUnit.removeDataModels(ids);
        return true;
    }


public DataModel<T>[] get(DataModel<T>[] dataModels){
    numberOfRequests++;
    int length = dataModels.length;
    Long[] ids = new Long[length];
    for (int i=0; i < length; i++) {
        ids[i] = dataModels[i].getDataModelId();
    }
    DataModel<T>[] searchedElements = cacheUnit.getDataModels(ids);
    for (int i=0; i < length; i++){
        if (searchedElements[i] == null){
            try {
                searchedElements[i] = dataAccessObject.find(ids[i]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    cacheUnit.putDataModels(searchedElements);
    return searchedElements;
}


    public boolean update(DataModel<T>[] dataModels){
        numberOfRequests++;
        DataModel<T>[] removedElements = cacheUnit.putDataModels(dataModels);
            for (DataModel<T> removedElement : removedElements) {
                if (removedElement != null) {
                    try {
                        dataAccessObject.save(removedElement);
                        numberOfSwaps++;
                    } catch (IOException e) {
                        return false;
                    }
                }
            }
            return true;
        }

    private Long[] getIds(DataModel<T>[] dataModels) {
        Long[] ids = new Long[dataModels.length];

        for (int i=0; i<dataModels.length; ++i) {
            ids[i] = dataModels[i].getDataModelId();
        }

        return ids;
    }
}
