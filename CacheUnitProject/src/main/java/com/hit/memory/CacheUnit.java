package com.hit.memory;

import com.hit.algorithm.IAlgoCache;
import com.hit.dm.DataModel;

public class CacheUnit<T> {

    private final IAlgoCache<Long, DataModel<T>> algo;

    // Constructor
    public CacheUnit(IAlgoCache<Long, DataModel<T>> algo){
        this.algo = algo;
    }

    // Get an array of ids and return the data models with theses ids.
    public DataModel<T>[] getDataModels(Long[] ids){
        DataModel<T>[] dataArr = new DataModel[ids.length];
        for (int i = 0; i < ids.length; i++) {
            dataArr[i] = algo.getElement(ids[i]);
        }
        return dataArr;
    }

    // Get an array of data models, put them in the cache and return the data models that where deleted by this action.
    public DataModel<T>[] putDataModels(DataModel<T>[] datamodels){
        DataModel<T>[] dataArr = new DataModel[datamodels.length];
        for (int i = 0; i < datamodels.length; i++) {
            if (datamodels[i] != null){
                dataArr[i] = algo.putElement(datamodels[i].getDataModelId(), datamodels[i]);
            }
        }
        return dataArr;
    }

    // Get an array of ids and delete the data models with theses ids.
    public void removeDataModels(Long[] ids){
        for (Long id : ids) {
            algo.removeElement(id);
        }
    }

}
