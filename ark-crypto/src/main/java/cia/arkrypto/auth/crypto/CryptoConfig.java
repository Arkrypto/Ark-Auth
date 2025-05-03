package cia.arkrypto.auth.crypto;

import cia.arkrypto.auth.crypto.impl.RSA;
import cia.arkrypto.auth.crypto.impl.Schnorr;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

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
    public Schnorr schnorr(Pairing pairing, Field G1, Field G2, Field GT, Field Zr) {
        int length = 10; // 可替换为配置参数
        boolean sanitizable = false; // 同上
        boolean updatable = false;
        return new Schnorr(pairing, G1, G2, GT, Zr, sanitizable, updatable, length);
    }

    @Bean
    public RSA rsa() {
        boolean sanitizable = false;
        boolean updatable = false;
        return new RSA(sanitizable, updatable);
    }
}
