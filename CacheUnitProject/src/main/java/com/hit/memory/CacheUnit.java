package main.java.com.hit.memory;


import main.java.com.hit.algorithm.IAlgoCache;
import main.java.com.hit.dm.DataModel;

/**
 *This class implements the IAlgoCash methods on arrays of memory pages.
 * @param <T> page value type
 */


public class CacheUnit <T>{
    private IAlgoCache<Long, DataModel<T>> algoCache;

    public CacheUnit(IAlgoCache<Long, DataModel<T>> algoCache)  {
        this.algoCache = algoCache;
    }

    public DataModel<T>[] getDataModels(Long[] ids) {
        DataModel<T>[] returnPages = new DataModel[ids.length];
        for (int i = 0; i < ids.length ; i++ ) {
            if(algoCache.getElement(ids[i])!=null && algoCache.getElement(ids[i]).getContent()!=null) {
                returnPages[i] = new DataModel(ids[i], algoCache.getElement(ids[i]).getContent());
            }
            else {
                returnPages[i] = null;
            }
        }
        return  returnPages;
    }

    public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) {
        DataModel<T>[] returnPages = new DataModel[datamodels.length];
        for (int i = 0; i < datamodels.length ; i++ ) {
            if(datamodels[i] != null && datamodels[i].getContent() != null)
                returnPages[i] =  algoCache.putElement(datamodels[i].getId(), datamodels[i] ) ;
        }
        return  returnPages;
    }

     public void removeDataModels(Long[] ids){
         for (int i = 0; i < ids.length ; i++ ) {
              algoCache.removeElement(ids[i]) ;
         }
     }
}