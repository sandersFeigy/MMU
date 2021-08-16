package main.java.com.hit.services;

import main.java.com.hit.dm.DataModel;

import java.util.ArrayList;

/**
 * This class implements separation layer between CacheUnitService class and HandleRequest class
 * <T> – Generic type of the body requests.
 */
public class CacheUnitController<T> extends java.lang.Object {
    private CacheUnitService cacheUnitService;

    public CacheUnitController() {
        cacheUnitService = new CacheUnitService();
    }

    public ArrayList<Object> update(DataModel<T>[] dataModels) {
        return cacheUnitService.update(dataModels);
    }

    public ArrayList<Object> delete(DataModel<T>[] dataModels) {
        return cacheUnitService.delete(dataModels);
    }

    public ArrayList<Object> get(DataModel<T>[] dataModels) {
        return cacheUnitService.get(dataModels);
    }

    public void onServerShutdown(){
       cacheUnitService.onServerShutdown();
    }
}
