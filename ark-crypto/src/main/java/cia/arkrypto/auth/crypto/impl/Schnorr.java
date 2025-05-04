package cia.arkrypto.auth.crypto.impl;

import cia.arkrypto.auth.crypto.CipherSystem;
import cia.arkrypto.auth.dto.CryptoMap;
import cia.arkrypto.auth.dto.KeyPair;
import cia.arkrypto.auth.utils.HashUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

public class Schnorr extends CipherSystem {


    public Schnorr(Pairing BP, Field G1, Field Zr, Boolean sanitizable, Boolean updatable) {
        super(BP, G1, null, null, Zr, sanitizable, updatable);
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
        Element g = sk.getElement("g", getG1());
        Element x = sk.getElement("x", getZr());

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
        Element y = pk.getElement("y", getG1());
        Element g = pk.getElement("g", getG1());
        Element z = signature.getElement("z", getZr());
        Element c = signature.getElement("c", getZr());

        Element R1 = g.mulZn(z).sub(y.mulZn(c)).getImmutable();


        return c.isEqual(HashUtil.hashStr2Group(getZr(), message, R1.toString()));
    }
}
