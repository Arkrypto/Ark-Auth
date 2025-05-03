package cia.arkrypto.auth.crypto;

import cia.arkrypto.auth.dto.Key;
import cia.arkrypto.auth.dto.Signature;

public interface Auth {
    default Key keygen(){
        throw new UnsupportedOperationException("Key Generation Is Not Supported");
    }

    default Signature sign(Key key){
        throw new UnsupportedOperationException("Sign Is Not Supported");
    }

    default Signature sanitize(Key key, Signature signature){
        throw new UnsupportedOperationException("Sanitize Is Not Supported");
    }

    default Boolean verify(Key key, Signature signature){
        throw new UnsupportedOperationException("Verify Is Not Supported");
    }
}
