package cia.arkrypto.auth.crypto.impl;

import cia.arkrypto.auth.crypto.CipherSystem;
import cia.arkrypto.auth.dto.CryptoMap;
import cia.arkrypto.auth.dto.KeyPair;
import cia.arkrypto.auth.utils.HashUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Elgamal extends CipherSystem {

    public Elgamal(Field Zr) {
        super(null, null, null, null, Zr, false, false);
    }


    @Override
    public KeyPair keygen(){
        BigInteger p = BigInteger.probablePrime(1024, new SecureRandom());

        BigInteger g = randomZ().toBigInteger();

        BigInteger x = randomZ().toBigInteger();
        BigInteger y = g.modPow(x, p);

        KeyPair key = new KeyPair();
        key.sk.put("x", x);
        key.sk.put("g", g);
        key.sk.put("p", p);

        key.pk.put("g", g);
        key.pk.put("y", y);
        key.pk.put("p", p);

        return key;
    }


    @Override
    public CryptoMap sign(String message, CryptoMap sk){
        BigInteger g = sk.getBigInteger("g");
        BigInteger p = sk.getBigInteger("p");
        BigInteger x = sk.getBigInteger("x");

        BigInteger HM = HashUtil.hashStr2Group(getZr(), message).toBigInteger();
        // k 必须是可逆的
        BigInteger k = BigInteger.probablePrime(128, new SecureRandom());
        BigInteger k1 = k.modInverse(p.subtract(BigInteger.ONE)); // k的逆
        BigInteger r = g.modPow(k, p);
        BigInteger s = HM.subtract(x.multiply(r)).multiply(k1).mod(p.subtract(BigInteger.ONE));

        CryptoMap signature = new CryptoMap();
        signature.put("r", r);
        signature.put("s", s);

        return signature;
    }


    @Override
    public Boolean verify(String message, CryptoMap pk, CryptoMap signature){
        BigInteger g = pk.getBigInteger("g");
        BigInteger y = pk.getBigInteger("y");
        BigInteger p = pk.getBigInteger("p");
        BigInteger r = signature.getBigInteger("r");
        BigInteger s = signature.getBigInteger("s");

        BigInteger HM = HashUtil.hashStr2Group(getZr(), message).toBigInteger();
        BigInteger left = y.modPow(r, p).multiply(r.modPow(s, p)).mod(p);
        BigInteger right = g.modPow(HM, p);


        return left.equals(right);

    }
}
