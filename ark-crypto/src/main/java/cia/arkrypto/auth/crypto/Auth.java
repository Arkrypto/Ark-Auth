package cia.arkrypto.auth.crypto;

import cia.arkrypto.auth.dto.CryptoMap;
import cia.arkrypto.auth.dto.KeyPair;

public interface Auth {

    default KeyPair keygen(){
        throw new UnsupportedOperationException("Key Generation Is Not Supported");
    }

    default CryptoMap sign(String message, CryptoMap sk){
        throw new UnsupportedOperationException("Sign Is Not Supported");
    }

    default CryptoMap sanitize(String message, CryptoMap sk, CryptoMap signature){
        throw new UnsupportedOperationException("Sanitize Is Not Supported");
    }

    default Boolean verify(CryptoMap pk, CryptoMap signature){
        throw new UnsupportedOperationException("Verify Is Not Supported");
    }
}
