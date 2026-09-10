package Modelo;

import java.util.ArrayList;
import java.util.List;

public class DataManager<T> {
    private List<T> data;
    
    public DataManager() {
        data = new ArrayList<>();
    }
    
    public void add(T item) {
        data.add(item);
    }
    
    public List<T> getAll() {
        return data;
    }
}