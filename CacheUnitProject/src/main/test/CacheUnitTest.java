package main.test;

import main.java.com.hit.algorithm.IAlgoCache;
import main.java.com.hit.algorithm.MFUAlgoCacheImpl;
import main.java.com.hit.dao.DaoFileImpl;
import main.java.com.hit.dm.DataModel;
import main.java.com.hit.memory.CacheUnit;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Asserting the correctness of cache unit and doafile algorithms.
 */

public class CacheUnitTest {
    private static DataModel<String> dataModel;
    private static DataModel<String> dataModel2;
    private static DataModel<String> dataModel3;
    private static DataModel<String> dataModel4;
    private static DaoFileImpl<String> daoFile;
    private static CacheUnit<String> cacheUnit;
    private static MFUAlgoCacheImpl mfu;

    @BeforeClass
    public static void startTest() throws Exception {
        daoFile = new DaoFileImpl("src/main/resources/datasource.json");
        mfu = new MFUAlgoCacheImpl<Long, DataModel<String>>(20);
        cacheUnit = new CacheUnit((IAlgoCache<Long, DataModel>) mfu);
        dataModel = new DataModel(5L, "hello word");
        dataModel2 = new DataModel(6L, "!!!!!!!!!!");
        dataModel3 = new DataModel(7L, "=))");
        dataModel4 = new DataModel(8L, "{ }");
    }

    @Test
    public void daoFileTest() throws Exception {
        Assert.assertNotNull(daoFile.find(1L).getContent());
        daoFile.save(dataModel);
        Assert.assertEquals(dataModel, daoFile.find(5L));
        daoFile.save(dataModel2);
        Assert.assertEquals(dataModel2, daoFile.find(6L));
        daoFile.delete(dataModel);
        Assert.assertNull(daoFile.find(5L));
    }

    @Test
    public void cacheUnitTest() {
        Long[] ids = new Long[15];
        DataModel[] dataModels = new DataModel[15];
        for (int i = 0; i < 15; i++) {
            ids[i] = Long.valueOf(i);
            dataModels[i] = new DataModel(Long.valueOf(i), i);
        }
        cacheUnit.putDataModels(dataModels);
        Assert.assertEquals(cacheUnit.getDataModels(ids)[0], dataModels[0]);
        cacheUnit.removeDataModels(ids);
        Assert.assertNotEquals(cacheUnit.getDataModels(ids)[0], dataModels[0]);
    }
}

