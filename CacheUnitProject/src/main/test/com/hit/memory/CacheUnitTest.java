package com.hit.memory;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dm.DataModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CacheUnitTest {
    private final int capacity = 8;
    private final CacheUnit<String> cacheUnit;
    private final DaoFileImpl<String> DAO = new DaoFileImpl<String>("src\\main\\resources\\datasource.txt");

    public CacheUnitTest() {
        IAlgoCache LRUAlgorithm = new LRUAlgoCacheImpl(capacity);
        this.cacheUnit = new CacheUnit<>(LRUAlgorithm);
    }

    @Test
    void test() throws IOException {
        //test cacheUnit
        DataModel<String>[] dataModelArray = new DataModel[capacity];

        for (int i = 0; i < capacity; i++) {
            dataModelArray[i] = new DataModel<>(i, "Page " + i);
        }

        cacheUnit.putDataModels(dataModelArray);
        DataModel<String>[] shortArray = new DataModel[]{new DataModel<>(0L, "Page 0"), new DataModel<>(1L, "Page 1")};
        Long[] shortArrayIds1 = {0L, 1L};
        assertArrayEquals(shortArray, cacheUnit.getDataModels(shortArrayIds1));
        cacheUnit.removeDataModels(shortArrayIds1);

        Long[] shortArrayIds2 = {0L, 2L};
        assertNull(cacheUnit.getDataModels(shortArrayIds2)[0]);
        assertNotNull(cacheUnit.getDataModels(shortArrayIds2)[1]);

        DataModel<String>[] dataModelArray2 = new DataModel[capacity];

        for (int i = capacity; i < capacity*2; i++) {
            dataModelArray2[i-capacity] = new DataModel<>(i, "Page " + i);
        }

        DataModel<String>[] returnArray = cacheUnit.putDataModels(dataModelArray2);
        assertNull(returnArray[1]);
        assertNotNull(returnArray[2]);

        //test DAO
        DataModel<String> data1= new DataModel<>(1L, "page 1");
        DataModel<String> data2= new DataModel<>(2L, "page 2");
        DataModel<String> data3= new DataModel<>(3L, "page 3");
        DataModel<String> data4= new DataModel<>(4L, "page 4");
        DataModel<String> data5= new DataModel<>(5L, "page 5");
        DAO.save(data1);
        DAO.save(data2);
        DAO.save(data3);
        DAO.save(data4);
        DAO.save(data5);
        assertNotNull(DAO.find(1L));
        assertNotNull(DAO.find(5L));
        assertNull(DAO.find(7L));
        try {
            DAO.delete(data2);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        assertNull(DAO.find(2L));
        DAO.save(data1);//check if it prints "ambiguous key"
    }


}