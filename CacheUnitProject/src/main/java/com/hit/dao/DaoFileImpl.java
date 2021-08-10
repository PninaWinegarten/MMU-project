
package com.hit.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import java.io.*;
import java.util.ArrayList;

public class DaoFileImpl<T> implements IDao<Long,DataModel<T>> {

    private static Gson gson = new Gson();
    private int capacity;
    private String filePath;

    public DaoFileImpl(){}

    //Constructor(filePath)
    public DaoFileImpl(String filePath) {
        this.capacity= 100;
        this.filePath = filePath;
    }

    //Constructor(filePath, capacity)
    public DaoFileImpl(String filePath, int capacity ) {
        this.capacity = capacity;
        this.filePath = filePath;
    }

    //Get an array of data and write it to the datasource.
    private void writeToFile(ArrayList<DataModel<T>> dataArr){
        try {
            Writer writer = new FileWriter(filePath);
            gson.toJson(dataArr, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Read the array from the datasource and return it.
    private ArrayList<DataModel<T>> readFromFile() throws IOException {
        Reader reader = new FileReader(filePath);
        ArrayList<DataModel<T>> data = gson.fromJson(reader, new TypeToken<ArrayList<DataModel<T>>>(){}.getType());
        reader.close();
        return data;
    }

    @Override
    //Saves a given entity.
    public void save(DataModel<T> entity) throws IOException {
        ArrayList<DataModel<T>> dataArray = this.readFromFile();// Read the data array from the datasource.

        if(dataArray != null){
            if(dataArray.size() == this.capacity){
                return;
            }
            //Check if id exists.
            for (DataModel<T> tDataModel : dataArray) {
                if (tDataModel.getDataModelId().equals(entity.getDataModelId())) {
                    System.out.println("Key already exist");
                    return;
                }
            }
        }
        dataArray.add(entity);// Add the new entity.
        writeToFile(dataArray);// Write the updated data array to the datasource.
    }

    @Override
    //Deletes a given entity.
    public void delete(DataModel<T> entity) throws IOException {
        if(entity == null){
            throw new IllegalArgumentException();
        }

        ArrayList<DataModel<T>> dataArray = this.readFromFile();// Read the data array from the datasource.
        if(dataArray == null){return;}
        dataArray.remove(entity);// Remove tha given entry.
        writeToFile(dataArray);// Write the updated data array to the datasource.
    }

    @Override
    //Retrieves an entity by its id, Return the entity with the given id or null if none found.
    public DataModel<T> find(Long id) throws IllegalArgumentException, IOException {
        if(id == null){
            throw new IllegalArgumentException();
        }
        ArrayList<DataModel<T>> jsonArray = readFromFile();// Read the data array from the datasource.
        //Search for the entity.
        for (DataModel<T> tDataModel : jsonArray) {
            if (tDataModel.getDataModelId().equals(id)) {
                return tDataModel; //Return the entity if found.
            }
        }
        return null;// If the entity wasn't found return null.
    }
}
