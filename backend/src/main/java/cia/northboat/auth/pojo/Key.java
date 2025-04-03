package cia.northboat.auth.pojo;

import it.unisa.dia.gas.jpbc.Element;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@Data
@AllArgsConstructor
public class Key {

    Map<String, Element> keys;

    public Key(){
        keys = new HashMap<>();
    }

    public Key(String id, Element key){
        keys = new HashMap<>();
        keys.put(id, key);
    }


    public void add(String id, Element key){
        keys.put(id, key);
    }

    public Element get(String id){
        return keys.get(id);
    }

}
