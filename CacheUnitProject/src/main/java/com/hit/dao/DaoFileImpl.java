package main.java.com.hit.dao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import main.java.com.hit.dao.IDao;
import main.java.com.hit.dm.DataModel;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This class manages the removal and insertion of hard disk memory pages.
 *
 * @param <T> page value type.
 */


public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {
    private String filePath;
    private int capacity;
    private ArrayList<DataModel<T>> HardDisk;

    public DaoFileImpl(String filePath, int capacity)   {
        this.filePath = filePath;
        this.capacity = capacity;
        readJson();
    }

    public DaoFileImpl(String filePath)   {
        this.filePath = filePath;
        this.capacity = 1024;
        readJson();
    }

    //A function designed to write information from an array simulates the hard memory into a file
    private void writeJson() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(HardDisk, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //A function designed to read information from a file into an array simulates hard memory
    private void readJson()  {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<DataModel<T>>>() {
        }.getType();
        ArrayList<DataModel<T>> tempHardDisk;
        HardDisk = new ArrayList<>(capacity);
        try {
            tempHardDisk = gson.fromJson(new FileReader(filePath), listType);
            if (tempHardDisk != null) {
                HardDisk = tempHardDisk;
            }
        } catch (FileNotFoundException e) {
            HardDisk = new ArrayList<>(capacity);
        } catch (JsonSyntaxException e) {
            System.out.println("Problem with reading file: " + e.getMessage());
        }
    }

    @Override
    public void save(DataModel<T> entity) {
        if (entity==null){
            return;
        }
        for (int i = 0; i < HardDisk.size(); i++) {
            if (HardDisk.get(i).getId().equals(entity.getId())) {
                HardDisk.remove(i);
            }
        }
        if ((HardDisk.size() < capacity) && (entity!=null)) {
            HardDisk.add(entity);
            writeJson();
        } else {
            System.out.println("Hard disk is full");
        }
    }

    @Override
    public void delete(DataModel<T> entity) {
        HardDisk.removeIf(item -> (item.getId().equals(entity.getId()) && item.getContent().equals(entity.getContent())));
        writeJson();
    }

    @Override
    public DataModel<T> find(Long aLong) {
        try {
            if (aLong != null) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < HardDisk.size(); i++) {
            if (HardDisk.get(i).getId() == aLong) {
                return HardDisk.get(i);
            }
        }
        System.out.println("Hard disk doesn't have this page");
        return null;
    }
}
