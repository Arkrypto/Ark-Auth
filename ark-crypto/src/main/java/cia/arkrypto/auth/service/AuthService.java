package cia.arkrypto.auth.service;


import cia.arkrypto.auth.crypto.CipherSystem;
import cia.arkrypto.auth.crypto.impl.Elgamal;
import cia.arkrypto.auth.crypto.impl.RSA;
import cia.arkrypto.auth.dto.KeyPair;
import cia.arkrypto.auth.crypto.impl.rfid.Schnorr;
import cia.arkrypto.auth.dto.CryptoMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CipherSystem schnorr_rfid, rsa, schnorr, elgamal;
    @Autowired
    public AuthService(Schnorr schnorr_rfid, RSA rsa, cia.arkrypto.auth.crypto.impl.Schnorr schnorr, Elgamal elgamal){
        this.schnorr_rfid = schnorr_rfid;
        this.rsa = rsa;
        this.schnorr = schnorr;
        this.elgamal = elgamal;
    }


    public CipherSystem selectSystem(String algo){
        if (algo.equalsIgnoreCase("schnorr")){
            return schnorr;
        } else if (algo.equalsIgnoreCase("rsa")){
            return rsa;
        } else if (algo.equalsIgnoreCase("elgamal")){
            return elgamal;
        } else if (algo.equalsIgnoreCase("schnorr_rfid")){
            return schnorr_rfid;
        }
        return null;
    }


    public KeyPair keygen(String algo){
        CipherSystem cipherSystem = selectSystem(algo);
        if(cipherSystem == null){
            return null;
        }
        return cipherSystem.keygen();
    }


    public CryptoMap sign(String algo, String message, CryptoMap sk){
        CipherSystem cipherSystem = selectSystem(algo);
        if(cipherSystem == null){
            return null;
        }
        return cipherSystem.sign(message, sk);
    }

    public Boolean verify(String algo, String message, CryptoMap pk, CryptoMap signature){
        CipherSystem cipherSystem = selectSystem(algo);
        if(cipherSystem == null){
            return null;
        }
        return cipherSystem.verify(message, pk, signature);
    }
}
