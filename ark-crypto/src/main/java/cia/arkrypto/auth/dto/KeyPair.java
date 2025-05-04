package cia.arkrypto.auth.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class KeyPair {
    public CryptoMap sk;
    public CryptoMap pk;

    public KeyPair(){
        sk = new CryptoMap();
        pk = new CryptoMap();
    }

    public void print(){
        sk.print();
        pk.print();
    }
}
