package cia.arkrypto.auth.service;


import cia.arkrypto.auth.crypto.CipherSystem;
import cia.arkrypto.auth.crypto.impl.Elgamal;
import cia.arkrypto.auth.crypto.impl.RSA;
import cia.arkrypto.auth.crypto.impl.Schnorr;
import cia.arkrypto.auth.dto.KeyPair;
import cia.arkrypto.auth.crypto.impl.SchnorrRFID;
import cia.arkrypto.auth.dto.CryptoMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CipherSystem schnorrRFID, rsa, schnorr, elgamal;
    @Autowired
    public AuthService(SchnorrRFID schnorrRFID, RSA rsa, Schnorr schnorr, Elgamal elgamal){
        this.schnorrRFID = schnorrRFID;
        this.rsa = rsa;
        this.schnorr = schnorr;
        this.elgamal = elgamal;
    }



    public KeyPair keygen(String algo){
        if (algo.equalsIgnoreCase("schnorr")){
            return schnorr.keygen();
        } else if (algo.equalsIgnoreCase("rsa")){
            return rsa.keygen();
        } else if (algo.equalsIgnoreCase("elgamal")){
            return elgamal.keygen();
        } else if (algo.equalsIgnoreCase("schnorrRFID")){
            return schnorrRFID.keygen();
        }
        return null;
    }


    public CryptoMap sign(String algo, String message, CryptoMap sk){
        if (algo.equalsIgnoreCase("schnorr")){
            return schnorr.sign(message, sk);
        } else if(algo.equalsIgnoreCase("rsa")){
            return rsa.sign(message, sk);
        } else if (algo.equalsIgnoreCase("elgamal")){
            return elgamal.sign(message, sk);
        } else if(algo.equalsIgnoreCase("schnorrRFID")){
            return schnorrRFID.sign(message, sk);
        }
        return null;
    }

    public Boolean verify(String algo, String message, CryptoMap pk, CryptoMap signature){
        if (algo.equalsIgnoreCase("schnorr")){
            return schnorr.verify(message, pk, signature);
        } else if(algo.equalsIgnoreCase("rsa")){
            return rsa.verify(message, pk, signature);
        } else if (algo.equalsIgnoreCase("elgamal")){
            return elgamal.verify(message, pk, signature);
        } else if(algo.equalsIgnoreCase("schnorrRFID")) {
            return schnorrRFID.verify(message, pk, signature);
        }
        return null;
    }
}
