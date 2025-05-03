package cia.arkrypto.auth.crypto.impl;

import cia.arkrypto.auth.crypto.CipherSystem;
import cia.arkrypto.auth.dto.Key;
import cia.arkrypto.auth.dto.Signature;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.math.BigInteger;
import java.security.SecureRandom;


// JPBC 并不适用，用 BigInteger 手写的
public class RSA extends CipherSystem {

    public RSA(Boolean sanitizable, Boolean updatable) {
        super(null, null, null, null, null, sanitizable, updatable);
    }

    @Override
    public Key keygen(){

        BigInteger p = BigInteger.probablePrime(512, new SecureRandom());
        BigInteger q = BigInteger.probablePrime(512, new SecureRandom());
        BigInteger n = p.multiply(q);
        // 欧拉函数
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger e = BigInteger.probablePrime(128, new SecureRandom());
        BigInteger d = e.modInverse(phi);


        Key k = new Key();
        k.add("d", d);
        k.add("e", e);
        k.add("n", n);

        return k;
    }


    @Override
    public Signature sign(Key key){
        // 签名
        BigInteger m = BigInteger.probablePrime(64, new SecureRandom());
        BigInteger s = m.modPow((BigInteger) key.get("d"), (BigInteger) key.get("n"));

        Signature signature = new Signature();
        signature.add("m", m);
        signature.add("s", s); // s = m^d % n

        return signature;
    }


    @Override
    public Boolean verify(Key key, Signature signature){
        BigInteger s = (BigInteger) signature.get("s");
        BigInteger recovered = s.modPow((BigInteger) key.get("e"), (BigInteger) key.get("n"));
        BigInteger m = (BigInteger) signature.get("m");
        return m.equals(recovered);
    }

}
