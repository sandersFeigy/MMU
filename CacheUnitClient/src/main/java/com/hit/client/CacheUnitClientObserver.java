package main.java.com.hit.client;

import main.java.com.hit.view.CacheUnitView;

import java.beans.PropertyChangeEvent;

/**
 * This class implements separate layer between GUI event and cache unit client.
 */
public class CacheUnitClientObserver extends java.lang.Object implements java.beans.PropertyChangeListener {
    private CacheUnitClient cacheUnitClient;
    private String data;
    private String status;
    private CacheUnitView cacheUnitView;

    public CacheUnitClientObserver() {
        cacheUnitClient = new CacheUnitClient();
        status = "failed";
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        cacheUnitView = (CacheUnitView) evt.getSource();
        data = cacheUnitClient.send((String) evt.getNewValue());
        String[] array = null;
        if (data != "failed") {
            if (data.contains("[[")) {
                int i = data.substring(0, data.length() - 1).lastIndexOf("]");
                String arr = data.substring(1, i + 1);
                array = data.substring(i + 2, data.length() - 1).split(",");
            } else {
                int i = data.substring(0, data.length() - 1).indexOf(",");
                array = data.substring(i + 1, data.length() - 1).split(",");
            }
            status = "succeed";
            cacheUnitView.setCapacity(array[0]);
            cacheUnitView.setAlgorithm(array[1]);
            cacheUnitView.setTotalDataModels(array[2]);
            cacheUnitView.setSwapsDataModels(array[3]);
            cacheUnitView.setTotalRequest(array[4]);
        } else {
            status = "failed";
        }
        cacheUnitView.updateUIData(status);
    }
}
;