package cia.arkrypto.auth.config;

import cia.arkrypto.auth.crypto.impl.Elgamal;
import cia.arkrypto.auth.crypto.impl.RSA;
import cia.arkrypto.auth.crypto.impl.rfid.Schnorr;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptoConfig {

    @Bean
    public Pairing pairing() {
        return PairingFactory.getPairing("a.properties");
    }

    @Bean
    public Field G1(Pairing pairing) {
        return pairing.getG1();
    }

    @Bean
    public Field G2(Pairing pairing) {
        return pairing.getG2();
    }

    @Bean
    public Field GT(Pairing pairing) {
        return pairing.getGT();
    }

    @Bean
    public Field Zr(Pairing pairing) {
        return pairing.getZr();
    }

    @Bean
    public RSA rsa(Field Zr) {
        return new RSA(Zr);
    }

    @Bean
    public cia.arkrypto.auth.crypto.impl.Schnorr schnorr(Field G1, Field Zr){
        return new cia.arkrypto.auth.crypto.impl.Schnorr(G1, Zr);
    }

    @Bean
    public Elgamal elgamal(Field Zr) {
        return new Elgamal(Zr);
    }

    @Bean
    public Schnorr schnorr_rfid(Field G1, Field Zr) {
        int length = 10; // 可替换配置参数
        return new Schnorr(G1, Zr, length);
    }
}
