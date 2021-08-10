package com.hit.client;

import com.hit.view.CacheUnitView;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CacheUnitClientObserver implements PropertyChangeListener {

    private CacheUnitClient cacheUnitClient;

    public CacheUnitClientObserver() {
        cacheUnitClient = new CacheUnitClient();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String result;
        CacheUnitView updateUIData = (CacheUnitView) evt.getSource();
        result = cacheUnitClient.send(evt.getNewValue().toString());
        updateUIData.updateUIData(result);
    }
}
