package cia.arkrypto.auth.crypto.impl.rfid;

import cia.arkrypto.auth.crypto.CipherSystem;
import cia.arkrypto.auth.dto.KeyPair;
import cia.arkrypto.auth.dto.CryptoMap;
import cia.arkrypto.auth.utils.FixedSizeQueue;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


// 完整的展示认证过程
// 密钥分发
// 签名
// 验证
public class Schnorr extends CipherSystem {

    private static List<PK> list = null;

    public Schnorr(Field G1, Field Zr, int length) {
        super(null, G1, null, null, Zr, false, false);
        list = new FixedSizeQueue<>(length);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class PK{
        Element Z;
        Element P;
    }


    // 密钥生成
    @Override
    public KeyPair keygen(){
        Element P = randomG1();
        Element x = randomZ();
        Element Z = P.mulZn(x.negate()).getImmutable();
        Element r = randomZ();
        Element X = P.mulZn(r).getImmutable();


        list.add(new PK(Z, P));

        KeyPair k = new KeyPair();
        k.sk.put("x", x);
        k.sk.put("r", r);
        k.pk.put("X", X);

        return k;
    }

    @Override
    public CryptoMap sign(String message, CryptoMap sk){
        Element e = randomZ();
        Element x = sk.get("x", getZr());
        Element sigma = x.mul(e).add(sk.get("r", getZr())).getImmutable();

        CryptoMap signature = new CryptoMap();
        signature.put("e", e);
        signature.put("sigma", sigma);

        return signature;
    }




    @Override
    public Boolean verify(String message, CryptoMap pk, CryptoMap signature){
        Element sigma = signature.get("sigma", getZr());
        Element e = signature.get("e", getZr());
        Element X = pk.get("X", getG1());
        // 查表检查密钥
        for(PK key: list){
            if(key.getP().mulZn(sigma).add(key.getZ().mulZn(e)).isEqual(X)){
                return true;
            }
        }
        return false;
    }


}
