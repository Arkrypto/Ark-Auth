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

    public String toString(){
        return pk.toString() + "\n" + sk.toString();
    }
}
