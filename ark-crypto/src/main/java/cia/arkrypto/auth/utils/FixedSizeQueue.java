package cia.arkrypto.auth.utils;

import java.util.ArrayList;

public class FixedSizeQueue<T> extends ArrayList<T> {

    private final int capacity;

    public FixedSizeQueue(int capacity){
        super();
        this.capacity = capacity;
    }

    @Override
    public boolean add(T e){
        if(size()+1 > capacity){
            super.remove(0);
        }
        return super.add(e);
    }
}
