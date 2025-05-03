package cia.arkrypto.auth.service;


import cia.arkrypto.auth.crypto.CipherSystem;
import cia.arkrypto.auth.crypto.impl.RSA;
import cia.arkrypto.auth.dto.Key;
import cia.arkrypto.auth.crypto.impl.Schnorr;
import cia.arkrypto.auth.dto.Signature;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final CipherSystem schnorr, rsa;
    @Autowired
    public AuthService(Schnorr schnorr, RSA rsa){
        this.schnorr = schnorr;
        this.rsa = rsa;
    }



    public Key keygen(String algo){
        if(algo.equalsIgnoreCase("schnorr")){
            return schnorr.keygen();
        } else if(algo.equalsIgnoreCase("rsa")){
            return rsa.keygen();
        }
        return null;
    }


    public Signature sign(String algo, Key key){
        if(algo.equalsIgnoreCase("schnorr")){
            return schnorr.sign(key);
        } else if(algo.equalsIgnoreCase("rsa")){
            return rsa.sign(key);
        }
        return null;
    }

    public Boolean verify(String algo, Key key, Signature signature){
        if (algo.equalsIgnoreCase("schnorr")) {
            return schnorr.verify(key, signature);
        } else if(algo.equalsIgnoreCase("rsa")){
            return rsa.verify(key, signature);
        }
        return null;
    }
}
