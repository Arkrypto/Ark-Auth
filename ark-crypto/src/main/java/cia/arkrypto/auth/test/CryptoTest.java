package cia.arkrypto.auth.test;

import cia.arkrypto.auth.crypto.CipherSystem;
import cia.arkrypto.auth.crypto.impl.RSA;
import cia.arkrypto.auth.crypto.impl.Schnorr;
import cia.arkrypto.auth.dto.Key;
import cia.arkrypto.auth.dto.Signature;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class CryptoTest {

    private static final Pairing BP = PairingFactory.getPairing("a.properties");
    private static final Field G1 = BP.getG1();
    private static final Field G2 = BP.getG2();
    private static final Field GT = BP.getGT();
    private static final Field Zr = BP.getZr();


    public static void main(String[] args) {

        CipherSystem schnorr = new Schnorr(BP, G1, G2, GT, Zr, false, false, 10);
        CipherSystem rsa = new RSA(false, false);

        Key key = rsa.keygen();
        Signature signature = rsa.sign(key);
        Boolean flag = rsa.verify(key, signature);


        key.print();
        signature.print();
        System.out.println(flag);

    }
}
