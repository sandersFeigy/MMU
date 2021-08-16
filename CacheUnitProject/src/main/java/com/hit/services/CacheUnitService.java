package main.java.com.hit.services;

import main.java.com.hit.algorithm.LRUAlgoCacheImpl;
import main.java.com.hit.dao.DaoFileImpl;
import main.java.com.hit.dm.DataModel;
import main.java.com.hit.memory.CacheUnit;

import java.util.ArrayList;
import java.util.Map;

/**
* This class responsible for coordinating the information between the RAM and the hard disk in any time.
 * Type parameters:
 * <T> – Generic type of the content in the ram and the hard disk.
 */
public class CacheUnitService<T> extends java.lang.Object {
    private final CacheUnit cacheUnit;
    private LRUAlgoCacheImpl<Long, DataModel<T>> lruAlgoCache;
    private DaoFileImpl daoFile;
    private int totalRequest;
    private int totalDataModels;
    private int swapsDataModels;
    private ArrayList values;


    public CacheUnitService() {
        try {
            daoFile = new DaoFileImpl("src/main/resources/datasource.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        lruAlgoCache = new LRUAlgoCacheImpl<>(10);
        cacheUnit = new CacheUnit(lruAlgoCache);
        values = new ArrayList();
        totalDataModels = 0;
        swapsDataModels = 0;
        totalRequest = 0;
    }

    //The function updates the date.
    public boolean basicUpdate(DataModel<T>[] dataModels) {
        totalRequest++;
        totalDataModels += dataModels.length;
        try {
            DataModel[] dataModel = cacheUnit.putDataModels(dataModels);
            for (int i = 0; i < dataModel.length; i++) {
                if (dataModel[i] != null) {
                    daoFile.save(dataModel[i]);
                    swapsDataModels++;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //The function delete the date.
    public boolean basicDelete(DataModel<T>[] dataModels) {
        totalRequest++;
        totalDataModels += dataModels.length;
        Long[] ids = new Long[dataModels.length];
        for (int i = 0; i < dataModels.length; i++) {
            ids[i] = dataModels[i].getId();
            daoFile.delete(dataModels[i]);
        }
        try {
            cacheUnit.removeDataModels(ids);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //The function get the date.
    public DataModel<T>[] basicGet(DataModel<T>[] dataModels) {
        totalRequest++;
        totalDataModels += dataModels.length;
        Long[] ids = new Long[dataModels.length];
        for (int i = 0; i < dataModels.length; i++) {
            ids[i] = dataModels[i].getId();
        }
        DataModel[] dataModel = cacheUnit.getDataModels(ids);
        DataModel temp;
        for (int i = 0; i < dataModel.length; i++) {
            if (dataModel[i] == null || dataModel[i].getContent() == null) {
                if (daoFile.find(dataModels[i].getId()) != null) {
                    temp = lruAlgoCache.putElement(ids[i], daoFile.find(dataModels[i].getId()));
                    if (temp != null) {
                        swapsDataModels++;
                        daoFile.save(temp);
                    }
                }
                dataModel[i] = daoFile.find(dataModels[i].getId());
            }
        }
        return dataModel;
    }

    /**
     * The function update the data and update the statistic data about the server
     * @param dataModels - The data to update
     * @return The statistic data
     */
    public ArrayList<Object> update(DataModel<T>[] dataModels) {
        values = new ArrayList();
        values.add(0, basicUpdate(dataModels));
        statisticData();
        return values;

    }

    /**
     * The function delete the data and update the statistic data about the server
     * @param dataModels - The data to delete
     * @return The statistic data
     */
    public ArrayList<Object> delete(DataModel<T>[] dataModels) {
        values = new ArrayList();
        values.add(0, basicDelete(dataModels));
        statisticData();
        return values;

    }

    /**
     * The function return data according to the ids of the data models and update the statistic data about the server
     * @param dataModels - The data to return
     * @return The statistic data
     */
    public ArrayList<Object> get(DataModel<T>[] dataModels) {
        values = new ArrayList();
        values.add(0, basicGet(dataModels));
        statisticData();
        return values;
    }

    /**
     * The function update the statistic data about the server
     */
    private void statisticData() {
        String algo = String.valueOf(lruAlgoCache.getClass());
        if (algo.contains("LRU")) {
            algo = "LRU";
        } else if (algo.contains("MFU")) {
            algo = "MFU";
        } else {
            algo = "RANDOM";
        }
        values.add(1, lruAlgoCache.getCapacity());
        values.add(2, algo);
        values.add(3, totalDataModels);
        values.add(4, swapsDataModels);
        values.add(5, totalRequest);
    }

    /**
     * The function saves the ram to the hard disk when client close
     */
    public void onServerShutdown() {
        Map<Long, DataModel<T>> tempRam = lruAlgoCache.getRam();
        for (Long key : tempRam.keySet()) {
            daoFile.save(tempRam.get(key));
        }
    }
}
