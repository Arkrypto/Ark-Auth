package cia.arkrypto.auth.crypto.impl;

import cia.arkrypto.auth.crypto.CipherSystem;
import cia.arkrypto.auth.dto.KeyPair;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.Map;

// Hidden Vector Encryption
public class HVE extends CipherSystem {

    public HVE(Pairing BP, Field G1, Field GT, Field Zr, Boolean sanitizable, Boolean updatable) {
        super(BP, G1, null, GT, Zr, sanitizable, updatable);
    }


    // 优化的 Gray 编码器
    public Map<String, String> grayOptimizer(Map<String, Double> cells){

        return null;
    }


    // 返回公私钥
    @Override
    public KeyPair keygen(){

        return null;
    }

}
