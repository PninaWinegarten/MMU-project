package com.hit.dm;

import java.io.Serializable;
import java.util.Objects;

//Defines the data model.
public class DataModel<T> implements Serializable {

    private static final Long serialVersionUID = -616017516154926601L;

    private long dataModelId;
    private T content;

    public DataModel(){}

    public DataModel(long id, T content) {
        this.dataModelId = id;
        this.content = content;
    }

    public Long getDataModelId() {
        return dataModelId;
    }

    public void setDataModelId(long dataModelId) {
        this.dataModelId = dataModelId;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataModel<T> dataModel = (DataModel<T>) o;
        return dataModelId == dataModel.dataModelId && Objects.equals(content, dataModel.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataModelId, content);
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "dataModelId=" + dataModelId +
                ", content=" + content +
                '}';
    }

}
