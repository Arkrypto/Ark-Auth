package cia.arkrypto.auth.test;

import cia.arkrypto.auth.crypto.CipherSystem;
import cia.arkrypto.auth.crypto.impl.Elgamal;
import cia.arkrypto.auth.crypto.impl.RSA;
import cia.arkrypto.auth.crypto.impl.rfid.Schnorr;
import cia.arkrypto.auth.dto.CryptoMap;
import cia.arkrypto.auth.dto.KeyPair;
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

        CipherSystem schnorrRFID = new Schnorr(G1, Zr, 10);
        CipherSystem rsa = new RSA(Zr);
        CipherSystem schnorr = new cia.arkrypto.auth.crypto.impl.Schnorr(G1, Zr);
        CipherSystem elgamal = new Elgamal(Zr);

        KeyPair key = elgamal.keygen();
        CryptoMap signature = elgamal.sign("null", key.sk);
        System.out.println(elgamal.verify("null", key.pk, signature));


    }
}
