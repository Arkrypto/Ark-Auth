package cia.arkrypto.auth.crypto.impl;

import cia.arkrypto.auth.crypto.CipherSystem;
import cia.arkrypto.auth.dto.Key;
import cia.arkrypto.auth.dto.Signature;
import cia.arkrypto.auth.utils.FixedSizeQueue;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;


// 完整的展示认证过程
// 密钥分发
// 签名
// 验证
public class Schnorr extends CipherSystem {

    private static List<PK> list = null;

    public Schnorr(Pairing BP, Field G1, Field G2, Field GT, Field Zr, Boolean sanitizable, Boolean updatable, int length) {
        super(BP, G1, G2, GT, Zr, sanitizable, updatable);
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
    public Key keygen(){
        Element P = randomG1();
        Element x = randomZ();
        Element Z = P.mulZn(x.negate()).getImmutable();
        Element r = randomZ();
        Element X = P.mulZn(r).getImmutable();


        list.add(new PK(Z, P));

        Key k = new Key();
        k.add("Z", Z);
        k.add("x", x);
        k.add("P", P);
        k.add("r", r);
        k.add("X", X);

        return k;
    }

    @Override
    public Signature sign(Key key){
        Element e = randomZ();
        Element x = (Element) key.get("x");
        Element sigma = x.mul(e).add((Element) key.get("r")).getImmutable();

        Signature signature = new Signature();
        signature.add("e", e);
        signature.add("sigma", sigma);

        return signature;
    }




    @Override
    public Boolean verify(Key key, Signature signature){
        // 查表检查密钥
        for(PK pk: list){
            if(pk.getP().mulZn((Element) signature.get("sigma")).add(pk.getZ().mulZn((Element) signature.get("e"))).isEqual((Element) key.get("X"))){
                return true;
            }
        }
        return false;
    }


}
