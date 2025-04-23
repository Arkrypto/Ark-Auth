package cia.arkrypto.auth.crypto.impl;

import cia.arkrypto.auth.pojo.Key;
import cia.arkrypto.auth.pojo.Pair;
import cia.arkrypto.auth.utils.FixedSizeQueue;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


// 完整的展示认证过程
// 密钥分发
// 挑战-响应
// 认证

@Component
public class Schnorr {
    private static Pairing bp = PairingFactory.getPairing("a.properties");

    private static Field G, Zr;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class PK{
        Element Z;
        Element P;
    }

    private static final List<PK> list;

    // 初始化
    static{
        G = bp.getG1();
        Zr = bp.getZr();
        // 一个固定长度的队列
        list = new FixedSizeQueue<>(10);
    }

    // 密钥生成
    public Key keyGen(){
        Element P = G.newRandomElement().getImmutable();
        Element x = Zr.newRandomElement().getImmutable();
        Element Z = P.mulZn(x.negate()).getImmutable();

        list.add(new PK(Z, P));

        Key k = new Key("Z", Z);
        k.add("x", x);
        k.add("P", P);

        return k;
    }


    public static boolean auth(Element X, Element y, Element e){
        for(PK pk: list){
            if(pk.getP().mulZn(y).add(pk.getZ().mulZn(e)).isEqual(X)){
                return true;
            }
        }
        return false;
    }


    public List<Pair> simulate(){
        List<Pair> detail = new ArrayList<>();
        long st, et;

        st = System.currentTimeMillis();
        Key key = keyGen();
        et = System.currentTimeMillis();

        Element x = key.get("x");
        Element P = key.get("P");
        Element Z = key.get("Z");

        detail.add(new Pair("x", x));
        detail.add(new Pair("P", P));
        detail.add(new Pair("Z", Z));
        detail.add(new Pair("Key Generate Cost", et-st+"ms"));


        st = System.currentTimeMillis();
        Element r = Zr.newRandomElement().getImmutable();
        Element X = P.mulZn(r).getImmutable();
        Element e = Zr.newRandomElement().getImmutable();
        Element y = x.mul(e).add(r).getImmutable();
        boolean flag = auth(X, y, e);
        et = System.currentTimeMillis();


        detail.add(new Pair("r", r));
        detail.add(new Pair("X", X));
        detail.add(new Pair("e", e));
        detail.add(new Pair("y", y));
        detail.add(new Pair("yP+eZ", X));
        detail.add(new Pair("Auth Success", flag));
        detail.add(new Pair("Auth Cost", et-st+"ms"));

//        System.out.println(list.size());

        return detail;
    }

    public static void main(String[] args) {

        Schnorr schnorr = new Schnorr();
        List<Pair> detail = schnorr.simulate();
        for(Pair p: detail){
            System.out.println(p.fst + ": " + p.snd);
        }
    }
}
