package cia.arkrypto.auth.test;

import cia.arkrypto.auth.crypto.CipherSystem;
import cia.arkrypto.auth.crypto.impl.Elgamal;
import cia.arkrypto.auth.crypto.impl.RSA;
import cia.arkrypto.auth.crypto.impl.SBS;
import cia.arkrypto.auth.dto.CryptoMap;
import cia.arkrypto.auth.dto.KeyPair;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.*;

public class CryptoTest {

    private static final Pairing BP = PairingFactory.getPairing("a.properties");
    private static final Field G1 = BP.getG1();
    private static final Field G2 = BP.getG2();
    private static final Field GT = BP.getGT();
    private static final Field Zr = BP.getZr();


    public static void main(String[] args) {

        CipherSystem rsa = new RSA(Zr);
        CipherSystem schnorr = new cia.arkrypto.auth.crypto.impl.Schnorr(G1, Zr);
        CipherSystem elgamal = new Elgamal(Zr);

        System.out.println(test(elgamal));
        System.out.println(test(rsa));
        System.out.println(test(schnorr));
        System.out.println(test());
    }


    public static Map<String, Long> test(){
        Map<String, Long> timeCost = new HashMap<>();
        CipherSystem sbs = new SBS(BP, G1, GT, Zr);

        long s1 = System.currentTimeMillis();
        KeyPair sig = sbs.keygen();
        KeyPair san = sbs.keygen();
        long e1 = System.currentTimeMillis();

        CryptoMap key = new CryptoMap();
        key.put("sk_sig", sig.sk.get("a"), "pk_sig", sig.pk.get("A"),
                "sk_san", san.sk.get("a"), "pk_san", san.pk.get("A"));

        long s2 = System.currentTimeMillis();
        CryptoMap signature = sbs.sign("test", key);
        long e2 = System.currentTimeMillis();

        long s3 = System.currentTimeMillis();
        System.out.println(sbs.verify(key, signature));
        long e3 = System.currentTimeMillis();

        long s4 = System.currentTimeMillis();
        CryptoMap signature1 = sbs.sanitize("sanit", key, signature);
        long e4 = System.currentTimeMillis();

        long s5 = System.currentTimeMillis();
        System.out.println(sbs.verify(key, signature1));
        long e5 = System.currentTimeMillis();

        timeCost.put("keygen", e1-s1);
        timeCost.put("sign", e2-s2);
        timeCost.put("verify-1", e3-s3);
        timeCost.put("sanit", e4-s4);
        timeCost.put("verify-2", e5-s5);

        return timeCost;
    }


    public static Map<String, Long> test(CipherSystem cipherSystem){
        Map<String, Long> timeCost = new HashMap<>();

        long s1 = System.currentTimeMillis();
        KeyPair key = cipherSystem.keygen();
        long e1 = System.currentTimeMillis();

        long s2 = System.currentTimeMillis();
        CryptoMap signature = cipherSystem.sign("test", key.sk);
        long e2 = System.currentTimeMillis();

        long s3 = System.currentTimeMillis();
        System.out.println(cipherSystem.verify(key.pk, signature));
        long e3 = System.currentTimeMillis();

        timeCost.put("keygen", e1-s1);
        timeCost.put("sign", e2-s2);
        timeCost.put("verify", e3-s3);

        return timeCost;
    }
}
