package cia.arkrypto.auth.dto;

import it.unisa.dia.gas.jpbc.Element;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class Key {

    Map<String, Object> keys;

    public Key(){
        keys = new HashMap<>();
    }

    public void add(String key, Object val){
        keys.put(key, val);
    }

    public Object get(String id){
        return keys.get(id);
    }

    public void print(){
        System.out.println(keys.toString());
    }

}
