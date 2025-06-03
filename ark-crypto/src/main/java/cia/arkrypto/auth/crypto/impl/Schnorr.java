package cia.arkrypto.auth.crypto.impl;

import cia.arkrypto.auth.crypto.CipherSystem;
import cia.arkrypto.auth.dto.CryptoMap;
import cia.arkrypto.auth.dto.KeyPair;
import cia.arkrypto.auth.utils.HashUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

public class Schnorr extends CipherSystem {


    public Schnorr(Field G1, Field Zr) {
        super(null, G1, null, null, Zr, false, false);
    }



    // y = xG
    @Override
    public KeyPair keygen(){
        Element g = randomG1();
        Element x = randomZ();
        Element y = g.mulZn(x).getImmutable();

        KeyPair keyPair = new KeyPair();
        keyPair.sk.put("g", g);
        keyPair.sk.put("x", x);
        keyPair.pk.put("g", g);
        keyPair.pk.put("y", y);

        return keyPair;
    }


    // R = rG, c = H(msg, R), z = r+cx
    @Override
    public CryptoMap sign(String message, CryptoMap sk){
        Element g = sk.get("g", getG1());
        Element x = sk.get("x", getZr());

        Element r = randomZ();
        Element R = g.mulZn(r).getImmutable();
        Element c = HashUtil.hashStr2Group(getZr(), message, R.toString());
        Element z = r.add(x.mulZn(c)).getImmutable();


        CryptoMap signature = new CryptoMap();
        signature.put("z", z);
        signature.put("c", c);

        return signature;
    }

    // R1 = zG-xGc = (r+cx)G-cxG = rG = R
    // H(msg, R1) = H(msg, R) = c
    @Override
    public Boolean verify(String message, CryptoMap pk, CryptoMap signature){
        Element y = pk.get("y", getG1());
        Element g = pk.get("g", getG1());
        Element z = signature.get("z", getZr());
        Element c = signature.get("c", getZr());

        Element R1 = g.mulZn(z).sub(y.mulZn(c)).getImmutable();


        return c.isEqual(HashUtil.hashStr2Group(getZr(), message, R1.toString()));
    }
}
