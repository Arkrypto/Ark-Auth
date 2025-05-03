package cia.arkrypto.auth.dto;

import it.unisa.dia.gas.jpbc.Element;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Signature {
    Map<String, Object> signatures;

    public Signature(){
        signatures = new HashMap<>();
    }

    public void add(String key, Object val){
        signatures.put(key, val);
    }

    public Object get(String id){
        return signatures.get(id);
    }

    public void print(){
        System.out.println(signatures.toString());
    }
}
